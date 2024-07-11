package com.draccoapp.poker.utils

import android.content.Context
import android.content.SharedPreferences

object SharedUtils {
    private var mInstance: SharedPreferences? = null

    private val sessionPreferences: android.content.SharedPreferences
        get() {
            val ctx = PokerApplication.instance
            return ctx!!.getSharedPreferences("SESSION_PREFERENCES", Context.MODE_PRIVATE)
        }

    fun setValueInSharedPreferences(key: String, value: String) {
        sessionPreferences.edit().putString(key,value).apply()
    }

    fun getValueInSharedPreferences(key: String): String {
        // Retorna vazio caso nao encontre uma chave correspondente.
        return sessionPreferences.getString(key,"").toString()
    }

    fun removeValueInSharedPreferences(key: String) {
        sessionPreferences.edit()?.remove(key)?.apply()
    }


}