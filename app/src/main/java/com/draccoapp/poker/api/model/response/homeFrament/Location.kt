package com.draccoapp.poker.api.model.response.homeFrament


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Location(
    @Json(name = "city")
    val city: String,
    @Json(name = "lat")
    val lat: String?,
    @Json(name = "lng")
    val lng: String?,
    @Json(name = "state")
    val state: String
)