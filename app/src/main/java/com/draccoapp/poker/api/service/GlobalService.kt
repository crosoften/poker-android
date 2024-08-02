package com.draccoapp.poker.api.service

import com.draccoapp.poker.api.model.response.contract.ContractResponse
import com.draccoapp.poker.api.model.response.homeFrament.HomeFragmentResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GlobalService {

//    @GET("/accounts/myself")
//    fun getMeusDados(): Call<MeusDadosResponse>

    @GET("/accounts/myself/home")
    fun getHomeFragment(): Call<HomeFragmentResponse>

    @GET("/contracts")
    suspend fun listarContractsPaginados(@Query("page") page: Int, @Query("size") size : Int = 10 ): ContractResponse


}