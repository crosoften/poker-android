package com.draccoapp.poker.api.service

import com.draccoapp.poker.api.model.request.TokenRequest
import com.draccoapp.poker.api.model.response.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface NotificationService {
    @POST("notifications/save-token")
    suspend fun saveToken(@Body body: TokenRequest): Response<TokenResponse>
}

