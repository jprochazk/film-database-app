package com.jpr.filmdatabaseapp.security.accesstoken.repository

import com.jpr.filmdatabaseapp.security.accesstoken.AccessToken

interface AccessTokenRepositoryCustom {
    fun findByTokenWithUser(token: String): AccessToken?
}