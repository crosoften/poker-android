package com.draccoapp.poker.api.model.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserShort(
    @Json(name = "name")
    val name: String,
    @Json(name = "profilePictureURL")
    val profilePictureURL: String
)