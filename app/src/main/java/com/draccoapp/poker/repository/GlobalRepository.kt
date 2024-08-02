package com.draccoapp.poker.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.draccoapp.poker.api.pagging.Paginacao
import com.draccoapp.poker.api.service.GlobalService

class GlobalRepository(private val service: GlobalService) {

//    fun getMeusDados() = service.getMeusDados()

    fun getHomeFragment() = service.getHomeFragment()

    fun listarContractsPaginados() = Pager(
        config = PagingConfig(pageSize = 10, maxSize = 30),
        pagingSourceFactory = { Paginacao(service) }
    ).liveData
}