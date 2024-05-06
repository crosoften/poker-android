package com.draccoapp.poker.api.modelOld.request


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RequestPasswordReset(
    @Json(name = "email")
    val email: String
)