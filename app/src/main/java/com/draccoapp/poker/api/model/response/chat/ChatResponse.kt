package com.draccoapp.poker.api.model.response.chat

import com.draccoapp.poker.api.model.ChatMessageReceived
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChatResponse(
    val id: String,
    val playerId: String,
    val playerName: String,
    val type: String,
    val messages: List<ChatMessageReceived>,
    val unreadMessages: Int,
    val createdAt: String,
    val updatedAt: String,
)
