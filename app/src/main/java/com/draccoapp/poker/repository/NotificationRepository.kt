package com.draccoapp.poker.repository

import android.util.Log
import com.draccoapp.poker.api.model.request.TokenRequest
import com.draccoapp.poker.api.model.response.TokenResponse
import com.draccoapp.poker.api.service.NotificationService
import com.draccoapp.poker.utils.Preferences
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.lang.Exception

class NotificationRepository(
    private val service: NotificationService,
    private val preferences: Preferences,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun saveToken(accountId: String, token: String): Result<TokenResponse?> =
        withContext(dispatcher) {
            try {
                val body = TokenRequest(
                    accountId = accountId,
                    token = token
                )
                val response = service.saveToken(body)

                when {
                    response.isSuccessful -> {
                        Result.success(response.body())
                    }
                    response.code() == 400 -> {
                        Result.failure(Throwable("Parâmetros obrigatórios ausentes"))
                    }
                    response.code() == 500 -> {
                        Result.failure(Throwable("Erro ao salvar token"))
                    }
                    else -> {
                        Result.failure(Throwable(response.message()))
                    }
                }
            } catch (e: Exception) {
                Log.i("NotificationRepository", "saveToken: ${e.message}")
                Result.failure(Throwable(e.message))
            }
        }

    suspend fun saveCurrentUserToken(token: String): Result<TokenResponse?> =
        withContext(dispatcher) {
            try {
                val accountId = getCurrentAccountId()
                saveToken(accountId, token)
            } catch (e: Exception) {
                Log.i("NotificationRepository", "saveCurrentUserToken: ${e.message}")
                Result.failure(Throwable(e.message))
            }
        }

    private fun getCurrentAccountId(): String {
        return preferences.getUserId().toString()
    }
}
