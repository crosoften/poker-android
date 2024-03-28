package com.draccoapp.poker.utils

import android.app.Application
import com.draccoapp.poker.di.listModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PokerApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidContext(this@PokerApplication)
            modules(listModules)
        }
    }

}