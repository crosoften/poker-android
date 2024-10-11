package com.draccoapp.poker.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import com.draccoapp.poker.R
import com.draccoapp.poker.api.model.response.homeFrament.LocationX
import com.draccoapp.poker.api.model.response.homeFrament.LocationXXX
import com.draccoapp.poker.api.model.response.homeFrament.NextTournament
import com.draccoapp.poker.api.model.response.homeFrament.Tournament
import com.draccoapp.poker.api.model.response.tournamentInIm.TournamentInImData
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
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


fun mapTournamentToNextTournament(tournament: Tournament): NextTournament {
    return NextTournament(
        description = tournament.description,
        eventUrl = tournament.eventUrl,
        finalDatetime = tournament.finalDatetime?.toString(),
        formId = tournament.formId?.toString(),
        id = tournament.id,
        imageUrl = tournament.imageUrl?.toString(),
        location = LocationX(
            city = tournament.location.city.toString(),
            country = tournament.location.country.toString(),
            lat = tournament.location.lat.toString(),
            lng = tournament.location.lng.toString(),
            neighborhood = tournament.location.neighborhood.toString(),
            number = tournament.location.number.toString(),
            state = tournament.location.state.toString(),
            street = tournament.location.street.toString(),
            zipCode = tournament.location.zipCode.toString(),
        ),
        prize = tournament.prize,
        rules = tournament.rules?.toString(),
        startDatetime = tournament.startDatetime,
        status = tournament.status,
        time = tournament.time?.toString(),
        title = tournament.title,
        type = tournament.type
    )
}

fun mapTournamentInImTournament(tournament: TournamentInImData): NextTournament {
    return NextTournament(
        description = tournament.tournament?.description,
        eventUrl = tournament.tournament?.eventUrl,
        finalDatetime = tournament.tournament?.finalDatetime?.toString(),
        formId = tournament.tournament?.formId?.toString(),
        id = tournament.tournament?.id,
        imageUrl = tournament.tournament?.imageUrl?.toString(),
        location = LocationX(
            city = tournament.tournament?.location?.city.toString(),
            country = tournament.tournament?.location?.country.toString(),
            lat = tournament.tournament?.location?.lat.toString(),
            lng = tournament.tournament?.location?.lng.toString(),
            neighborhood = tournament.tournament?.location?.neighborhood.toString(),
            number = tournament.tournament?.location?.number.toString(),
            state = tournament.tournament?.location?.state.toString(),
            street = tournament.tournament?.location?.street.toString(),
            zipCode = tournament.tournament?.location?.zipCode.toString(),
        ),
        prize = tournament.tournament?.prize ?: 0,
        rules = tournament.tournament?.rules?.toString(),
        startDatetime = tournament.tournament?.startDatetime,
        status = tournament.tournament?.status,
        time = tournament.tournament?.time?.toString(),
        title = tournament.tournament?.title,
        type = tournament.tournament?.type
    )
}


fun converterDataNextTournament(dataHoraString: String): String {
    // Formato original da string de data e hora
    val formatoOriginal = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())

    // Formato desejado para a data
    val formatoDesejado = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    return try {
        // Parse da string para objeto Date
        val dataHora = formatoOriginal.parse(dataHoraString)

        // Formatar para obter a data no formato desejado
        formatoDesejado.format(dataHora)
    } catch (e: Exception) {
        e.printStackTrace()
        "N/A" // ou lançar uma exceção, dependendo do contexto do seu app
    }
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

fun View.showSnackbarRed(message: String) {
    val snackBar = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    snackBar.setBackgroundTint(resources.getColor(R.color.vermelhoSnack))
    snackBar.show()
}

fun View.showSnackbarGreen(message: String) {
    val snackBar = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    snackBar.setBackgroundTint(resources.getColor(R.color.verdeSnack))
    snackBar.show()
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