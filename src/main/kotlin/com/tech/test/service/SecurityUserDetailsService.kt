package com.tech.test.service

import com.tech.test.respository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class SecurityUserDetailsService: UserDetailsService {
    @Autowired
    private lateinit var userRepo: UserRepository

    override fun loadUserByUsername(email: String?): UserDetails {
        return userRepo.findUserByEmail(email)
    }

}