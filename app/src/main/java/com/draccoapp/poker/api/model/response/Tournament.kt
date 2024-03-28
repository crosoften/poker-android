package com.draccoapp.poker.api.model.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class Tournament(
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String?,
    @Json(name = "description")
    val description: String,
    @Json(name = "prize")
    val prize: Int,
    @Json(name = "date")
    val date: String,
    @Json(name = "latitude")
    val latitude: Double,
    @Json(name = "longitude")
    val longitude: Double,
    @Json(name = "imageURL")
    val imageURL: String,
    @Json(name = "distance")
    val distance: Double?
) : Serializable