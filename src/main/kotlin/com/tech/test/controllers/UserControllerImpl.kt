package com.tech.test.controllers

import com.agnugroh.phonebooking.api.UsersApiDelegate
import com.agnugroh.phonebooking.model.User
import com.tech.test.service.UserService
import org.modelmapper.ModelMapper
import org.modelmapper.TypeToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class UserControllerImpl: UsersApiDelegate{
    @Autowired
    private lateinit var userService: UserService
    @Autowired
    private lateinit var modelMapper: ModelMapper

    override fun getUsers(): ResponseEntity<List<User>> {
        val list = userService.getAllUsersWithPagination()
        return ResponseEntity.ok(modelMapper.map(list, object : TypeToken<List<User>>() {}.type))
    }
}