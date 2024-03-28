package com.draccoapp.poker.repository

import com.draccoapp.poker.api.model.request.UpdateLocation
import com.draccoapp.poker.api.model.response.User
import com.draccoapp.poker.api.service.UserService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.lang.Exception

class UserRepository(
    private val service: UserService,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun getUserById(userId: Int): Result<User?> =
        withContext(dispatcher){
            try {
                val response = service.getUserById(
                    userId = userId
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

    suspend fun updateLocation(userId: Int, update: UpdateLocation): Result<Unit?> =
        withContext(dispatcher){
            try {
                val response = service.updateLocation(
                    userId = userId,
                    body = update
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