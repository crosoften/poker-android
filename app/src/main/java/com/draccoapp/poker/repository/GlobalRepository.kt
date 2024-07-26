package com.draccoapp.poker.repository

import com.draccoapp.poker.api.service.GlobalService

class GlobalRepository(private val service: GlobalService) {

    fun getMeusDados() = service.getMeusDados()

}