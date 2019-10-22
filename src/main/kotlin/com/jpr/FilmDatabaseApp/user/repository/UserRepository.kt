package com.jpr.FilmDatabaseApp.user.repository

import com.jpr.FilmDatabaseApp.user.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long>, UserRepositoryCustom {
    fun findByEmail(email: String): User?
}