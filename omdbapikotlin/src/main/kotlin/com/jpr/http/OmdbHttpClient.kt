package com.jpr.http

import com.jpr.exception.*
import com.jpr.model.*
import org.apache.http.HttpHost
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.impl.client.HttpClients
import java.io.Closeable
import java.net.URI

class OmdbHttpClient(private val client: CloseableHttpClient) : Closeable {

    class Builder() {
        private var proxy: HttpHost? = null

        fun proxy(proxy: HttpHost): Builder {
            this.proxy = proxy

            return this
        }

        fun build(): OmdbHttpClient {
            val httpClient =
                if(this.proxy != null) HttpClientBuilder.create().setProxy(this.proxy).build()
                else HttpClients.createDefault()

            return OmdbHttpClient(httpClient)
        }
    }

    fun execute(query: OmdbQuery): OmdbResponse {
        return internalHandleResponse(internalExecuteGetRequest(query.requestURI), query.type)
    }

    override fun close() {
        client.close()
    }

    private fun internalExecuteGetRequest(uri: URI): CloseableHttpResponse {
        return client.execute(HttpGet(uri))
    }

    private fun internalHandleResponse(httpResponse: CloseableHttpResponse, type: OmdbQuery.Type): OmdbResponse {
        httpResponse.use {
            val statusCode = httpResponse.statusLine.statusCode
            if(statusCode == 200) {
                return when(type) {
                    OmdbQuery.Type.LIST -> OmdbListResponse.build(it)
                    OmdbQuery.Type.INFO -> OmdbInfoResponse.build(it)
                }
            }

            when(statusCode) {
                400 -> throw OmdbBadRequestException()
                401 -> throw OmdbAccessDeniedException()
                403 -> throw OmdbForbiddenException()
                else -> throw OmdbUnknownErrorException(statusCode)
            }
        }
    }
}