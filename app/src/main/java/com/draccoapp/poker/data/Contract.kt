package com.draccoapp.poker.data

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import java.text.NumberFormat
import java.util.Locale
import kotlin.random.Random

@JsonClass(generateAdapter = true)
@Parcelize
data class Contract(
    @Json(name = "title")
    val title: String,
    @Json(name = "value")
    val valor: String,
): Parcelable

fun randomContract(): Contract{
        val random = Random.Default

        val brazilianLocale = Locale("pt", "BR")
        val currencyFormat = NumberFormat.getCurrencyInstance(brazilianLocale)

        val title = "Torneio ${random.nextInt(1, 100)}"
        val value = currencyFormat.format(random.nextFloat() * 10000.0f)

        return Contract(title, value)
}
