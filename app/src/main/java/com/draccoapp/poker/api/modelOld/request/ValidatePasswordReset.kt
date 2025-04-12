package com.draccoapp.poker.api.modelOld.request


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ValidatePasswordReset(
    @Json(name = "email")
    val email: String,
    @Json(name = "newPassword")
    val newPassword: String,
    @Json(name = "code")
    val code: Int
)