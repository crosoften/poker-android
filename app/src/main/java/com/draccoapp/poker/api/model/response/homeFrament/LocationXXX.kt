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
    val lat: String?,
    @Json(name = "lng")
    val lng: String?,
    @Json(name = "neighborhood")
    val neighborhood: String?,
    @Json(name = "number")
    val number: String?,
    @Json(name = "state")
    val state: String,
    @Json(name = "street")
    val street: String?,
    @Json(name = "zipCode")
    val zipCode: String?,
    @Json(name = "distance")
    val distance: String?

)