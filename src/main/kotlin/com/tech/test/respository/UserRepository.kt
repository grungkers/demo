package com.tech.test.respository

import com.tech.test.model.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository: CrudRepository<User, Int> {
    fun findUserByEmail(email: String?): User
}