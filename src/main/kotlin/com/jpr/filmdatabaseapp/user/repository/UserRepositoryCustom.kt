package com.jpr.filmdatabaseapp.user.repository

import com.jpr.filmdatabaseapp.user.model.User

interface UserRepositoryCustom {
    fun findByToken(token: String): User?
}