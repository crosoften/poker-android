package com.draccoapp.poker.api.model.response.homeFrament

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Answers(
    @Json(name = "type")
    val type: String,
    @Json(name = "required")
    val required: Boolean,
    @Json(name = "question")
    val question: String,
    @Json(name = "description")
    val description: String?,
    @Json(name = "imageUrl")
    val imageUrl: String?,
    @Json(name = "options")
    val options: Any?,
    @Json(name = "answer")
    val answer: String



)
