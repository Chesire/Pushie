package com.chesire.pushie.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat

/**
 * Fragment allowing the user to change various application settings.
 */
class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = getString(R.string.preference_file_name)
        setPreferencesFromResource(R.xml.pref_settings, rootKey)
    }
}
