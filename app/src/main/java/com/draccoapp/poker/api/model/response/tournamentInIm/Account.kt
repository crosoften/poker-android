package com.draccoapp.poker.api.model.response.tournamentInIm


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Account(
    @Json(name = "id")
    val id: String?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "birthday")
    val birthday: String?,
    @Json(name = "phone")
    val phone: String?,
    @Json(name = "gender")
    val gender: String?,
    @Json(name = "email")
    val email: String?,
    @Json(name = "status")
    val status: String?,
    @Json(name = "imageUrl")
    val imageUrl: Any?,
    @Json(name = "reprovedDueTo")
    val reprovedDueTo: Any?,
    @Json(name = "location")
    val location: Location?
)