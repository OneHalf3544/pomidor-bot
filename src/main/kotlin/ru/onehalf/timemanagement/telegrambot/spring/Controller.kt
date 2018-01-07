package ru.onehalf.timemanagement.telegrambot.spring

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.onehalf.timemanagement.telegrambot.telegram.client.TelegramClient

@RestController
@RequestMapping("/telegrambot", produces = ["application/json"])
class Controller(private val client: TelegramClient) {

    private val chatId = 124535103

    @GetMapping("/getUpdates")
    fun root() = client.getUpdates()

    @GetMapping("/sendMessage")
    fun sendMessage(): String {
        client.sendMessage(chatId, "Hello world!")
        return """{"success": true}"""
    }
}