package ru.onehalf.timemanagement.telegrambot.http

import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class HttpClient {

    private val log = LoggerFactory.getLogger(HttpClient::class.java)

    private val httpClient: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(5, TimeUnit.SECONDS)
            .build()


    fun execute(url: String, jsonBody: String): String {
        log.info("execute call: url={}, body={}", url, jsonBody)
        val result = httpClient.newCall(Request.Builder()
                .url(url)
                .post(RequestBody.create(MediaType.parse("application/json"), jsonBody))
                .build())
                .execute()

        val body = result
                .body()?.string()

        log.info("http call result: {}, {}", result, body)

        return body?:"";
    }

}