package com.tech.test.initializer

import com.tech.test.service.SecurityUserDetailsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import java.util.*


@Component
class AuthProvider: AuthenticationProvider{
    @Autowired
    private lateinit var securityUserDetailsService: SecurityUserDetailsService

    override fun authenticate(authentication: Authentication): Authentication? {
        val name = authentication.name
        val password = authentication.credentials.toString()
        val userDetails = securityUserDetailsService.loadUserByUsername(name)
        if(userDetails.password.equals(password))
            return UsernamePasswordAuthenticationToken(
                userDetails, password,
                Collections.singleton(SimpleGrantedAuthority("ROLE_" + userDetails.authorities.first().authority.toString()))
            )
        throw UsernameNotFoundException("Wrong password")
    }

    override fun supports(authentication: Class<*>?): Boolean {
        return true
    }

}