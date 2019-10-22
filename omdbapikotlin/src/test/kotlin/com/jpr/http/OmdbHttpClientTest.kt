package com.jpr.http

import com.jpr.exception.OmdbAccessDeniedException
import com.jpr.exception.OmdbNotJsonException
import com.jpr.factory.OmdbFilmInfoQueryBuilder
import com.jpr.model.OmdbQuery
import junit.framework.TestCase.assertTrue
import org.apache.commons.codec.Charsets
import org.apache.http.HttpEntity
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.utils.URIBuilder
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import org.junit.Before
import org.junit.Test
import java.net.URI
import java.nio.charset.StandardCharsets

class OmdbHttpClientTest {

    @Before
    fun isReachable() {
        HttpClients.createDefault().use {
            val response = it.execute(HttpGet("http://www.omdbapi.com/"))
            response.use {
                if(response.statusLine.statusCode != 200) throw RuntimeException("OMDB Api server is not reachable!")
            }
        }
    }

    @Test
    fun `No api key throws exception`() {
        val query = OmdbQuery(OmdbQuery.Type.LIST, URI("http://www.omdbapi.com/?r=json&title=inception"))
        try {
            query.send()
        } catch(e: Exception) {
            assertTrue(e is OmdbAccessDeniedException)
            assertTrue(e.message == "401: Access denied, API key is missing or invalid")
        }
    }

    @Test
    fun `No URI parameters throws exception`() {
        val query = OmdbQuery(OmdbQuery.Type.LIST, URIBuilder("http://www.omdbapi.com/").build())
        try {
            query.send()
        } catch(e: Exception) {
            assertTrue(e is OmdbNotJsonException)
            assertTrue(e.message == "Response from OMDB Api contains no JSON, please check query URI parameters")
        }
    }
}