package com.jpr.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.jpr.exception.OmdbNotJsonException
import org.apache.commons.codec.Charsets
import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.util.EntityUtils
import java.io.IOException
import java.net.URI
import java.nio.charset.StandardCharsets

data class OmdbInfoResponse(val film: OmdbFilm) : OmdbResponse {
    companion object {
        fun build(httpResponse: HttpResponse): OmdbInfoResponse {
            val resultDto = getJson(httpResponse.entity)

            val film = OmdbFilm(
                resultDto.Title, resultDto.Year, resultDto.imdbID, OmdbFilm.Type.fromString(resultDto.Type),
                URI(resultDto.Poster), resultDto.Genre, resultDto.Plot, resultDto.Director, resultDto.Writer,
                resultDto.Actors, resultDto.Production)
            return OmdbInfoResponse(film)
        }

        private fun getJson(entity: HttpEntity): OmdbInfoQueryResultJsonDto {
            val mapper = jacksonObjectMapper()
            val encoding = if (entity.contentEncoding == null) StandardCharsets.UTF_8 else Charsets.toCharset(entity.contentEncoding.value)
            val jsonString = EntityUtils.toString(entity, encoding)

            try {
                return mapper.readValue(jsonString)
            } catch(e: IOException) {
                throw OmdbNotJsonException(e)
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        private data class OmdbInfoRatingJsonDto(var Source: String, var Value: String)

        @JsonIgnoreProperties(ignoreUnknown = true)
        private data class OmdbInfoQueryResultJsonDto(
            var Title: String, var Year: String, var Rated: String, var Released: String, var Runtime: String, var Genre: String, var Director: String,
            var Writer: String, var Actors: String, var Plot: String, var Language: String, var Country: String, var Awards: String, var Poster: String,
            var Ratings: List<OmdbInfoRatingJsonDto>, var imdbRating: String, var imdbVotes: String, var imdbID: String, var Type: String, var DVD: String,
            var BoxOffice: String, var Production: String, var Website: String)
    }
}