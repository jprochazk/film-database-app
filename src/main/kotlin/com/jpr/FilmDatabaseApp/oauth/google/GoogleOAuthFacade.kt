package com.jpr.FilmDatabaseApp.oauth.google

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.jpr.FilmDatabaseApp.oauth.OAuthException
import com.jpr.FilmDatabaseApp.security.UserClientInfo
import com.jpr.FilmDatabaseApp.security.accesstoken.AccessToken
import com.jpr.FilmDatabaseApp.security.accesstoken.AccessTokenProvider
import com.jpr.FilmDatabaseApp.user.model.Role
import com.jpr.FilmDatabaseApp.user.model.User
import com.jpr.FilmDatabaseApp.user.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class GoogleOAuthFacade(private val userRepository: UserRepository) {
    @Value("\${app.google.oauth.clientId}")
    lateinit var clientId: String

    @Value("\${app.google.oauth.clientSecret}")
    lateinit var clientSecret: String

    @Autowired
    lateinit var tokenProvider: AccessTokenProvider

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Transactional
    fun authenticate(userClientInfo: UserClientInfo, authCode: String, type: String): AccessToken {
        val idToken = getIdToken(authCode, type)

        val email = idToken.payload.email
        val name = idToken.payload["name"].toString()
        val picture = idToken.payload["picture"].toString()
        val role = Role.User

        val user =
            userRepository.findByEmail(idToken.payload.email)
        ?:  userRepository.save(User(email, name, picture, role))
        ?:  throw OAuthException()

        return tokenProvider.generateToken(user, userClientInfo)
    }

    private fun getIdToken(authCode: String, type: String): GoogleIdToken {
        val transport = NetHttpTransport()
        val factory = JacksonFactory()
        val tokenRequest = GoogleAuthorizationCodeTokenRequest(
            transport, factory,
            clientId, clientSecret,
            authCode, type)
        val tokenResponse = tokenRequest.execute()

        val idToken = GoogleIdToken.parse(factory, tokenResponse.idToken)
        if(!GoogleIdTokenVerifier(transport, factory).verify(idToken))
            throw OAuthException()

        return idToken
    }
}
