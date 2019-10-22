package com.jpr.http

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.jpr.exception.*
import com.jpr.model.*
import org.apache.commons.codec.Charsets
import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import java.io.Closeable
import java.io.IOException
import java.net.URI
import java.nio.charset.StandardCharsets

class OmdbHttpClient : Closeable {
    private val client: CloseableHttpClient = HttpClients.createDefault()
    private val mapper = jacksonObjectMapper()

    fun execute(query: OmdbQuery): OmdbResponse {
        return internalHandleResponse(internalExecuteUriRequest(query.requestURI), query.type)
    }

    override fun close() {
        client.close()
    }

    private fun internalExecuteUriRequest(uri: URI): CloseableHttpResponse {
        return client.execute(HttpGet(uri))
    }

    private fun internalHandleResponse(httpResponse: CloseableHttpResponse, type: OmdbQuery.Type): OmdbResponse {
        httpResponse.use {
            when(httpResponse.statusLine.statusCode) {
                200 -> return when(type) {
                        OmdbQuery.Type.LIST -> buildOmdbListResponse(httpResponse)
                        OmdbQuery.Type.INFO -> buildOmdbInfoResponse(httpResponse)
                    }
                400 -> throw OmdbBadRequestException()
                401 -> throw OmdbAccessDeniedException()
                403 -> throw OmdbForbiddenException()
                else -> throw OmdbUnknownErrorException(httpResponse.statusLine.statusCode)
            }
        }
    }

    private fun buildOmdbInfoResponse(httpResponse: HttpResponse): OmdbInfoResponse {
        val resultDto = getJsonFrom<OmdbInfoQueryResultJsonDto>(httpResponse).result
        val film = OmdbFilm(
            resultDto.Title, resultDto.Year, resultDto.imdbID, OmdbFilm.Type.fromString(resultDto.Type),
            URI(resultDto.Poster), resultDto.Genre, resultDto.Plot, resultDto.Director, resultDto.Writer,
            resultDto.Actors, resultDto.Production)
        return OmdbInfoResponse(film)
    }

    private fun buildOmdbListResponse(httpResponse: HttpResponse): OmdbListResponse {
        val resultDto = getJsonFrom<OmdbListQueryResultJsonDto>(httpResponse).result

        val films: ArrayList<OmdbFilm> = ArrayList()
        resultDto.Search?.forEach {
                result -> films.add(OmdbFilm(result.Title, result.Year, result.imdbID, OmdbFilm.Type.fromString(result.Type), URI(result.Poster)))
        }
        return OmdbListResponse(films)
    }

    private inline fun <reified T : OmdbJson> getJsonFrom(response: HttpResponse): OmdbJsonResult<T> {
        val entity: HttpEntity = response.entity
        val encoding = if (entity.contentEncoding == null) StandardCharsets.UTF_8 else Charsets.toCharset(entity.contentEncoding.value)
        val jsonString = EntityUtils.toString(entity, encoding)

        return try {
            val result: T = mapper.readValue(jsonString)
            OmdbJsonResult(result)
        } catch(e: IOException) {
            throw OmdbNotJsonException(e)
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private interface OmdbJson

    private data class OmdbJsonResult<T : OmdbJson>(val result: T)

    private data class OmdbFilmJsonDto(var Title: String, var Year: String, var imdbID: String, var Type: String, var Poster: String) : OmdbJson

    private data class OmdbListQueryResultJsonDto(var Search: List<OmdbFilmJsonDto>?, var totalResults: Int?, var Response: Boolean?, var Error: String?) : OmdbJson

    private data class OmdbInfoRatingJsonDto(var Source: String, var Value: String) : OmdbJson

    private data class OmdbInfoQueryResultJsonDto(
        var Title: String, var Year: String, var Rated: String, var Released: String, var Runtime: String, var Genre: String, var Director: String,
        var Writer: String, var Actors: String, var Plot: String, var Language: String, var Country: String, var Awards: String, var Poster: String,
        var Ratings: List<OmdbInfoRatingJsonDto>, var imdbRating: String, var imdbVotes: String, var imdbID: String, var Type: String, var DVD: String,
        var BoxOffice: String, var Production: String, var Website: String) : OmdbJson
}