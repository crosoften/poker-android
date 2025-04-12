package com.draccoapp.poker.api.model.request


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocationX(
    @Json(name = "city")
    val city: String,
    @Json(name = "state")
    val state: String
)