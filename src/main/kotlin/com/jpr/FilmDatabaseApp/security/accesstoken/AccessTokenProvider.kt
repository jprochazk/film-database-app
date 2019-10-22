package com.jpr.FilmDatabaseApp.security.accesstoken

import com.jpr.FilmDatabaseApp.security.UserClientInfo
import com.jpr.FilmDatabaseApp.security.accesstoken.repository.AccessTokenRepository
import com.jpr.FilmDatabaseApp.user.model.User
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*

@Component
class AccessTokenProvider(
    private val accessTokenRepository: AccessTokenRepository
) {
    private val log = LoggerFactory.getLogger(this.javaClass)

    fun generateToken(user: User, userClientInfo: UserClientInfo): AccessToken {
        return accessTokenRepository.save(AccessToken(UUID.randomUUID().toString(), user, userClientInfo))
    }

    fun validateToken(token: String, userClientInfo: UserClientInfo): AccessToken? {
        val savedToken = accessTokenRepository.findByToken(token) ?: return null // if token not found return null
        if(userClientInfo !== savedToken.userClientInfo) return null

        return savedToken
    }
}