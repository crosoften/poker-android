package com.draccoapp.poker.api.service

import com.draccoapp.poker.api.model.response.chat.ChatResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ChatService{

    @GET("/chats")
    suspend fun getAllChats() : List<ChatResponse>

    @GET("/chats/{id}")
    suspend fun getChatById(
        @Path("id") chatId: String
    ): ChatResponse
}