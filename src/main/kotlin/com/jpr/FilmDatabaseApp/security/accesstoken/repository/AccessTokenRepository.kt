package com.jpr.FilmDatabaseApp.security.accesstoken.repository

import com.jpr.FilmDatabaseApp.security.accesstoken.AccessToken
import org.springframework.data.jpa.repository.JpaRepository

interface AccessTokenRepository : JpaRepository<AccessToken, String>, AccessTokenRepositoryCustom {
    fun findByToken(token: String): AccessToken?
}