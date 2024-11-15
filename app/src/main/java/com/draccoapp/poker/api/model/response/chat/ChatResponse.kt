package com.draccoapp.poker.api.model.response.chat

data class ChatResponse(
    val id: String,
    val playerId: String,
    val playerName: String,
    val type: String,
//    val messages: List<Message>,
    val unreadMessages: Int,
    val createdAt: String,
    val updatedAt: String,
)
