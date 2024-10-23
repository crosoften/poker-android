package com.draccoapp.poker.api.service

import com.draccoapp.poker.api.model.request.AnswerBody
import com.draccoapp.poker.api.model.response.AnswerResponse
import com.draccoapp.poker.api.model.response.MeusDadosResponse
import com.draccoapp.poker.api.model.response.contract.ContractResponse
import com.draccoapp.poker.api.model.response.homeFrament.HomeFragmentResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface GlobalService {

    @GET("/accounts/myself")
    fun getMeusDados(): Call<MeusDadosResponse>

    @GET("/accounts/myself/home")
    fun getHomeFragment(
        @Query("lat") lat: String,
        @Query("lng") lng: String
    ): Call<HomeFragmentResponse>

    @GET("/contracts")
    suspend fun listarContractsPaginados(@Query("page") page: Int, @Query("size") size: Int = 10): ContractResponse

//    @POST("/tournaments/66a193b8350cc2d74e989325/subscribe")
//    fun subscribeTournament(): Call<Void>


}