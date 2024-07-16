package com.draccoapp.poker.utils


import android.text.InputFilter
import android.text.Spanned

class DecimalDigitsFilter(private val decimalDigits: Int) : InputFilter {

    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        val sb = StringBuilder(dest)
        sb.replace(dstart, dend, source?.subSequence(start, end).toString())

        val value = sb.toString()
        if (value.contains(".")) {
            val index = value.indexOf(".")
            val lengthAfterDecimal = value.length - index - 1

            if (lengthAfterDecimal > decimalDigits) {
                return ""
            }
        }

        return null
    }
}
