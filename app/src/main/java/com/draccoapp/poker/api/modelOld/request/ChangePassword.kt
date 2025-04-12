package com.draccoapp.poker.api.modelOld.request


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChangePassword(
    @Json(name = "userId")
    val userId: Int,
    @Json(name = "oldPassword")
    val oldPassword: String,
    @Json(name = "newPassword")
    val newPassword: String
)