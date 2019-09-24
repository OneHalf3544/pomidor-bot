package ru.onehalf.timemanagement.telegrambot.telegram

import kotlin.Pair
import ru.onehalf.timemanagement.telegrambot.telegram.client.TelegramClient
import spock.lang.Specification

import static java.time.Duration.*
import static org.mockito.Mockito.mock

class TaskMessageServiceTest extends Specification {

    def "can parse time from message #message"() {
        def telegramClientMock = mock(TelegramClient.class)
        def echoMessageService = new TaskMessageService(telegramClientMock)

        expect:
        echoMessageService.parseText(message) == new Pair(duration, taskText)

        where:
        duration      | taskText         | message
        ofMinutes(30) | "Code function"  | "30 min Code function"
        ofSeconds(10) | "Take a rest"    | "10 seconds take a rest"
        ofHours(1)    | "To read a book" | "hour to read a book"
        ofMinutes(30) | "Just a task"    | "just a task"
    }
}
