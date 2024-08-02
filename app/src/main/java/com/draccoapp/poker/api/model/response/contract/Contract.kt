package com.draccoapp.poker.api.model.response.contract


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Contract(
    @Json(name = "endDate")
    val endDate: String,
    @Json(name = "fileUrl")
    val fileUrl: String,
    @Json(name = "id")
    val id: String,
    @Json(name = "playerId")
    val playerId: String,
    @Json(name = "playerSignature")
    val playerSignature: Boolean,
    @Json(name = "sponsorSignature")
    val sponsorSignature: Boolean,
    @Json(name = "startDate")
    val startDate: String,
    @Json(name = "status")
    val status: String,
    @Json(name = "title")
    val title: String,
    @Json(name = "value")
    val value: Int
)