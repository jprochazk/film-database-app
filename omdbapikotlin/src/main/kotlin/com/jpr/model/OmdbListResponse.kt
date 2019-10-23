package com.jpr.model

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.jpr.exception.OmdbNotJsonException
import org.apache.commons.codec.Charsets
import org.apache.http.HttpEntity
import java.net.URI
import org.apache.http.HttpResponse
import org.apache.http.util.EntityUtils
import java.io.IOException
import java.nio.charset.StandardCharsets

data class OmdbListResponse(val films: ArrayList<OmdbFilm>) : OmdbResponse {

    companion object {
        fun build(httpResponse: HttpResponse): OmdbListResponse {
            val resultDto = getJson(httpResponse.entity)

            val films: ArrayList<OmdbFilm> = ArrayList()
            resultDto.Search?.forEach {
                    result -> films.add(OmdbFilm(result.Title, result.Year, result.imdbID, OmdbFilm.Type.fromString(result.Type), URI(result.Poster)))
            }
            return OmdbListResponse(films)
        }

        private fun getJson(entity: HttpEntity): OmdbListQueryResultJsonDto {
            val mapper = jacksonObjectMapper()
            val encoding = if (entity.contentEncoding == null) StandardCharsets.UTF_8 else Charsets.toCharset(entity.contentEncoding.value)
            val jsonString = EntityUtils.toString(entity, encoding)

            try {
                return mapper.readValue(jsonString)
            } catch(e: IOException) {
                throw OmdbNotJsonException(e)
            }
        }

        private data class OmdbFilmJsonDto(var Title: String, var Year: String, var imdbID: String, var Type: String, var Poster: String) : OmdbJson
        private data class OmdbListQueryResultJsonDto(var Search: List<OmdbFilmJsonDto>?, var totalResults: Int?, var Response: Boolean?, var Error: String?) : OmdbJson
    }
}