package com.draccoapp.poker.api.model.request


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegisterStep1Body(
    @Json(name = "email")
    val email: String,
    @Json(name = "phone")
    val phone: String
)