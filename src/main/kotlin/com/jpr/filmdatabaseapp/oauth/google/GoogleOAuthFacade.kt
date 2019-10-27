package com.jpr.filmdatabaseapp.oauth.google

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.jpr.filmdatabaseapp.oauth.OAuthException
import com.jpr.filmdatabaseapp.security.UserClientInfo
import com.jpr.filmdatabaseapp.security.accesstoken.AccessToken
import com.jpr.filmdatabaseapp.security.accesstoken.AccessTokenProvider
import com.jpr.filmdatabaseapp.user.model.Role
import com.jpr.filmdatabaseapp.user.model.User
import com.jpr.filmdatabaseapp.user.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class GoogleOAuthFacade(
    private val userRepository: UserRepository,
    private val netHttpTransport: NetHttpTransport = NetHttpTransport(),
    private val jacksonFactory: JacksonFactory = JacksonFactory()
) {

    @Value("\${app.google.oauth.clientId}")
    lateinit var clientId: String

    @Value("\${app.google.oauth.clientSecret}")
    lateinit var clientSecret: String

    @Autowired
    lateinit var tokenProvider: AccessTokenProvider

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
        val tokenRequest = GoogleAuthorizationCodeTokenRequest(
            netHttpTransport, jacksonFactory,
            clientId, clientSecret,
            authCode, type)
        val tokenResponse = tokenRequest.execute()

        val idToken = GoogleIdToken.parse(jacksonFactory, tokenResponse.idToken)
        if(!GoogleIdTokenVerifier(netHttpTransport, jacksonFactory).verify(idToken))
            throw OAuthException()

        return idToken
    }
}
