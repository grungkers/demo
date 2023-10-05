package com.tech.test.model

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
//import org.springframework.security.core.GrantedAuthority
//import org.springframework.security.core.authority.SimpleGrantedAuthority
//import org.springframework.security.core.userdetails.UserDetails
import java.util.*

@Entity
data class User(
    @Column
    val name: String?= null,

    @Column
    val role: Roles?= null,

    @Column
    val email: String?= null,

    @Column
    val pwd: String?= null,
): UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null

    enum class Roles {
        ADMIN, TESTER
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return Collections.singleton(SimpleGrantedAuthority(role?.name))
    }

    override fun getPassword(): String? {
        return pwd
    }

    override fun getUsername(): String? {
        return email
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}