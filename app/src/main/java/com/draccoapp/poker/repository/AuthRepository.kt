package com.draccoapp.poker.repository

import com.draccoapp.poker.api.model.request.Login
import com.draccoapp.poker.api.model.response.LoginResponse
import com.draccoapp.poker.api.service.AuthService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.lang.Exception

class AuthRepository(
    private val service: AuthService,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun login(login: Login): Result<LoginResponse?> =
        withContext(dispatcher){
            try {
                val response = service.login(
                    body = login
                )
                when {

                    response.isSuccessful -> {
                        Result.success(response.body())
                    }

                    else -> {
                        Result.failure(Throwable(response.message()))
                    }
                }
            }catch (e: Exception){
                Result.failure(Throwable(e.message))
            }
        }

}