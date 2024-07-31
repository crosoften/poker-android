package com.draccoapp.poker.api.model.response.homeFrament


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HomeFragmentResponse(
    @Json(name = "myself")
    val myself: Myself,
    @Json(name = "nextTournaments")
    val nextTournaments: List<NextTournament>,
    @Json(name = "tournamentsImIn")
    val tournamentsImIn: List<TournamentsImIn>
)