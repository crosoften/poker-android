package com.draccoapp.poker.api.model.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegisterStep1e2Response(
    @Json(name = "message")
    val message: String
)