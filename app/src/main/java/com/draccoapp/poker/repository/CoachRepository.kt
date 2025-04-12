package com.draccoapp.poker.repository

import com.draccoapp.poker.api.model.response.chat.ChatResponse
import com.draccoapp.poker.api.service.ChatService

class CoachRepository(
    private val service: ChatService
) {
    suspend fun getAllChats() = service.getAllChats()
    suspend fun getChatById(chatId: String): ChatResponse {
        return service.getChatById(chatId)
    }
}