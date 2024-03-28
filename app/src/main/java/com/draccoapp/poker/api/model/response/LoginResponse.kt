package com.draccoapp.poker.api.model.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginResponse(
    @Json(name = "jwt")
    val jwt: String,
    @Json(name = "id")
    val id: Int,
    @Json(name = "role")
    val role: String
)