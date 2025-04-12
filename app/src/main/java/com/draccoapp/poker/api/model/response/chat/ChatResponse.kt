package com.draccoapp.poker.api.model.response.chat

import com.draccoapp.poker.api.model.ChatMessageReceived
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChatResponse(
    val id: String,
    val title: String,
    val participants: List<Participants>,
    val messages: List<ChatMessageReceived>,
    val unreadMessages: Int? = null,
    val createdAt: String,
    val updatedAt: String,
)
