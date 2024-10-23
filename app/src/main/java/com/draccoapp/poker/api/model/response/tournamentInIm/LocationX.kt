package com.draccoapp.poker.api.model.response.tournamentInIm


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocationX(
    @Json(name = "zipCode")
    val zipCode: String?,
    @Json(name = "street")
    val street: String?,
    @Json(name = "number")
    val number: String?,
    @Json(name = "neighborhood")
    val neighborhood: String?,
    @Json(name = "city")
    val city: String?,
    @Json(name = "state")
    val state: String?,
    @Json(name = "country")
    val country: String?,
    @Json(name = "lat")
    val lat: String?,
    @Json(name = "lng")
    val lng: String?,
    @Json(name = "distance")
    val distance: String
    )