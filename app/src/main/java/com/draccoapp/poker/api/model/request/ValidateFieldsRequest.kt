package com.draccoapp.poker.api.model.request


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ValidateFieldsRequest(
    @Json(name = "email")
    val email: String,
    @Json(name = "phone")
    val phone: String
)