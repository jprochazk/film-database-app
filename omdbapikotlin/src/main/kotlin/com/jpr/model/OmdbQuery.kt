package com.jpr.model

import com.jpr.http.OmdbHttpClient
import java.net.URI

data class OmdbQuery(val type: Type, val requestURI: URI) {
    enum class Type {
        LIST, INFO;
    }
    fun send(): OmdbResponse {
        OmdbHttpClient().use {
            return it.execute(this)
        }
    }
}