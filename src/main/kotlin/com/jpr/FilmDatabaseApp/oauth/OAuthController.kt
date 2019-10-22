package com.jpr.FilmDatabaseApp.oauth

import com.jpr.FilmDatabaseApp.oauth.google.GoogleOAuthFacade
import com.jpr.FilmDatabaseApp.oauth.google.GoogleOAuthRequest
import com.jpr.FilmDatabaseApp.security.UserClientInfo
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/oauth")
class OAuthController(private val googleOauthFacade: GoogleOAuthFacade) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @PostMapping("/google")
    fun google(
        @RequestBody OAuthRequest: GoogleOAuthRequest,
        userClientInfo: UserClientInfo
    ): ResponseEntity<OAuthResponse> {
        val accessToken = googleOauthFacade.authenticate(userClientInfo, OAuthRequest.authCode, OAuthRequest.type)

        return ResponseEntity.ok(
            OAuthResponse(accessToken.token!!)
        )
    }
}