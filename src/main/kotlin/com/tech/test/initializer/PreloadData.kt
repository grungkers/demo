package com.tech.test.initializer

import com.tech.test.model.Device
import com.tech.test.model.User
import com.tech.test.respository.PhoneDeviceRepository
import com.tech.test.respository.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PreloadData {
    @Bean
    fun init(userRepo: UserRepository, phoneDeviceRepository: PhoneDeviceRepository) = CommandLineRunner {
        userRepo.save(User("John", User.Roles.TESTER, "jhon@email.com", "123"))
        userRepo.save(User("Agung", User.Roles.ADMIN, "admin", "admin"))
        userRepo.save(User("Dicky", User.Roles.TESTER, "dicky@email.com", "123"))
        phoneDeviceRepository.save(Device(model = "Samsung Galaxy S9"))
        phoneDeviceRepository.save(Device(model = "Samsung Galaxy S8", quantity = 2))
        phoneDeviceRepository.save(Device(model = "Motorola Nexus 6"))
        phoneDeviceRepository.save(Device(model = "Oneplus 9"))
        phoneDeviceRepository.save(Device(model = "Apple iPhone 13"))
        phoneDeviceRepository.save(Device(model = "Apple iPhone 12"))
        phoneDeviceRepository.save(Device(model = "Apple iPhone 11"))
        phoneDeviceRepository.save(Device(model = "iPhone X"))
        phoneDeviceRepository.save(Device(model = "Nokia 3310"))
    }
}