package com.draccoapp.poker.api.model.response.chat

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Participants(
    val id: String,
    val imageUrl: String,
    val name: String
)