package ru.onehalf.timemanagement.telegrambot.telegram.client

import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.junit.jupiter.api.Test

internal class GetUpdatesResponseTest {

    @Test
    fun should_deserializeGetUpdateResponse() {
        val readValue = ObjectMapper().readValue(
                """{
                    "ok":true,
                    "result":[
                        {
                            "update_id":123101763,
                            "message":{"message_id":5,"from":{"id":124535103,"is_bot":false,"first_name":"Mikhail","last_name":"Sergeev","username":"OneHalf3544","language_code":"en-US"},"chat":{"id":124535103,"first_name":"Mikhail","last_name":"Sergeev","username":"OneHalf3544","type":"private"},"date":1515333987,"text":"fsd"}},
                        {
                            "update_id":123101764,
                            "message":{"message_id":6,"from":{"id":124535103,"is_bot":false,"first_name":"Mikhail","last_name":"Sergeev","username":"OneHalf3544","language_code":"en-US"},"chat":{"id":124535103,"first_name":"Mikhail","last_name":"Sergeev","username":"OneHalf3544","type":"private"},"date":1515336834,"text":"fd"}}
                    ]}""",
                GetUpdatesResponse::class.java)
        assertThat(readValue?.result?.last()?.update_id, equalTo(123101764L))
    }
}