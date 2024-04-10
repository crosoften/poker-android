package com.draccoapp.poker.api.model.request


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateRequest(
    @Json(name = "code")
    val code: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "birthday")
    val birthday: String,
    @Json(name = "gender")
    val gender: String,
    @Json(name = "email")
    val email: String,
    @Json(name = "phone")
    val phone: String,
    @Json(name = "password")
    val password: String,
    @Json(name = "passwordConfirmation")
    val passwordConfirmation: String,
    @Json(name = "imageUrl")
    val imageUrl: String,
    @Json(name = "location")
    val location: Location
)