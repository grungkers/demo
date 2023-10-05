package com.tech.test.initializer

import com.tech.test.service.NotificationService
import jakarta.jms.Message
import jakarta.jms.MessageListener
import org.apache.activemq.artemis.jms.client.ActiveMQQueue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

@Component
class MessageConsumer: MessageListener {
    @Lazy
    @Autowired
    private lateinit var notificationService: NotificationService
    override fun onMessage(message: Message) {
        notificationService.sendNotification("/${(message.jmsDestination as ActiveMQQueue).address.replace(".", "/")}", message.getBody(String::class.java))
    }
}