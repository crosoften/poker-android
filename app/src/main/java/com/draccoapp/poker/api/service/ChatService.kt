package com.draccoapp.poker.api.service

import retrofit2.http.GET
import retrofit2.http.Path

interface ChatService{

    @GET("/chats")
    suspend fun getAllChats() : Unit
    @GET("/chats/{id}")

    suspend fun getChatById(
        @Path("id") chatId: Int
    ): Unit
}