package com.jpr.factory

import junit.framework.TestCase.assertTrue
import org.junit.Test

class OmdbFilmInfoQueryBuilderTest {

    @Test
    fun `all params set returns expected`() {
        val expected = "http://www.omdbapi.com/?apikey=APIKEY&r=json&i=IMDB_ID"
        println("Expected: $expected")
        val actual = OmdbFilmInfoQueryBuilder("APIKEY").imdbID("IMDB_ID").build().requestURI.toString()
        println("Actual: $actual")

        assertTrue(expected == actual)
    }
}