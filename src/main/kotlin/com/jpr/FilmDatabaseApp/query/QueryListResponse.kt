package com.jpr.FilmDatabaseApp.query

import com.jpr.model.OmdbFilm

data class QueryListResponse(val movies: ArrayList<OmdbFilm>)