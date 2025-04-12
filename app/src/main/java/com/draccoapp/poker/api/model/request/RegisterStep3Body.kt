package com.draccoapp.poker.api.model.request


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegisterStep3Body(
    @Json(name = "birthday")
    val birthday: String,
    @Json(name = "code")
    val code: String,
    @Json(name = "email")
    val email: String,
    @Json(name = "gender")
    val gender: String,
    @Json(name = "imageUrl")
    val imageUrl: String,
    @Json(name = "location")
    val location: LocationX,
    @Json(name = "name")
    val name: String,
    @Json(name = "password")
    val password: String,
    @Json(name = "passwordConfirmation")
    val passwordConfirmation: String,
    @Json(name = "phone")
    val phone: String
)