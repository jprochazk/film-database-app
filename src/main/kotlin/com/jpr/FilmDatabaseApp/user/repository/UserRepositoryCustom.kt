package com.jpr.FilmDatabaseApp.user.repository

import com.jpr.FilmDatabaseApp.user.model.User

interface UserRepositoryCustom {
    fun findByToken(token: String): User?
}