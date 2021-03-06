package com.chesire.pushie.datastore

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class PreferenceStore(context: Context) {
    private val preferences = context.getSharedPreferences(
        context.getString(R.string.preference_file_name),
        MODE_PRIVATE
    )
    private val _pushieUrlModel = PreferenceModel(
        context.getString(R.string.key_default_url),
        context.getString(R.string.default_default_url)
    )

    val pushieUrl: String
        get() = preferences.getPreference(_pushieUrlModel)

    private fun SharedPreferences.getPreference(model: PreferenceModel): String =
        getString(model.key, model.defaultValue) ?: model.defaultValue
}

private data class PreferenceModel(val key: String, val defaultValue: String)
