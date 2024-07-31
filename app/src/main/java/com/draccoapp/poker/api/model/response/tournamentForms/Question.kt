package com.draccoapp.poker.api.model.response.tournamentForms


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Question(
    @Json(name = "description")
    val description: Any?,
    @Json(name = "imageUrl")
    val imageUrl: Any?,
    @Json(name = "options")
    val options: List<Option>,
    @Json(name = "question")
    val question: String,
    @Json(name = "required")
    val required: Boolean,
    @Json(name = "type")
    val type: String
)