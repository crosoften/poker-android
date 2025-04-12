package com.draccoapp.poker.api.service

import com.draccoapp.poker.api.modelOld.request.UpdateLocation
import com.draccoapp.poker.api.modelOld.response.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface UserService {

    @Multipart
    @POST("user")
    suspend fun createUser(
        @Part("name") name : String,
        @Part("email") email : String,
        @Part("birthdate") birthdate : String,
        @Part("phoneNumber") phoneNumber : String,
        @Part("gender") gender : String,
        @Part("country") country : String,
        @Part("city") city : String,
        @Part("language") language : String,
        @Part("units") units : String,
        @Part("profilePicture") profilePicture : String,
        @Part("password") password : String
    ): Response<Void>

    @PATCH("/user/{userId}/updateLocation")
    suspend fun updateLocation(
        @Path("userId") userId: Int,
        @Body body: UpdateLocation
    ): Response<Unit>

    @GET("/user/{userId}")
    suspend fun getUserById(
        @Path("userId") userId: Int
    ): Response<User>
}