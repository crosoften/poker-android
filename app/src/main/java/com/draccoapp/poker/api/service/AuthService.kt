package com.draccoapp.poker.api.service

import com.draccoapp.poker.api.model.request.CreateRequest
import com.draccoapp.poker.api.model.request.Login
import com.draccoapp.poker.api.model.request.Login2FARequest
import com.draccoapp.poker.api.model.request.Login2faBodyNew
import com.draccoapp.poker.api.model.request.ValidateFieldsRequest
import com.draccoapp.poker.api.model.response.CreateResponse
import com.draccoapp.poker.api.model.response.Login2FAResponse
import com.draccoapp.poker.api.model.response.ValidateFieldsResponse
import com.draccoapp.poker.api.model.request.ValidateCode
import com.draccoapp.poker.api.model.response.Login2faResponseNew
import com.draccoapp.poker.api.model.response.LoginResponse
import com.draccoapp.poker.api.model.response.ValidateCodeResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("accounts")
    suspend fun create(
        @Body body: CreateRequest
    ): Response<CreateResponse>

    @POST("accounts/validate-fields/code")
    suspend fun validateCode(
        @Body body: ValidateCode
    ): Response<ValidateCodeResponse>

    @POST("accounts/validate-fields")
    suspend fun validateFields(
        @Body body: ValidateFieldsRequest
    ): Response<ValidateFieldsResponse>

    //OLD
//    @POST("auth/login")
//    suspend fun login(
//        @Body body: Login
//    ): Response<LoginResponse>

    @POST("/auth/login/2fa")
    fun login2faNew(
        @Body body: Login2faBodyNew
    ): Call<Login2faResponseNew>


    @POST("auth/login")
    fun login(
        @Body body: Login
    ): Call<LoginResponse>

    @POST("auth/login/2fa")
    suspend fun login2fa(
        @Body body: Login2FARequest
    ): Response<Login2FAResponse>


}