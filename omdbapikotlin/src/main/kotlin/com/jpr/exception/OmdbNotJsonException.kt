package com.jpr.exception

class OmdbNotJsonException(cause: Throwable) : Exception("Response from OMDB Api contains no JSON, please check query URI parameters", cause)