package com.draccoapp.poker.api.model.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TokenRequest(
    @Json(name = "accountId")
    val accountId: String,
    @Json(name = "token")
    val token: String
)
