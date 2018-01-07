package ru.onehalf.timemanagement.telegrambot.server

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.filter.ServletContextRequestLoggingFilter
import javax.servlet.Filter

@Configuration
open class ServerConfiguration {

    @Bean
    open fun servletContextRequestLoggingFilter(): Filter {
        val loggingFilter = ServletContextRequestLoggingFilter()
        loggingFilter.setIncludePayload(true)
        loggingFilter.setIncludeQueryString(true)
        return loggingFilter
    }
}