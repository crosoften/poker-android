package com.draccoapp.poker.utils

object Validation {

    fun isEmailValid(email: String?): Boolean {
        if (email.isNullOrEmpty())
            return false

        return android
            .util
            .Patterns
            .EMAIL_ADDRESS
            .matcher(email)
            .matches()
    }

}