package com.draccoapp.poker.api.model.response.homeFrament


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Tournament(
    @Json(name = "createdAt")
    val createdAt: String,
    @Json(name = "description")
    val description: String,
    @Json(name = "eventUrl")
    val eventUrl: String,
    @Json(name = "finalDatetime")
    val finalDatetime: Any?,
    @Json(name = "formId")
    val formId: Any?,
    @Json(name = "id")
    val id: String,
    @Json(name = "imageUrl")
    val imageUrl: Any?,
    @Json(name = "location")
    val location: LocationXXX,
    @Json(name = "prize")
    val prize: Int,
    @Json(name = "rules")
    val rules: Any?,
    @Json(name = "startDatetime")
    val startDatetime: String,
    @Json(name = "status")
    val status: String,
    @Json(name = "time")
    val time: Any?,
    @Json(name = "title")
    val title: String,
    @Json(name = "type")
    val type: String,
    @Json(name = "updatedAt")
    val updatedAt: String
)