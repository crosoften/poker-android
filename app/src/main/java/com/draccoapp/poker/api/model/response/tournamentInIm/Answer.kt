package com.draccoapp.poker.api.model.response.tournamentInIm


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Answer(
    @Json(name = "type")
    val type: String?,
    @Json(name = "required")
    val required: Boolean?,
    @Json(name = "question")
    val question: String?,
    @Json(name = "description")
    val description: Any?,
    @Json(name = "imageUrl")
    val imageUrl: Any?,
    @Json(name = "options")
    val options: List<Option?>?,
    @Json(name = "answer")
    val answer: String?
)