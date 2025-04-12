package com.draccoapp.poker.api.model.request


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Login2FARequest(
    @Json(name = "key")
    val key: String,
    @Json(name = "code")
    val code: String
)