package com.tech.test

import org.openapitools.HomeController
import org.openapitools.SpringDocConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType


@SpringBootApplication
@ComponentScan(basePackages = ["com.tech.test", "com.agnugroh.phonebooking.api", "com.agnugroh.phonebooking.model"], excludeFilters = [ComponentScan.Filter(type= FilterType.ASSIGNABLE_TYPE, classes =[HomeController::class])])
@ConfigurationPropertiesScan
class PhoneBookingApplication

fun main(args: Array<String>) {
	runApplication<PhoneBookingApplication>(*args)
}
