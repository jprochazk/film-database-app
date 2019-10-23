package com.jpr.filmdatabaseapp.security.accesstoken.repository

import com.jpr.filmdatabaseapp.security.accesstoken.AccessToken
import org.springframework.data.jpa.repository.JpaRepository

interface AccessTokenRepository : JpaRepository<AccessToken, String>, AccessTokenRepositoryCustom {
    fun findByToken(token: String): AccessToken?
}