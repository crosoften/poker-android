package com.draccoapp.poker.api.model.response.homeFrament


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocationX(
    @Json(name = "city")
    val city: String,
    @Json(name = "country")
    val country: String,
    @Json(name = "lat")
    val lat: Any?,
    @Json(name = "lng")
    val lng: Any?,
    @Json(name = "neighborhood")
    val neighborhood: String,
    @Json(name = "number")
    val number: String,
    @Json(name = "state")
    val state: String,
    @Json(name = "street")
    val street: String,
    @Json(name = "zipCode")
    val zipCode: String,
    @Json(name = "distance")
    val distance: String

)