package com.jpr.exception

import com.jpr.model.OmdbQuery

class OmdbBadRequestException : Exception("400: Bad request, please check query parameters")