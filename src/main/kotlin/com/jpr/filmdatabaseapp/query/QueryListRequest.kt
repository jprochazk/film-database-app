package com.jpr.filmdatabaseapp.query

data class QueryListRequest(var title: String, var type: String = "any", var page: Int = 1)