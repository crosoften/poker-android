package com.draccoapp.poker.api.model.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Location(
    @Json(name = "city")
    val city: String?,
    @Json(name = "lat")
    val lat: Any?,
    @Json(name = "lng")
    val lng: Any?,
    @Json(name = "state")
    val state: String?
)