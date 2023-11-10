package com.draccoapp.poker.utils

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class CodeValidatedHandler(
    private val parentLayout: View,
    private val nextButton: Button
) {

    private val textInputLayouts = mutableListOf<TextInputLayout>()

    init {
        setupListeners()
    }

    private fun setupListeners() {
        findTextInputLayouts(parentLayout)
        setupTextChangeListeners()
    }

    private fun findTextInputLayouts(view: View) {
        if (view is TextInputLayout) {
            textInputLayouts.add(view)
        } else if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val child = view.getChildAt(i)
                findTextInputLayouts(child)
            }
        }

        textInputLayouts[0].requestFocus()
    }

    private fun setupTextChangeListeners() {
        for (textInputLayout in textInputLayouts) {
            val editText = textInputLayout.editText as TextInputEditText
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s?.length == 1) {
                        val index = textInputLayouts.indexOf(textInputLayout)
                        if (index < textInputLayouts.size - 1) {
                            textInputLayouts[index + 1].requestFocus()
                        }

                    }

                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }

    fun isAllDigitsEntered(): Boolean {
        return textInputLayouts.all { it.editText?.text?.length == 1 }
    }


    fun getCodeValidated(): String {
        val code = StringBuilder()
        for (textInputLayout in textInputLayouts) {
            code.append(textInputLayout.editText?.text)
        }
        return code.toString()
    }

}