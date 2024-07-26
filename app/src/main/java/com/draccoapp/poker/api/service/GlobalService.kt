package com.draccoapp.poker.api.service

import com.draccoapp.poker.api.model.response.MeusDadosResponse
import retrofit2.Call
import retrofit2.http.GET

interface GlobalService {

    @GET("/accounts/myself")
    fun getMeusDados(): Call<MeusDadosResponse>

}