package com.jpr.filmdatabaseapp.user.repository

import com.jpr.filmdatabaseapp.user.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long>, UserRepositoryCustom {
    fun findByEmail(email: String): User?
    fun existsByEmail(email: String): Boolean
}