package com.draccoapp.poker.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import com.draccoapp.poker.di.listModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.util.Locale

class PokerApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this

        startKoin{
            androidContext(this@PokerApplication)
            modules(listModules)
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: Context
            private set
    }
}