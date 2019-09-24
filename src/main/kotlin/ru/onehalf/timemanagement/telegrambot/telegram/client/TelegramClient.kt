package ru.onehalf.timemanagement.telegrambot.telegram.client

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import ru.onehalf.timemanagement.telegrambot.http.HttpClient

class GetUpdatesResponse(
        var ok: Boolean? = null,
        var result: List<Update> = listOf())

class From(var id: Long = 0,
           var isIs_bot: Boolean = false,
           var first_name: String? = null,
           var last_name: String? = null,
           var username: String? = null,
           var language_code: String? = null)

class Chat(var id: Long = 0,
           var first_name: String? = null,
           var last_name: String? = null,
           var username: String? = null,
           var type: String? = null)

class Message(var message_id: Long = 0,
              var from: From? = null,
              var chat: Chat? = null,
              var date: Long? = null,
              var text: String? = null)

class Update(var update_id: Long = 0,
             var message: Message? = null)


@Component
open class TelegramClient(private val httpClient: HttpClient) {

    private val token: String = "530998288:AAGdfM6NyRkhTAuku2ViCZRLWi8t4U48ZaI"

    private val objectMapper = ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun getUpdates(offset: Long): GetUpdatesResponse {
        val body = httpClient.execute(botApiUrl("getUpdates"), """
                    {
                        "offset": $offset,
                        "limit": 100,
                        "timeout": 60,
                        "allowed_updates": []
                    }
                    """.trimIndent())
        return objectMapper.readValue(body, GetUpdatesResponse::class.java)
    }
    fun sendMessage(chatId: Long, text: String, replyTo: Long?) {
        httpClient.execute(botApiUrl("sendMessage"), """
                    {
                        "chat_id": $chatId,
                        "text": "$text",
                        "reply_to_message_id": $replyTo
                    }
                    """.trimIndent())
    }

    private fun botApiUrl(commandName: String) = "https://api.telegram.org/bot$token/$commandName"

}
