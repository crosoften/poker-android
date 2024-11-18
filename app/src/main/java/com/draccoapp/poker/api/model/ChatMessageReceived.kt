package com.draccoapp.poker.api.model

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
