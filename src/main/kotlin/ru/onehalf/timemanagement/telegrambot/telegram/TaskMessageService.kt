package ru.onehalf.timemanagement.telegrambot.telegram

import org.slf4j.LoggerFactory
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import ru.onehalf.timemanagement.telegrambot.telegram.client.TelegramClient
import ru.onehalf.timemanagement.telegrambot.telegram.client.Update
import java.lang.Exception
import java.time.Duration
import java.time.Duration.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit.*

@Component
class TaskMessageService(private val telegramClient: TelegramClient) {

    private val log = LoggerFactory.getLogger(TaskMessageService::class.java)

    private val scheduledExecutorService = Executors.newScheduledThreadPool(2)

    private var lastUpdateId: Long = 0

    @EventListener(ContextRefreshedEvent::class)
    fun onApplicationEvent() {
        scheduledExecutorService.scheduleWithFixedDelay(this::processNewMessages, 0, 1, SECONDS)
    }

    private fun processNewMessages() {
        try {
            val updates = telegramClient.getUpdates(lastUpdateId)
            for (message in updates.result) {
                val text = message.message?.text!!
                val messageId = message.message!!.message_id
                log.info("received message: {}", text)
                processMessage(message, text, messageId)
            }
            if (updates.result.isNotEmpty()) {
                lastUpdateId = updates.result.map{it.update_id}.max()?.inc()?: lastUpdateId
            }

        } catch (e: Exception) {
            log.warn("error", e)
            SECONDS.sleep(10)
        }
    }

    private fun processMessage(message: Update, text: String, messageId: Long) {
        val parsedMessage = parseText(text)
        telegramClient.sendMessage(message.message?.chat!!.id, "I will send you remind in ${parsedMessage.first}.", messageId)

        scheduledExecutorService.schedule(
                {telegramClient.sendMessage(
                        message.message?.chat!!.id,
                        "Have you finished this task?", messageId)},
                parsedMessage.first.toMillis(), MILLISECONDS
        )
    }


    private fun parseText(text: String): Pair<Duration, String> {


        val numberString = text.takeWhile { it.isDigit() }
        val trimmedString: String
        val number: Long
        if (numberString.isEmpty()) {
            number = 1L
            trimmedString = text.trim()
        } else {
            number = numberString.toLong()
            trimmedString = text.substring(numberString.length).trim()
        }
        val unit = trimmedString.takeWhile { it.isLetter() }.toLowerCase()
        fun restString() = trimmedString.substring(unit.length).trim().capitalize()

        return when(unit) {
            "m", "min", "minute", "minutes" -> Pair(ofMinutes(number), restString())
            "s", "sec", "second", "seconds" -> Pair(ofSeconds(number), restString())
            "h", "hour", "hours"            -> Pair(ofHours(number), restString())
            else -> defaultTaskTime(text)
        }
    }

    private fun defaultTaskTime(text: String) = Pair(ofMinutes(30), text.capitalize())
}
