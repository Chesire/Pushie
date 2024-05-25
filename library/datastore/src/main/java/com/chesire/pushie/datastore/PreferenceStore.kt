package com.chesire.pushie.datastore

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PreferenceStore @Inject constructor(@ApplicationContext context: Context) {
    private val preferences = context.getSharedPreferences(
        context.getString(R.string.preference_file_name),
        MODE_PRIVATE
    )
    private val _pushieUrlModel = PreferenceStringModel(
        context.getString(R.string.key_default_url),
        context.getString(R.string.default_default_url)
    )
    private val _defaultDaysTillExpiry = PreferenceIntModel(
        context.getString(R.string.key_default_days),
        context.resources.getInteger(R.integer.default_default_days)
    )
    private val _defaultViewsTillExpiry = PreferenceIntModel(
        context.getString(R.string.key_default_views),
        context.resources.getInteger(R.integer.default_default_views)
    )

    val pushieUrl: String
        get() = preferences.getString(_pushieUrlModel)

    val defaultDaysTillExpiry: Int
        get() = preferences.getInt(_defaultDaysTillExpiry)

    val defaultViewsTillExpiry: Int
        get() = preferences.getInt(_defaultViewsTillExpiry)

    private fun SharedPreferences.getString(model: PreferenceStringModel): String =
        getString(model.key, model.defaultValue) ?: model.defaultValue

    private fun SharedPreferences.getInt(model: PreferenceIntModel): Int =
        getString(model.key, model.defaultValue.toString())?.toIntOrNull() ?: model.defaultValue
}

private data class PreferenceStringModel(val key: String, val defaultValue: String)
private data class PreferenceIntModel(val key: String, val defaultValue: Int)
