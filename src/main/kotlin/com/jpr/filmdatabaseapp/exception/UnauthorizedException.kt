package com.jpr.filmdatabaseapp.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
class UnauthorizedException(reason: String? = null) : Exception(reason ?: "Unauthorized") {
    val status: Int = 401
}