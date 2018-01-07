package ru.onehalf.timemanagement.telegrambot.telegram.client

import org.springframework.stereotype.Component
import ru.onehalf.timemanagement.telegrambot.http.HttpClient

@Component
class TelegramClient(private val httpClient: HttpClient) {

    private val token: String = "530998288:AAGdfM6NyRkhTAuku2ViCZRLWi8t4U48ZaI"

    fun getUpdates(): String {
        return httpClient.execute(botApiUrl("getUpdates"), """
                    {
                        "offset": null,
                        "limit": 100,
                        "timeout": 4,
                        "allowed_updates": []
                    }
                    """.trimIndent())
    }

    fun sendMessage(chatId: Int, text: String) {
        httpClient.execute(botApiUrl("sendMessage"), """
                    {
                        "chat_id": $chatId,
                        "text": "$text",
                        "reply_to_message_id": null
                    }
                    """.trimIndent())
    }

    private fun botApiUrl(commandName: String) = "https://api.telegram.org/bot$token/$commandName"

}