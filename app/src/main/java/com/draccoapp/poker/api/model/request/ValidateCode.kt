package com.draccoapp.poker.api.model.request


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ValidateCode(
    @Json(name = "email")
    val email: String,
    @Json(name = "code")
    val code: String
)