package com.tech.test.controllers

import com.agnugroh.phonebooking.api.PhonesApiDelegate
import com.agnugroh.phonebooking.model.BookingTransaction
import com.agnugroh.phonebooking.model.PhoneDevice
import com.tech.test.model.Device
import com.tech.test.model.User
import com.tech.test.respository.PhoneDeviceRepository
import com.tech.test.service.BookingService
import org.modelmapper.ModelMapper
import org.modelmapper.TypeToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component


@Component
class PhoneDeviceControllerImpl: PhonesApiDelegate {
    @Autowired
    private lateinit var phoneDeviceRepository: PhoneDeviceRepository
    @Autowired
    private lateinit var bookingService: BookingService
    @Autowired
    private lateinit var modelMapper: ModelMapper

    override fun getPhones(): ResponseEntity<List<PhoneDevice>> {
        val deviceList = phoneDeviceRepository.findAll()
        val mappedList: List<PhoneDevice> = modelMapper.map(deviceList, object : TypeToken<List<PhoneDevice>>() {}.type)
        return ResponseEntity.ok(mappedList)
    }

    override fun bookPhone(phoneId: Int): ResponseEntity<BookingTransaction> {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        val userDetails = authentication.principal as User
        val txn = bookingService.borrowDevice(userDetails, Device(phoneId))
        val res:BookingTransaction = modelMapper.map(txn, BookingTransaction::class.java)
        return ResponseEntity.ok(res)
    }

    override fun returnPhone(phoneId: Int): ResponseEntity<BookingTransaction> {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        val userDetails = authentication.principal as User
        val txn = bookingService.returnDevice(userDetails, Device(phoneId))
        val res:BookingTransaction = modelMapper.map(txn, BookingTransaction::class.java)
        return ResponseEntity.ok(res)
    }
}