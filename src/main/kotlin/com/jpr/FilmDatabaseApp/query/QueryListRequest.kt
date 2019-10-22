package com.jpr.FilmDatabaseApp.query

data class QueryListRequest(var title: String, var type: String = "any", var page: Int = 1)