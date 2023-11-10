package com.draccoapp.poker.data

import android.os.Parcelable
import com.draccoapp.poker.R
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import java.text.NumberFormat
import java.util.Locale
import kotlin.random.Random

@JsonClass(generateAdapter = true)
@Parcelize
data class Tournament(
    @Json(name = "image")
    val image: Int,
    @Json(name = "title")
    val title: String,
    @Json(name = "data")
    val data: String,
    @Json(name = "data_full")
    val dataFull: String,
    @Json(name = "hour")
    val hour: String,
    @Json(name = "local")
    val local: String
): Parcelable

//private fun getMonth(): String
//{
//    val month = arrayListOf("Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez")
//
//    return month.random()
//}

fun randomTournament(): Tournament {
    val random = Random.Default

    val brazilianLocale = Locale("pt", "BR")
    val currencyFormat = NumberFormat.getCurrencyInstance(brazilianLocale)

    val image = R.drawable.ic_wallpaper
//    val price = currencyFormat.format(random.nextFloat() * 100.0f)
    val title = "Torneio ${random.nextInt(1, 100)}" // Nome aleatório
    val month = "Jan"
    val data = buildString {
        append(random.nextInt(1, 31))
        append("\n")
        append(month)
    }
    val dataFull = buildString {
        append(random.nextInt(1, 31))
        append("/")
        append(random.nextInt(1, 12))
        append("/202")
        append(random.nextInt(3, 9))
    }
    val hour = buildString {
        append(random.nextInt(1, 23))
        append(":")
        append(random.nextInt(1, 59))
        append("\n")
        append("horas")
    }

    val local = "Rua ${random.nextInt(1, 100)}"

    return Tournament(image, title, data, dataFull, hour, local)
}
