package com.jpr.model

import java.net.URI
import java.time.Year

/**
 * Used to carry basic information about a film query entry outside of the module
 */
data class OmdbFilm(
    val title: String, val year: String, val imdbID: String, val type: Type, val posterURI: URI,    // base
    val genre: String? = null, val plot: String? = null, val director: String? = null,              // extension
    val writer: String? = null, val actors: String? = null, val production: String? = null          // extension
) {
    enum class Type {
        ANY, MOVIE, SERIES, EPISODE, UNKNOWN;

        /**
         * @return String the enum identifier in lower-case for usage in requests
         */
        fun getName(): String {
            return name.toLowerCase()
        }

        companion object {
            @JvmStatic
            fun fromString(name: String): Type {
                return when (name.toLowerCase()) {
                    "any" -> ANY
                    "movie" -> MOVIE
                    "series" -> SERIES
                    "episode" -> EPISODE
                    else -> UNKNOWN
                }
            }
        }
    }
}