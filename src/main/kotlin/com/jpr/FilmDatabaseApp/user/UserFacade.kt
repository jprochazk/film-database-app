package com.jpr.FilmDatabaseApp.user

import com.jpr.FilmDatabaseApp.security.accesstoken.AccessToken
import com.jpr.FilmDatabaseApp.security.accesstoken.repository.AccessTokenRepository
import com.jpr.FilmDatabaseApp.user.model.UserProfileDto
import com.jpr.FilmDatabaseApp.user.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class UserFacade {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var accessTokenRepository: AccessTokenRepository

    @Transactional
    fun getUser(id: Long): UserProfileDto? {
        return userRepository.findById(id).get().toDto()
    }

    @Transactional
    fun deleteToken(token: AccessToken) {
        accessTokenRepository.deleteById(token.token!!)
    }
}