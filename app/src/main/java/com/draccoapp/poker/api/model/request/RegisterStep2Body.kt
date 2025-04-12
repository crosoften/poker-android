package com.draccoapp.poker.api.model.request


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegisterStep2Body(
    @Json(name = "code")
    val code: String,
    @Json(name = "email")
    val email: String
)