package com.tech.test.service

import com.tech.test.model.User
import com.tech.test.respository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService {
    @Autowired
    private lateinit var userRepo: UserRepository

    fun getAllUsersWithPagination(): List<User> {
        return userRepo.findAll().toList()
    }
}