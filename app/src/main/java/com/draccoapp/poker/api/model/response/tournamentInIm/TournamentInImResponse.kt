package com.draccoapp.poker.api.model.response.tournamentInIm


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TournamentInImResponse(
    @Json(name = "data")
    val `data`: List<TournamentInImData>?,
    @Json(name = "currentPage")
    val currentPage: Int?,
    @Json(name = "itemsPerPage")
    val itemsPerPage: Int?,
    @Json(name = "totalItems")
    val totalItems: Int?,
    @Json(name = "totalPages")
    val totalPages: Int?
)