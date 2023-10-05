package com.tech.test.service

import com.tech.test.model.BookingTransaction
import com.tech.test.model.Device
import com.tech.test.model.User
import com.tech.test.respository.BookingTxnRepository
import com.tech.test.respository.PhoneDeviceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.jms.core.JmsTemplate
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.*


@Service
class BookingService {
    @Autowired
    private lateinit var deviceRepository: PhoneDeviceRepository
    @Autowired
    private lateinit var bookingTxnRepository: BookingTxnRepository

    @Lazy
    @Autowired
    private lateinit var jmsTemplate: JmsTemplate

    val PREFIX_TOPIC_BOOKING_NAME = "booking.topic.device."
    val PREFIX_TOPIC_RETURN_NAME = "return.topic.device."

    fun findCirculationDevices(): List<Device> {
        return deviceRepository.findAll()
    }

    @Transactional
    fun borrowDevice(user: User, device: Device, toDate: Date? = null): BookingTransaction{
        val borrowDevice = deviceRepository.findById(device.id).get()
        if(borrowDevice.quantity > 0){
            borrowDevice.quantity--
            val txn = bookingTxnRepository.save(BookingTransaction(device = borrowDevice, borrower = user, fromDate = Date(), toDate = toDate))
            deviceRepository.save(borrowDevice)
            if(SecurityContextHolder.getContext().authentication?.isAuthenticated?:false)
                jmsTemplate.convertAndSend(
                    PREFIX_TOPIC_BOOKING_NAME + borrowDevice.id,
                    "Device model ${borrowDevice.model} has been booked by ${user.name}"
                )

            return txn
        } else {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Device not available", null)
        }
    }

    @Transactional
    fun returnDevice(user: User, device: Device): BookingTransaction{
        val borrowedDevice = deviceRepository.findById(device.id).get()
        try {
            val txn = bookingTxnRepository.findUnreturnedTxnsByBorrowerAndDevice(user, device)
            txn.returnedDate = Date()
            bookingTxnRepository.save(txn)
            borrowedDevice.quantity++
            deviceRepository.save(borrowedDevice)
            if(SecurityContextHolder.getContext().authentication?.isAuthenticated?:false)
                jmsTemplate.convertAndSend(
                    PREFIX_TOPIC_RETURN_NAME + borrowedDevice.id,
                    "Device model ${borrowedDevice.model} has been returned by ${user.name}"
                )
            return txn
        } catch (e: EmptyResultDataAccessException){
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Transaction not available", null)
        }
    }
}