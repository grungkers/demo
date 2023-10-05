package com.tech.test.controllers

import com.agnugroh.phonebooking.model.BookingTransaction
import com.tech.test.model.Device
import com.tech.test.model.User
import com.tech.test.model.WatchDevice
import com.tech.test.respository.PhoneDeviceRepository
import com.tech.test.respository.WatchDeviceRepository
import io.swagger.v3.oas.annotations.Hidden
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.openapitools.HomeController
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@Controller
class AppController {
    @Autowired
    private lateinit var watchDeviceRepository: WatchDeviceRepository
    @Autowired
    private lateinit var phoneDeviceRepository: PhoneDeviceRepository

    @Hidden
    @GetMapping(path = ["/home"])
    fun getHome(model: Model): String {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        val userDetails = authentication.principal as User
        model.addAttribute("name", userDetails.name)
        var watchDevices: List<WatchDevice>?
        var list: List<String> = mutableListOf()
        if(userDetails.role == User.Roles.ADMIN) {
            phoneDeviceRepository.findAll().forEach {
                list.addLast("'/booking/topic/device/${it.id}'")
                list.addLast("'/return/topic/device/${it.id}'")
            }
        } else {
            watchDevices = watchDeviceRepository.findByWatcher(userDetails)
            list = watchDevices.map { "'/return/topic/device/${it.device.id}'" }
        }
        model.addAttribute("destinations", list)
        return "app"
    }

    @Operation(
        summary = "",
        operationId = "bookPhone",
        description = """""",
        responses = [
            ApiResponse(responseCode = "200", description = "Returning transaction detail of phone booking", content = [Content(schema = Schema(implementation = BookingTransaction::class))])
        ]
    )
    @RequestMapping(
        method = [RequestMethod.POST],
        value = ["/api/phones/{phoneId}/watch"],
        produces = ["plain/text"]
    )
    fun watchPhone(@Parameter(description = "phone device id", required = true) @PathVariable("phoneId") phoneId: kotlin.Int): ResponseEntity<String> {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        val userDetails = authentication.principal as User
        watchDeviceRepository.save(WatchDevice(watcher = userDetails, device = Device(id = phoneId)))
        return ResponseEntity.ok("Successfully watched")
    }

    @RequestMapping("/")
    fun index(): String = "redirect:/home"
}