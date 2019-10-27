package com.jpr.filmdatabaseapp.user

import com.jpr.filmdatabaseapp.exception.ExceptionResponse
import com.jpr.filmdatabaseapp.exception.UnauthorizedException
import com.jpr.filmdatabaseapp.security.accesstoken.AccessToken
import com.jpr.filmdatabaseapp.user.dto.SignoutResponse
import com.jpr.filmdatabaseapp.user.model.User
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.context.request.WebRequest
import java.lang.Exception

@ControllerAdvice
class UserControllerAdvice {

    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorizedException(e: Exception, request: WebRequest): ResponseEntity<ExceptionResponse> {
        e as UnauthorizedException

        return ResponseEntity
            .status(e.status)
            .body(ExceptionResponse(e.message!!))
    }
}

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