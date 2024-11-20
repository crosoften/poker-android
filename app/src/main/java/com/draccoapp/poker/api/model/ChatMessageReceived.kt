package com.draccoapp.poker.api.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChatMessageReceived(
    val id: String,
    val chatId: String,
    val sentByPlayer: Boolean,
    val content: String,
    val readByAdmin: Boolean,
    val readByPlayer: Boolean,
    val createdAt: String,
    val updatedAt: String
)
