package com.jpr.exception

class OmdbUnknownErrorException(statusCode: Int) : Exception("Unrecognized error: $statusCode")