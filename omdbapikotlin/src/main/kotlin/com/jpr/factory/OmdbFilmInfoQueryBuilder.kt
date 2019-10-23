package com.jpr.factory

import com.jpr.exception.OmdbQueryBuilderNullParamException
import com.jpr.model.OmdbQuery
import org.apache.http.client.utils.URIBuilder

class OmdbFilmInfoQueryBuilder(private val apiKey: String, baseURI: String = "http://www.omdbapi.com/") {
    private var imdbID: String? = null

    private val uriBuilder = URIBuilder(baseURI)

    fun build(): OmdbQuery {
        if(this.imdbID == null) throw OmdbQueryBuilderNullParamException("imdbID")

        return OmdbQuery(OmdbQuery.Type.INFO, uriBuilder.build())
    }

    fun imdbID(value: String): OmdbFilmInfoQueryBuilder {
        this.imdbID = value
        readyParameters()
        return this
    }

    private fun readyParameters() {
        uriBuilder.clearParameters()
        uriBuilder.addParameter("apikey", apiKey)
        uriBuilder.addParameter("r", "json")

        if(this.imdbID != null) uriBuilder.addParameter("i", this.imdbID)
    }
}