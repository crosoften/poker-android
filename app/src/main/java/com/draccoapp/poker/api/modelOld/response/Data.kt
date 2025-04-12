package com.draccoapp.poker.api.modelOld.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Data(
    @Json(name = "id")
    val id: Int,
    @Json(name = "state")
    val state: String,
    @Json(name = "user")
    val user: UserShort,
    @Json(name = "tournament")
    val tournament: Tournament
)