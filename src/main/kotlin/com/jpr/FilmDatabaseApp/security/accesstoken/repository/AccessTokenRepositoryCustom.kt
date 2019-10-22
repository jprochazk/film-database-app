package com.jpr.FilmDatabaseApp.security.accesstoken.repository

import com.jpr.FilmDatabaseApp.security.accesstoken.AccessToken

interface AccessTokenRepositoryCustom {
    fun findByTokenWithUser(token: String): AccessToken?
}