package com.draccoapp.poker.api.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChatMessageReceived(
    val id: String,
    val chatId: String,
    val content: String,
    val sentById: String,
    val readByIds: List<String>,
    val createdAt: String,
)
