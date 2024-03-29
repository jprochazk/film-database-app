package com.jpr.filmdatabaseapp.oauth

import com.jpr.filmdatabaseapp.oauth.google.GoogleOAuthFacade
import com.jpr.filmdatabaseapp.oauth.google.GoogleOAuthRequest
import com.jpr.filmdatabaseapp.security.UserClientInfo
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/oauth")
class OAuthController(private val googleOauthFacade: GoogleOAuthFacade) {

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