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
            .connectTimeout(5, TimeUnit.SECONDS)
            .build()


    fun execute(url: String, jsonBody: String): String {
        if (log.isDebugEnabled) {
            log.debug("execute call: url={}, body={}", url, jsonBody)
        } else {
            log.info("execute call: url={}", url)
        }

        val result = httpClient.newCall(Request.Builder()
                .url(url)
                .post(RequestBody.create(MediaType.parse("application/json"), jsonBody))
                .build())
                .execute()

        val body = result.body()?.string()

        if (log.isDebugEnabled) {
            log.debug("http call result: {}, {}", result, body)
        } else {
            log.info("http call finished: {}", url)
        }

        return body?:"";
    }

}