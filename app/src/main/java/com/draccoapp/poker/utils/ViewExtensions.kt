package com.draccoapp.poker.utils


import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.view.View
import com.google.android.material.snackbar.Snackbar

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
