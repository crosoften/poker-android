package com.draccoapp.poker.api.model.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "email")
    val email: String,
    @Json(name = "birthdate")
    val birthdate: String,
    @Json(name = "phoneNumber")
    val phoneNumber: String,
    @Json(name = "gender")
    val gender: String,
    @Json(name = "country")
    val country: String,
    @Json(name = "city")
    val city: String,
    @Json(name = "language")
    val language: String,
    @Json(name = "units")
    val units: String,
    @Json(name = "profilePicture")
    val profilePicture: String
)