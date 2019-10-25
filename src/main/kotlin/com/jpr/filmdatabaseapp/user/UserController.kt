package com.jpr.filmdatabaseapp.user

import com.jpr.filmdatabaseapp.security.accesstoken.AccessToken
import com.jpr.filmdatabaseapp.user.dto.SignoutResponse
import com.jpr.filmdatabaseapp.user.model.User
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class UserController(
    private val userFacade: UserFacade
) {

    @GetMapping("/profile")
    @PreAuthorize("hasRole('USER')")
    fun getUserProfile(
        accessToken: AccessToken
    ): ResponseEntity<User.Dto> {
        return ResponseEntity.ok(
            accessToken.user!!.toDto()
        )
    }

    @PostMapping("/signout")
    @PreAuthorize("hasRole('USER')")
    fun signOut(
        accessToken: AccessToken
    ): ResponseEntity<SignoutResponse> {
        userFacade.deleteToken(accessToken)
        return ResponseEntity.ok(SignoutResponse())
    }
}