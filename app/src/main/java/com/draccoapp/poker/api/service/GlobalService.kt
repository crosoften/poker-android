package com.draccoapp.poker.api.service

import com.draccoapp.poker.api.model.response.MeusDadosResponse
import com.draccoapp.poker.api.model.response.homeFrament.HomeFragmentResponse
import retrofit2.Call
import retrofit2.http.GET

interface GlobalService {

//    @GET("/accounts/myself")
//    fun getMeusDados(): Call<MeusDadosResponse>

    @GET("/accounts/myself/home")
    fun getHomeFragment(): Call<HomeFragmentResponse>


}