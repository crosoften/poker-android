package com.draccoapp.poker.api.model.response.homeFrament


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Account(
    @Json(name = "birthday")
    val birthday: String,
    @Json(name = "email")
    val email: String,
    @Json(name = "gender")
    val gender: String,
    @Json(name = "id")
    val id: String,
    @Json(name = "imageUrl")
    val imageUrl: String?,
    @Json(name = "location")
    val location: Location,
    @Json(name = "name")
    val name: String,
    @Json(name = "phone")
    val phone: String,
    @Json(name = "reprovedDueTo")
    val reprovedDueTo: Any?,
    @Json(name = "status")
    val status: String
)