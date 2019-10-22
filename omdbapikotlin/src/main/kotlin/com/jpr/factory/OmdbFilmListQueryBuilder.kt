package com.jpr.factory

import com.jpr.exception.OmdbQueryBuilderNullParamException
import com.jpr.model.OmdbFilm
import com.jpr.model.OmdbQuery
import org.apache.http.client.utils.URIBuilder


class OmdbFilmListQueryBuilder(private val apiKey: String, baseURI: String = "http://www.omdbapi.com/") {
    private var title: String? = null
    private var year: String? = null
    private var page: Int? = null
    private var type: OmdbFilm.Type? = null

    private val uriBuilder = URIBuilder(baseURI)

    fun build(): OmdbQuery {
        if(this.title == null) throw OmdbQueryBuilderNullParamException("title")

        return OmdbQuery(OmdbQuery.Type.LIST, uriBuilder.build())
    }

    fun title(value: String): OmdbFilmListQueryBuilder {
        this.title = value
        readyParameters()
        return this
    }

    fun year(value: String): OmdbFilmListQueryBuilder {
        this.year = value
        readyParameters()
        return this
    }

    fun page(value: Int): OmdbFilmListQueryBuilder {
        this.page = value
        readyParameters()
        return this
    }

    fun filmType(value: OmdbFilm.Type): OmdbFilmListQueryBuilder {
        if(value == OmdbFilm.Type.ANY) return this
        this.type = value
        readyParameters()
        return this
    }

    private fun readyParameters() {
        uriBuilder.clearParameters()
        uriBuilder.addParameter("apikey", apiKey)
        uriBuilder.addParameter("r", "json")

        if(this.title != null) uriBuilder.addParameter("s", this.title)
        if(this.year != null) uriBuilder.addParameter("y", this.year)
        if(this.page != null) uriBuilder.addParameter("page", this.page.toString())
        if(this.type != null) uriBuilder.addParameter("type", this.type.toString().toLowerCase())
    }
}