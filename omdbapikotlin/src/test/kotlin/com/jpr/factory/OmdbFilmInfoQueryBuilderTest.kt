package com.jpr.factory

import com.jpr.exception.OmdbQueryBuilderNullParamException
import com.jpr.model.OmdbFilm
import junit.framework.TestCase.assertTrue
import org.junit.Test

class OmdbFilmInfoQueryBuilderTest {

    @Test(expected = OmdbQueryBuilderNullParamException::class)
    fun `Id not set throws exception`() {
        val builder = OmdbFilmInfoQueryBuilder("APIKEY")

        builder.build()
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