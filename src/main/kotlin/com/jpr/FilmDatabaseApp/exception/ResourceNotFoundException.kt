package com.jpr.FilmDatabaseApp.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class ResourceNotFoundException(resourceName: String) : Exception("$resourceName not found")