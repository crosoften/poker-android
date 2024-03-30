package com.draccoapp.poker.extensions


import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.View
import com.draccoapp.poker.utils.Preferences
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.TimeZone

fun Context.getCharset(text: String): String {
    return String(text.toByteArray(Charsets.ISO_8859_1), Charsets.UTF_8)
}

fun String.changeOrder(date: String): String {
    val part = date.split("-")
    if (part.size == 3) {
        val year = part[0]
        val month = part[1]
        val day = part[2]
        return "$day-$month-$year"
    }
    return date
}

fun View.viewVisible() {
    this.visibility = View.VISIBLE
}

fun View.viewInvisible() {
    this.visibility = View.INVISIBLE
}

fun View.viewGone() {
    this.visibility = View.GONE
}

fun View.showSnackBarRed(message: String) {
    val snackBar = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    snackBar.setBackgroundTint(Color.RED)
    snackBar.show()
}

fun View.showSnackBar(message: String) {
    val snackBar = Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
    snackBar.show()
}

fun View.snackBarWithAction(
    message: String, actionLabel: String,
    block: () -> Unit
) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG)
        .setAction(actionLabel) {
            block()
        }
        .show()
}

fun Context.copyToClipboard(text: CharSequence) {
    val clipboard = getSystemService(ClipboardManager::class.java)
    val clip = ClipData.newPlainText("label", text)
    clipboard.setPrimaryClip(clip)
}



//fun String.isoToBrFormat(): String {
//    val isoFormatter = DateTimeFormatter.ISO_DATE_TIME
//    val brFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
//    val date = LocalDate.parse(this, isoFormatter)
//    return date.format(brFormatter)
//}


//fun String.isoToDateTimeFormat(context: Context): String {
//
//    val preferences = Preferences(context)
//    val isoFormatter = DateTimeFormatter.ISO_DATE_TIME
//    val locale = Locale(preferences.getLanguage(), preferences.getCountry())
//    val localFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss", locale)
//    val date = LocalDateTime.parse(this, isoFormatter)
//    val localDate = date.atZone(ZoneId.of("UTC")).toLocalDateTime()
//    return localDate.format(localFormatter)
//}

fun Context.openWebPage(url: String) {
    val webpage: Uri = Uri.parse(url)
    val intent = Intent(Intent.ACTION_VIEW, webpage)
    startActivity(intent)
}

fun Context.getPreferenceData(): Preferences {
    return Preferences(this)
}
