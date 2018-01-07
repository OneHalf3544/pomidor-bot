package ru.onehalf.timemanagement.telegrambot.spring

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.onehalf.timemanagement.telegrambot.telegram.client.TelegramClient

@RestController
@RequestMapping("/telegrambot", produces = ["application/json"])
class Controller(private val client: TelegramClient) {

    private val chatId = 124535103L

    @GetMapping("/getUpdates")
    fun root(@RequestParam("lastMessageId") offset: Long?) = client.getUpdates(offset?:0)

    @GetMapping("/sendMessage")
    fun sendMessage(@RequestParam("text") text: String?): String {
        client.sendMessage(chatId, text?:"Hello world!", null)
        return """{"success": true}"""
    }
}