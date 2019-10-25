package com.jpr.filmdatabaseapp.query

import com.jpr.flashbacktestutils.FlashbackBaseTest
import com.jpr.factory.OmdbFilmListQueryBuilder
import com.jpr.http.OmdbHttpClient
import com.jpr.model.OmdbFilm
import com.jpr.model.OmdbListResponse
import com.linkedin.flashback.matchrules.MatchRuleUtils
import org.junit.After
import org.junit.Before
import org.junit.Test

class QueryControllerApiTest : FlashbackBaseTest() {

    @Before
    fun init() {
        initializeFlashback()
    }

    @After
    fun cleanup() {
        cleanupFlashback()
    }

    @Test
    fun `test flashback working`() {
        val scene = createFlashbackScene("OmdbApi_FilmList_TitleInception")
        Flashback.setScene(scene)
        Flashback.setMatchRule(MatchRuleUtils.matchEntireRequest())
        Flashback.startScene()

        val query = OmdbFilmListQueryBuilder("c512d55e")
            .title("inception")
            .filmType(OmdbFilm.Type.fromString("any"))
            .page(1)
            .build()

        val httpClient = OmdbHttpClient.Builder().proxy(Flashback.proxy).build()
        httpClient.use {
            client ->

            val response = client.execute(query) as OmdbListResponse
            println(response.films.forEach { film -> println("Title: ${film.title}") })
        }

        Flashback.stopScene()
    }

}