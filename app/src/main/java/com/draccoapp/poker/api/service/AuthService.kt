package com.draccoapp.poker.api.service

import com.draccoapp.poker.api.model.request.ChangePassword
import com.draccoapp.poker.api.model.request.Contact
import com.draccoapp.poker.api.model.request.Login
import com.draccoapp.poker.api.model.request.RequestCode
import com.draccoapp.poker.api.model.request.RequestPasswordReset
import com.draccoapp.poker.api.model.request.ValidateCode
import com.draccoapp.poker.api.model.request.ValidatePasswordReset
import com.draccoapp.poker.api.model.response.LoginResponse
import com.draccoapp.poker.api.model.response.ValidateCodeResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("user/auth/requestCode")
    suspend fun requestCode(
        @Body body: RequestCode
    ): Response<Void>

    @POST("user/auth/validateCode")
    suspend fun validateCode(
        @Body body: ValidateCode
    ): Response<ValidateCodeResponse>

    @POST("user/auth/validatePasswordReset")
    suspend fun validatePasswordReset(
        @Body body: ValidatePasswordReset
    ): Response<Void>

    @POST("user/auth/login")
    suspend fun login(
        @Body body: Login
    ): Response<LoginResponse>

    @POST("user/auth/requestPasswordReset")
    suspend fun requestPasswordReset(
        @Body body: RequestPasswordReset
    ): Response<Void>

    @POST("user/auth/changePassword")
    suspend fun changePassword(
        @Body body: ChangePassword
    ): Response<Void>

    @POST("user/auth/contact")
    suspend fun contact(
        @Body body: Contact
    ): Response<Void>


}