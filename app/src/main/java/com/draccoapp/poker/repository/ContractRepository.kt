package com.draccoapp.poker.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.draccoapp.poker.api.pagging.Paginacao
import com.draccoapp.poker.api.service.ContractService

class ContractRepository(
    private val service: ContractService
) {
    suspend fun getAllContracts() = service.getAllContracts()
    suspend fun getContractById(contractId: String) = service.getContractById(contractId)

    fun listarContractsPaginados() = Pager(
        config = PagingConfig(pageSize = 10, maxSize = 30),
        pagingSourceFactory = { Paginacao(service) }
    ).liveData
}