package com.chesire.pushie.common

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

private val dateParser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ROOT).apply {
    timeZone = TimeZone.getTimeZone("UTC")
}

fun String.toDisplayDate(): String {
    val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    return formatter.format(dateParser.parse(this))
}
