package ru.onehalf.timemanagement.telegrambot.telegram

import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component
import ru.onehalf.timemanagement.telegrambot.telegram.client.TelegramClient
import java.lang.Exception
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit.SECONDS

@Component
class EchoMessageService(private val telegramClient: TelegramClient) : ApplicationListener<ContextRefreshedEvent> {

    private val log = LoggerFactory.getLogger(EchoMessageService::class.java)

    private val scheduledExecutorService = Executors.newSingleThreadScheduledExecutor()

    private var lastUpdateId: Long = 0

    override fun onApplicationEvent(event: ContextRefreshedEvent?) {
        scheduledExecutorService.scheduleWithFixedDelay(this::sendEchoToSenders, 0, 1, SECONDS)
    }

    fun sendEchoToSenders() {
        try {
            val updates = telegramClient.getUpdates(lastUpdateId)
            for (message in updates.result) {
                val text = message.message?.text
                val messageId = message.message?.message_id
                log.info("received message: {}", text)
                telegramClient.sendMessage(message.message?.chat!!.id, "reply to $text", messageId)
            }
            if (updates.result.isNotEmpty()) {
                lastUpdateId = updates.result.map{it.update_id}.max()?.inc()?: lastUpdateId
            }

        } catch (e: Exception) {
            log.warn("error", e)
            SECONDS.sleep(10)
        }
    }
}
