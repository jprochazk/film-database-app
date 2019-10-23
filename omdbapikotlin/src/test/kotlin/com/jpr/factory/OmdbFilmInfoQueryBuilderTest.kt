package com.jpr.factory

import com.jpr.exception.OmdbQueryBuilderNullParamException
import com.jpr.model.OmdbFilm
import junit.framework.TestCase.assertTrue
import org.junit.Test

class OmdbFilmInfoQueryBuilderTest {

    // @Test(expected = OmdbQueryBuilderNullParamException)
    // ^ Currently not possible. See https://youtrack.jetbrains.com/issue/KT-16304
    @Test
    fun `Id not set throws exception`() {
        val builder = OmdbFilmInfoQueryBuilder("APIKEY")

        try {
            builder.build()
        } catch(expected: Exception) {
            println(expected.toString())
            val actual = OmdbQueryBuilderNullParamException("imdbID")
            println(actual.toString())

            assertTrue(expected is OmdbQueryBuilderNullParamException)
            assertTrue(expected.message == "Parameter imdbID is required")
        }
    }

    @Test
    fun `Params set twice returns expected`() {
        val expected = "http://www.omdbapi.com/?apikey=APIKEY&r=json&i=IMDBID2"
        println("Expected: $expected")

        val actual = OmdbFilmInfoQueryBuilder("APIKEY")
            .imdbID("IMDBID1")
            .imdbID("IMDBID2")
        .build().requestURI.toString()
        println("Actual: $actual")

        assertTrue(expected == actual)
    }

    @Test
    fun `All params set returns expected`() {
        val expected = "http://www.omdbapi.com/?apikey=APIKEY&r=json&i=IMDBID"
        println("Expected: $expected")

        val actual = OmdbFilmInfoQueryBuilder("APIKEY")
            .imdbID("IMDBID")
        .build().requestURI.toString()
        println("Actual: $actual")

        assertTrue(expected == actual)
    }
}