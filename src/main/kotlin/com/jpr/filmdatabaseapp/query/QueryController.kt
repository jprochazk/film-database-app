package com.jpr.filmdatabaseapp.query

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/query")
class QueryController(private val queryFacade: QueryFacade) {

    @PostMapping("/list")
    fun list(@RequestBody request: QueryListRequest): ResponseEntity<QueryListResponse> {
        val result = queryFacade.getFilmList(request.title, request.type, request.page)
        return ResponseEntity.ok(QueryListResponse(result.films))
    }

    @PostMapping("/info")
    fun info(@RequestBody request: QueryInfoRequest): ResponseEntity<QueryInfoResponse> {
        val result = queryFacade.getFilmInfo(request.imdbID)
        return ResponseEntity.ok(QueryInfoResponse(result.film))
    }
}