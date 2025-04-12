package com.draccoapp.poker.api.model.request


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Login2faBodyNew(
    @Json(name = "code")
    val code: String,
    @Json(name = "key")
    val key: String
)