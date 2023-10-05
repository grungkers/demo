package com.tech.test.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service

@Service
class NotificationService {
    @Lazy
    @Autowired
    private lateinit var simpMessagingTemplate: SimpMessagingTemplate

    fun sendNotification(destination: String, message: String){
        simpMessagingTemplate.convertAndSend(destination, message)
    }
}