package com.draccoapp.poker.repository

import com.draccoapp.poker.api.model.request.CreateRequest
import com.draccoapp.poker.api.model.request.Login
import com.draccoapp.poker.api.model.request.Login2FARequest
import com.draccoapp.poker.api.model.request.Login2faBodyNew
import com.draccoapp.poker.api.model.request.ValidateCode
import com.draccoapp.poker.api.model.request.ValidateFieldsRequest
import com.draccoapp.poker.api.model.response.CreateResponse
import com.draccoapp.poker.api.model.response.Login2FAResponse
import com.draccoapp.poker.api.model.response.Login2faResponseNew
import com.draccoapp.poker.api.model.response.ValidateCodeResponse
import com.draccoapp.poker.api.model.response.ValidateFieldsResponse
import com.draccoapp.poker.api.service.AuthService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.lang.Exception

class AuthRepository(
    private val service: AuthService,
    private val dispatcher: CoroutineDispatcher
) {

    //OLD
//    suspend fun login(body: Login): Result<LoginResponse?> =
//        withContext(dispatcher){
//            try {
//                val response = service.login(
//                    body = body
//                )
//                when {
//
//                    response.isSuccessful -> {
//                        Result.success(response.body())
//                    }
//
//                    else -> {
//                        Result.failure(Throwable(response.message()))
//                    }
//                }
//            }catch (e: Exception){
//                Result.failure(Throwable(e.message))
//            }
//        }

    fun login(body: Login) = service.login(body = body)

    fun login2faNew(body: Login2faBodyNew) = service.login2faNew(body = body)

    suspend fun login2fa(body: Login2FARequest): Result<Login2FAResponse?> =
        withContext(dispatcher) {
            try {
                val response = service.login2fa(
                    body = body
                )
                when {

                    response.isSuccessful -> {
                        Result.success(response.body())
                    }

                    else -> {
                        Result.failure(Throwable(response.message()))
                    }
                }
            } catch (e: Exception) {
                Result.failure(Throwable(e.message))
            }
        }

    suspend fun validateFields(body: ValidateFieldsRequest): Result<ValidateFieldsResponse?> =
        withContext(dispatcher) {
            try {
                val response = service.validateFields(
                    body = body
                )
                when {

                    response.isSuccessful -> {
                        Result.success(response.body())
                    }

                    else -> {
                        Result.failure(Throwable(response.message()))
                    }
                }
            } catch (e: Exception) {
                Result.failure(Throwable(e.message))
            }
        }

    suspend fun validateCode(body: ValidateCode): Result<ValidateCodeResponse?> =
        withContext(dispatcher) {
            try {
                val response = service.validateCode(
                    body = body
                )
                when {

                    response.isSuccessful -> {
                        Result.success(response.body())
                    }

                    else -> {
                        Result.failure(Throwable(response.message()))
                    }
                }
            } catch (e: Exception) {
                Result.failure(Throwable(e.message))
            }
        }

    suspend fun create(body: CreateRequest): Result<CreateResponse?> =
        withContext(dispatcher) {
            try {
                val response = service.create(
                    body = body
                )
                when {
                    response.isSuccessful -> {
                        Result.success(response.body())
                    }

                    else -> {
                        Result.failure(Throwable(response.message()))
                    }
                }
            } catch (e: Exception) {
                Result.failure(Throwable(e.message))
            }
        }

}