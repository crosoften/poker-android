package com.draccoapp.poker.repository

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
}