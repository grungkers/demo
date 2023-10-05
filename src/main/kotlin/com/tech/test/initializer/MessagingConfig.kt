package com.tech.test.initializer

import jakarta.jms.ConnectionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Lazy
import org.springframework.jms.listener.DefaultMessageListenerContainer
import org.springframework.messaging.core.GenericMessagingTemplate
import org.springframework.stereotype.Component

@Component
class MessagingConfig() {
    @Lazy
    @Autowired
    private lateinit var messageConsumer: MessageConsumer
    @Lazy
    @Autowired
    private lateinit var connectionFactory: ConnectionFactory
    @Bean
    fun messageListener(): DefaultMessageListenerContainer {
        val container = DefaultMessageListenerContainer()
        container.setConnectionFactory(connectionFactory)
        container.setDestinationName("#.topic.device.#")
        container.setMessageListener(messageConsumer)
        return container
    }
}