package com.draccoapp.poker.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

object MaskEditUtil {
    const val FORMAT_CPF = "###.###.###-##"
    const val FORMAT_RUT = "##.###.###-#"
    const val FORMAT_PHONE = "(##) #####-####"
    const val FORMAT_PHONE_ATUALIZADO = "+## (##) #####-####"
    const val FORMAT_CEP = "#####-###"
    const val FORMAT_DATE = "##/##/####"
    const val FORMAT_EXPIRATION = "##/##"
    const val FORMAT_SECURITY_CODE = "###"

    private const val MAX_CPF_LENGTH = 11
    private const val MAX_RUT_LENGTH = 9
    private const val MAX_CNPJ_LENGTH = 14

    fun mask(ediTxt: EditText, mask: String): TextWatcher {
        return object : TextWatcher {
            var isUpdating = false
            var old = ""
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val str = unmask(s.toString())
                var emptyMask = ""
                if (isUpdating) {
                    old = str
                    isUpdating = false
                    return
                }

                var i = 0
                for (m in mask.toCharArray()) {
                    if (m != '#' && str.length > old.length) {
                        emptyMask += m
                        continue
                    }
                    emptyMask += try {
                        str[i]
                    } catch (e: Exception) {
                        break
                    }
                    i++
                }
                isUpdating = true
                ediTxt.setText(emptyMask)
                ediTxt.setSelection(emptyMask.length)
            }
        }
    }

    fun maskPhoneDDI(ediTxt: EditText, mask: String): TextWatcher {
        return object : TextWatcher {
            var isUpdating = false
            var old = ""
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val str = unmask(s.toString())
                var emptyMask = ""
                if (isUpdating) {
                    old = str
                    isUpdating = false
                    return
                }
// && m != '+'
                var i = 0
                for (m in mask.toCharArray()) {
                    if (m != '#' && m != '+' && str.length > old.length) {
                        emptyMask += m
                        continue
                    }
                    emptyMask += try {
                        str[i]
                    } catch (e: Exception) {
                        break
                    }
                    i++
                }
                isUpdating = true
                ediTxt.setText(emptyMask)
                ediTxt.setSelection(emptyMask.length)
            }
        }
    }

    fun maskCep(ediTxt: EditText): TextWatcher {
        return object : TextWatcher {
            private var isFormatting = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Verificar se a ação é devido à formatação do texto
                isFormatting = count > 0 && after == 0
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Não é necessário implementar nada aqui
            }

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (!isFormatting) {
                        val input = s.toString()
                        val formattedInput = StringBuilder()

                        // Remover todos os caracteres não numéricos
                        val numericInput = input.replace(Regex("\\D"), "")

                        // Adicionar o hífen separador
                        for (i in numericInput.indices) {
                            formattedInput.append(numericInput[i])
                            if (i == 4) {
                                formattedInput.append("-")
                            }
                            if (i == 1) {
                                formattedInput.append(".")
                            }
                        }

                        // Definir o texto formatado
                        ediTxt.removeTextChangedListener(this)
                        ediTxt.setText(formattedInput.toString())
                        ediTxt.setSelection(formattedInput.length)
                        ediTxt.addTextChangedListener(this)

                        // Verificar se o CEP está completo
                        if (numericInput.length == 8) {
                            // Indicar que o CEP foi totalmente digitado e fazer a requisição
//                            makeRequest()
                        }
                    } else {
                        // Limpar a formatação para permitir a deleção de caracteres
                        isFormatting = false
                    }
                }
            }

        }
    }

    fun maskDocument(ediTxt: EditText): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val str = unmask(s.toString())
                val mask = when {
                    str.length <= MAX_CPF_LENGTH -> "###.###.###-##"
                    str.length <= MAX_RUT_LENGTH -> "##.###.###-#"
                    str.length <= MAX_CNPJ_LENGTH -> "##.###.###/####-##"
                    else -> "" // No mask for lengths greater than MAX_CNPJ_LENGTH
                }
                ediTxt.removeTextChangedListener(this)
                ediTxt.setText(applyMask(str, mask))
                ediTxt.setSelection(ediTxt.text?.length ?: 0)
                ediTxt.addTextChangedListener(this)
            }
        }
    }

    fun maskCpfRut(ediTxt: EditText): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val str = unmask(s.toString())
                val mask = when {
                    str.length <= MAX_RUT_LENGTH -> "##.###.###-#"
                    str.length <= MAX_CPF_LENGTH -> "###.###.###-##"
                    else -> "###.###.###-##" // No mask for lengths greater than MAX_CNPJ_LENGTH
                }
                ediTxt.removeTextChangedListener(this)
                ediTxt.setText(applyMask(str, mask))
                ediTxt.setSelection(ediTxt.text?.length ?: 0)
                ediTxt.addTextChangedListener(this)
            }
        }
    }


    private fun applyMask(str: String, mask: String): String {
        var result = ""
        var i = 0
        for (m in mask.toCharArray()) {
            if (i >= str.length) break
            if (m != '#') {
                result += m
                continue
            }
            result += str[i]
            i++
        }
        return result
    }

    fun unmask(s: String): String {
        return s.replace("[.]".toRegex(), "").replace("[-]".toRegex(), "")
            .replace("[/]".toRegex(), "").replace("[(]".toRegex(), "").replace("[ ]".toRegex(), "")
            .replace("[:]".toRegex(), "").replace("[)]".toRegex(), "")
    }

    fun unmaskTelefone(s: String): String {
        return s.replace("[.]".toRegex(), "").replace("[-]".toRegex(), "")
            .replace("[/]".toRegex(), "").replace("[ ]".toRegex(), "")
            .replace("[:]".toRegex(), "")
    }
}