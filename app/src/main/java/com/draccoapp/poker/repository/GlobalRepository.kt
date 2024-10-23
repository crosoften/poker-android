package com.draccoapp.poker.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.draccoapp.poker.api.model.request.AnswerBody
import com.draccoapp.poker.api.pagging.Paginacao
import com.draccoapp.poker.api.service.GlobalService
import com.draccoapp.poker.utils.Preferences


class GlobalRepository(
    private val service: GlobalService,
    private val preferences: Preferences
) {

    fun getMeusDados() = service.getMeusDados()

    fun getHomeFragment() = service.getHomeFragment(
       lat = preferences.getLatitude(),
        lng = preferences.getLongitude()
    )


    fun listarContractsPaginados() = Pager(
        config = PagingConfig(pageSize = 10, maxSize = 30),
        pagingSourceFactory = { Paginacao(service) }
    ).liveData
}