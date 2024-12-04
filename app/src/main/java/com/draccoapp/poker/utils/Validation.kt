package com.draccoapp.poker.utils

import android.widget.EditText

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

    fun validateEditTexts(vararg editTexts: EditText): Boolean {
        return editTexts.all { it.text.toString().isNotEmpty() }
    }
}

