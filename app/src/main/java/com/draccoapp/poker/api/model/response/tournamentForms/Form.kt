package com.draccoapp.poker.api.model.response.tournamentForms


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Form(
    @Json(name = "createdAt")
    val createdAt: String,
    @Json(name = "id")
    val id: String,
    @Json(name = "questions")
    val questions: List<Question>,
    @Json(name = "status")
    val status: String,
    @Json(name = "title")
    val title: String,
    @Json(name = "updatedAt")
    val updatedAt: String
)