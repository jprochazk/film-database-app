package com.jpr.factory

import com.jpr.exception.OmdbQueryBuilderNullParamException
import com.jpr.model.OmdbFilm
import junit.framework.TestCase.assertTrue
import org.junit.Test

class OmdbFilmListQueryBuilderTest {

    @Test
    fun `Title not set throws exception`() {
        val builder = OmdbFilmListQueryBuilder("APIKEY")

        try {
            builder.build()
        } catch(e: Exception) {
            assertTrue(e is OmdbQueryBuilderNullParamException)
            assertTrue(e.message == "Parameter title is required")
        }
    }

    @Test
    fun `Title set returns expected`() {
        val expected = "http://www.omdbapi.com/?apikey=APIKEY&r=json&s=TITLE"
        println("Expected: $expected")
        val actual = OmdbFilmListQueryBuilder("APIKEY").title("TITLE").build().requestURI.toString()
        println("Actual: $actual")

        assertTrue(expected == actual)
    }

    @Test
    fun `Title set twice returns expected`() {
        val expected = "http://www.omdbapi.com/?apikey=APIKEY&r=json&s=TITLE2"
        println("Expected: $expected")
        val actual = OmdbFilmListQueryBuilder("APIKEY")
            .title("TITLE1")
            .title("TITLE2")
        .build().requestURI.toString()
        println("Actual: $actual")

        assertTrue(expected == actual)
    }

    @Test
    fun `All params set returns expected`() {
        val expected = "http://www.omdbapi.com/?apikey=APIKEY&r=json&s=TITLE&y=YEAR&page=50&type=movie"
        println("Expected: $expected")
        val actual = OmdbFilmListQueryBuilder("APIKEY")
            .title("TITLE")
            .year("YEAR")
            .filmType(OmdbFilm.Type.MOVIE)
            .page(50)
        .build().requestURI.toString()
        println("Actual: $actual")

        assertTrue(expected == actual)
    }
}