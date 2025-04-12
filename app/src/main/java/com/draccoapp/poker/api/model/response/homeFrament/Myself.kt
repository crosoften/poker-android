package com.draccoapp.poker.api.model.response.homeFrament


import com.draccoapp.poker.api.model.response.CurrentContract
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Myself(
    @Json(name = "id")
    val id: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "birthday")
    val birthday: String,
    @Json(name = "phone")
    val phone: String,
    @Json(name = "gender")
    val gender: String,
    @Json(name = "email")
    val email: String,
    @Json(name = "status")
    val status: String,
    @Json(name = "imageUrl")
    val imageUrl: String?,
    @Json(name = "reprovedDueTo")
    val reprovedDueTo: Any?,
    @Json(name = "location")
    val location: Location,
    @Json(name = "playerLevel")
    val playerLevel: String,
    @Json(name = "contractExpiresIn")
    val contractExpiresIn: String? = "0",
    @Json(name = "tournamentsCount")
    val tournamentsCount: Int,
    @Json(name = "contractProfit")
    val contractProfit: Double,
    @Json(name = "ranking")
    val ranking: Int,
    @Json(name = "currentContract")
    val currentContract: CurrentContract?,
    @Json(name = "overallInfos")
    val overallInfos: OverallInfosX?
)