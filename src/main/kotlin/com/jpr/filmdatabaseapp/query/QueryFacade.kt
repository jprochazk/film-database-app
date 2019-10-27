package com.jpr.filmdatabaseapp.query

import com.jpr.factory.OmdbFilmInfoQueryBuilder
import com.jpr.factory.OmdbFilmListQueryBuilder
import com.jpr.http.OmdbHttpClient
import com.jpr.model.OmdbFilm
import com.jpr.model.OmdbInfoResponse
import com.jpr.model.OmdbListResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class QueryFacade(
    val client: OmdbHttpClient = OmdbHttpClient.Builder().build()
) {

    @Value("\${app.omdbApiKey}")
    lateinit var apiKey: String

    fun getFilmList(title: String, type: String, page: Int): OmdbListResponse {
        val query = OmdbFilmListQueryBuilder(apiKey)
            .title(title)
            .filmType(OmdbFilm.Type.fromString(type))
            .page(page)
            .build()
        return client.execute(query) as OmdbListResponse
    }

    fun getFilmInfo(imdbID: String): OmdbInfoResponse {
        val query = OmdbFilmInfoQueryBuilder(apiKey)
            .imdbID(imdbID)
            .build()
        return client.execute(query) as OmdbInfoResponse
    }
}