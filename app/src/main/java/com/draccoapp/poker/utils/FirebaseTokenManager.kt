package com.draccoapp.poker.utils

import android.content.Context
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.draccoapp.poker.api.model.request.TokenRequest
import com.draccoapp.poker.api.service.NotificationService
import com.draccoapp.poker.repository.NotificationRepository
import kotlinx.coroutines.tasks.await
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class FirebaseTokenManager(
    private val context: Context,
    notificationRepository: NotificationRepository
) {

    private val preferences by lazy { Preferences(context) }
    private val notificationService by lazy { createNotificationService() }

    suspend fun getCurrentToken(): String? {
        return try {
            FirebaseMessaging.getInstance().token.await()
        } catch (e: Exception) {
            Log.e(TAG, "Error getting FCM token", e)
            null
        }
    }

    suspend fun refreshAndSaveToken(): Boolean {
        return try {
            val token = getCurrentToken()
            if (token != null) {
                val accountId = getCurrentAccountId()
                val request = TokenRequest(accountId = accountId, token = token)
                val response = notificationService.saveToken(request)

                if (response.isSuccessful) {
                    Log.d(TAG, "Token refreshed and saved successfully")
                    true
                } else {
                    Log.e(TAG, "Failed to save token: ${response.code()} - ${response.message()}")
                    false
                }
            } else {
                Log.e(TAG, "Failed to get FCM token")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error refreshing token", e)
            false
        }
    }

    private fun createNotificationService(): NotificationService {
        // Ajuste a URL base conforme sua API
        val retrofit = Retrofit.Builder()
            .baseUrl("https://sua-api.com/") // Substitua pela sua URL
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        return retrofit.create(NotificationService::class.java)
    }

    private fun getCurrentAccountId(): String {
        // Ajuste conforme sua implementação de Preferences
        return preferences.getUserId().toString() // ou como você obtém o accountId
    }

    companion object {
        private const val TAG = "FirebaseTokenManager"
    }
}