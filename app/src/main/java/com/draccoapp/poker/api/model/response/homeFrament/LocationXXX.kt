package com.draccoapp.poker.api.model.response.homeFrament


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocationXXX(
    @Json(name = "city")
    val city: String,
    @Json(name = "country")
    val country: String,
    @Json(name = "lat")
    val lat: Any?,
    @Json(name = "lng")
    val lng: Any?,
    @Json(name = "neighborhood")
    val neighborhood: Any?,
    @Json(name = "number")
    val number: Any?,
    @Json(name = "state")
    val state: String,
    @Json(name = "street")
    val street: Any?,
    @Json(name = "zipCode")
    val zipCode: Any?
)