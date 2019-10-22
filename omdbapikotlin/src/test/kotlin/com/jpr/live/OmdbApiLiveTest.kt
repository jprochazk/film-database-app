package com.jpr.live

import com.jpr.factory.OmdbFilmListQueryBuilder
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.junit.Before
import org.junit.Test

// TODO: Write live tests
class OmdbApiLiveTest {

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
    fun `Params set returns expected`() {
    }
}