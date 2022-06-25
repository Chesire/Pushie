package com.chesire.pushie.common

import android.content.Context
import android.text.format.DateFormat
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

/**
 * Provides methods to format a date string for display.
 */
class DateFormatter @Inject constructor(@ApplicationContext private val context: Context) {
    private val dateParser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ROOT).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    private val dateFormatter: SimpleDateFormat
        get() {
            val dateFormat = DateFormat.getDateFormat(context) as SimpleDateFormat
            return SimpleDateFormat("${dateFormat.toLocalizedPattern()} HH:MM", Locale.getDefault())
        }

    /**
     * Formats the [value] into a date string based on the users date preference. If the [value]
     * cannot be parsed then an empty string is returned.
     */
    fun toDisplayDate(value: String): String {
        return try {
            dateParser.parse(value)?.let { dateFormatter.format(it) } ?: ""
        } catch (ex: ParseException) {
            ""
        }
    }
}
