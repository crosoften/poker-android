package com.draccoapp.poker.api.model.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Login2faResponseNew(
    @Json(name = "accessToken")
    val accessToken: String
)