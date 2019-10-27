package com.jpr.filmdatabaseapp.query

import com.jpr.flashbacktestutils.FlashbackBaseTest
import com.jpr.http.OmdbHttpClient
import com.linkedin.flashback.matchrules.MatchRuleUtils
import org.apache.http.HttpHost
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class QueryFacadeTest : FlashbackBaseTest() {

    lateinit var httpClient: OmdbHttpClient

    lateinit var queryFacade: QueryFacade

    @Before
    fun init() {
        initializeFlashback()

        httpClient = OmdbHttpClient.Builder().proxy(HttpHost(Flashback.proxyHost, Flashback.proxyPort)).build()
        queryFacade = QueryFacade(httpClient)
        queryFacade.apiKey = "API_KEY"
    }

    @After
    fun cleanup() {
        cleanupFlashback()
    }

    @Test
    fun `film list query returns expected`() {
        val scene = createFlashbackScene("OMDB_API_FILM_LIST")
        Flashback.setScene(scene)
        Flashback.setMatchRule(MatchRuleUtils.matchUriWithQueryBlacklist(setOf("apikey")))


        Flashback.startScene()

        val response = queryFacade.getFilmList("inception", "any", 1)
        Assert.assertEquals(response.films.size, 10)

        Flashback.stopScene()
    }

    @Test
    fun `film info query returns expected`() {
        val scene = createFlashbackScene("OMDB_API_FILM_INFO")
        Flashback.setScene(scene)
        Flashback.setMatchRule(MatchRuleUtils.matchUriWithQueryBlacklist(setOf("apikey")))

        Flashback.startScene()

        val response = queryFacade.getFilmInfo("tt1375666")
        Assert.assertEquals(response.film.title, "Inception")

        Flashback.stopScene()
    }

}