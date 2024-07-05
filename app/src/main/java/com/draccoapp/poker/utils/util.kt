package com.draccoapp.poker.utils

import android.content.Context
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.regex.Matcher
import java.util.regex.Pattern

fun mostrarToast(message: String, context: Context) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun mostrarToastLonga(message: String, context: Context) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

fun String.formatToBrl(): String {
    val value = this.toFloatOrNull() ?: return this
    return String.format("%.2f", value).replace(".", ",")
}

fun toast(context: Context, text: String) {
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}

fun String.isValidUrl(): Boolean {
    val regex = """^(https?://)[^\s/$.?#].[^\s]*$""".toRegex()
    return regex.matches(this)
}

fun String.isValidEmail(): Boolean {
    val regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$".toRegex()
    return regex.matches(this)
}


fun converterDataNasc(date: String): String {
    val inputFormat = SimpleDateFormat("dd/MM/yyyy")
    val outputFormat = SimpleDateFormat("yyyy-MM-dd")
    try {
        val parsedDate: Date = inputFormat.parse(date)
        return outputFormat.format(parsedDate)
    } catch (e: Exception) {
        return "DataListarMeusCasos inválida"
    }
}

fun getVideoId(videoUrl: String): String {
    var videoId = "";
    val regex =
        "http(?:s)?:\\/\\/(?:m.)?(?:www\\.)?youtu(?:\\.be\\/|(?:be-nocookie|be)\\.com\\/(?:watch|[\\w]+\\?(?:feature=[\\w]+.[\\w]+\\&)?v=|v\\/|e\\/|embed\\/|user\\/(?:[\\w#]+\\/)+))([^&#?\n]+)"
    val pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    val matcher = pattern.matcher(videoUrl);
    if (matcher.find()) {
        videoId = matcher.group(1)?.toString() ?: "";
    }
    return videoId;
}


fun limparError(texto: String): String? {
    // Utilizando expressão regular para extrair a mensagem entre as aspas
    val pattern: Pattern = Pattern.compile("\"error\":\"([^\"]*)\"")
//    val pattern: Pattern = Pattern.compile("(message|error)\":\"([^\"]*)\"")
    val matcher: Matcher = pattern.matcher(texto)

    // Verificando se houve um match na expressão regular
    return if (matcher.find()) {
        // Retornando o conteúdo capturado entre as aspas
        matcher.group(1)
//        matcher.group(2)
    } else {
        // Caso não seja encontrado, retornando o texto original
        texto
    }
}

fun limparMessage(texto: String): String? {
    // Utilizando expressão regular para extrair a mensagem entre as aspas
//    val pattern: Pattern = Pattern.compile("\"message\":\"([^\"]*)\"")
    val pattern: Pattern = Pattern.compile("(message|error)\":\"([^\"]*)\"")
    val matcher: Matcher = pattern.matcher(texto)

    // Verificando se houve um match na expressão regular
    return if (matcher.find()) {
        // Retornando o conteúdo capturado entre as aspas
//        matcher.group(1)
        matcher.group(2)
    } else {
        // Caso não seja encontrado, retornando o texto original
        texto
    }
}