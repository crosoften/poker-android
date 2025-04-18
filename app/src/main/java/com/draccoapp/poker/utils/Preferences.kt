package com.draccoapp.poker.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.draccoapp.poker.api.model.response.Login2faResponseNew
import com.draccoapp.poker.api.model.response.LoginResponse
import com.draccoapp.poker.api.model.response.homeFrament.Myself
import com.draccoapp.poker.api.modelOld.response.User
import com.google.android.gms.maps.model.LatLng

class Preferences(context: Context) {


    var preferences: SharedPreferences
    var editor: SharedPreferences.Editor
    private var MODE_PRIVATE: Int = 0

    init {
        preferences = context.getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE)
        editor = preferences.edit()
    }

    companion object {
        const val PREFERENCES_NAME = "Preferences"
        const val IS_LOGIN = "is_login"
        const val IS_AUTORIZED = "is_authorized"
        const val KEY_ACCESS_TOKEN = "access_token"
        const val KEY_ROLE = "role"
        const val KEY_ID = "user_id"
        const val KEY_NAME = "user_name"
        const val KEY_EMAIL = "user_email"
        const val KEY_BIRTHDATE = "user_birthdate"
        const val KEY_PHONE = "user_phone"
        const val KEY_GENDER = "user_gender"
        const val KEY_COUNTRY = "user_country"
        const val KEY_CITY = "user_city"
        const val KEY_LANGUAGE = "user_language"
        const val KEY_UNITS = "user_units"
        const val KEY_IMAGE = "user_image"
        const val KEY_CONTRACT_ACTIVE = "contract_active"
        const val KEY_LOCATION_LAT = "key_location_lat"
        const val KEY_LOCATION_LNG = "key_location_lng"
    }

    fun createSession( userData: Myself, onSessionCreated: () -> Unit) {
        Log.i("dadosTeste", "createSession: $userData")
        editor.putBoolean(IS_LOGIN, true)
        editor.putString(KEY_EMAIL, userData.email)
        editor.putString(KEY_NAME, userData.name)
        editor.putString(KEY_IMAGE, userData.imageUrl)
        editor.putBoolean(KEY_CONTRACT_ACTIVE, userData.currentContract != null)

        editor.commit()
        editor.apply()
        onSessionCreated()
    }

    //
//    fun isFirstAccess(): Boolean {
//        return preferences.getBoolean(KEY_FIRST_ACCESS, false)
//
//    }
//
//    fun hasCompleted(): Boolean {
//        return preferences.getBoolean(HAS_COMPLETED, false)
//
//    }
//
//    fun setHasCompleted() {
//        editor.putBoolean(HAS_COMPLETED, true)
//        editor.commit()
//        editor.apply()
//    }
//
//    fun setFirstAccess() {
//        editor.putBoolean(KEY_FIRST_ACCESS, true)
//        editor.commit()
//        editor.apply()
//    }
//
    fun location(location: LatLng) {
        editor.putString(KEY_LOCATION_LAT, location.latitude.toString())
        editor.putString(KEY_LOCATION_LNG, location.longitude.toString())
        editor.commit()
        editor.apply()
    }

    fun getLatitude(): String {
        return preferences.getString(KEY_LOCATION_LAT, "").toString()
    }


    fun getLongitude(): String {
        return preferences.getString(KEY_LOCATION_LNG, "").toString()
    }

    fun getContractStatus(): Boolean {
        return preferences.getBoolean(KEY_CONTRACT_ACTIVE, false)
    }
    //
//    fun updateMe(name: String, cpf: String, phone: String) {
//        editor.putString(KEY_NAME, name)
//        editor.putString(KEY_CPF, cpf)
//        editor.putString(KEY_PHONE, phone)
//        editor.commit()
//        editor.apply()
//    }
//
//    fun getDocument() : String {
//        return preferences.getString(KEY_CPF, "").toString()
//    }
//
//    fun getPhone() : String {
//        return preferences.getString(KEY_PHONE, "").toString()
//    }
//
//    fun getName() : String {
//        return preferences.getString(KEY_NAME, "").toString()
//    }
//
//    fun getImage() : String? {
//        return preferences.getString(KEY_IMAGE, null)
//    }
//
//
    fun getToken(): String {
        return preferences.getString(KEY_ACCESS_TOKEN, null).orEmpty()
    }

    fun isAutorized(): Boolean? {
        return preferences.getBoolean(IS_AUTORIZED, null == true)
    }

    fun setAutorized(autorized: Boolean) {
        editor.putBoolean(IS_AUTORIZED, autorized)
        editor.commit()
        editor.apply()
    }

    fun getLanguage(): String {
        return preferences.getString(KEY_LANGUAGE, "").orEmpty()

    }

    fun setLanguage(language: String) {
        editor.putString(KEY_LANGUAGE, language)
        editor.commit()
        editor.apply()
    }

    fun getCountry(): String {
        return preferences.getString(KEY_COUNTRY, "").orEmpty()
    }

    fun saveToken(token: Login2faResponseNew) {
        Log.i("TokenWill", "saveToken: no preferences   token a ser salvo é ${token.accessToken}")
        editor.putBoolean(IS_LOGIN, true)
        editor.putString(KEY_ACCESS_TOKEN, token.accessToken)
        editor.commit()
        editor.apply()
    }

    fun saveUserId(userId: String){
        editor.putString(KEY_ID, userId)
        editor.commit()
        editor.apply()
    }

    fun getUserId(): String {
        return preferences.getString(KEY_ID, null).orEmpty()
    }

    fun getUnit(): String {
        return preferences.getString(KEY_UNITS, "").orEmpty()
    }

    fun saveUser(user: User) {
        editor.putInt(KEY_ID, user.id)
        editor.putString(KEY_NAME, user.name)
        editor.putString(KEY_EMAIL, user.email)
        editor.putString(KEY_BIRTHDATE, user.birthdate)
        editor.putString(KEY_PHONE, user.phoneNumber)
        editor.putString(KEY_GENDER, user.gender)
        editor.putString(KEY_COUNTRY, user.country)
        editor.putString(KEY_CITY, user.city)
        editor.putString(KEY_LANGUAGE, user.language)
        editor.putString(KEY_UNITS, user.units)
        editor.putString(KEY_IMAGE, user.profilePicture)
        editor.commit()
        editor.apply()
    }

    //
//    fun getRefreshToken() : String {
//        return preferences.getString(KEY_TOKEN_REFRESH, "").orEmpty()
//    }
//
//    fun hasSession(): Boolean {
//        return preferences.getBoolean(IS_LOGIN, false)
//    }
//
//    fun isTokenExpiring(): Boolean {
//        val expiresIn: Int = preferences.getInt(KEY_EXPIRES_IN, 0)
//        val expiryTime = java.time.Instant.ofEpochSecond(expiresIn.toLong())
//            .atZone(java.time.ZoneId.systemDefault())
//            .toLocalDateTime()
//
//        val currentTime = java.time.LocalDateTime.now()
//
//        val duration = java.time.Duration.between(currentTime, expiryTime)
//
//        return duration.toHours() < 1
//    }
//
//
//
    fun logout() {
        editor.clear()
        editor.commit()
    }


}