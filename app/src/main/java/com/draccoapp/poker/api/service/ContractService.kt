package com.draccoapp.poker.api.service

import com.draccoapp.poker.api.model.response.contract.ContractResponse
import com.draccoapp.poker.api.model.response.contract.details.ContractDetails
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ContractService{

    @GET("/contracts")
    suspend fun getAllContracts(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10
    ) : ContractResponse

    @GET("/contracts")
    suspend fun listarContractsPaginados(@Query("page") page: Int, @Query("size") size: Int = 10): ContractResponse

    @GET("/contracts/{id}")
    suspend fun getContractById(
        @Path("id") contractId: String
    ): ContractDetails
}