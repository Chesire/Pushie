package com.chesire.pushie.settings

import android.os.Bundle
import android.text.InputType
import androidx.lifecycle.lifecycleScope
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.chesire.pushie.datasource.pwpush.local.dao.PushedDao
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Fragment allowing the user to change various application settings.
 */
@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    @Inject
    lateinit var pushedDao: PushedDao

    private lateinit var clearHistoryPreference: String
    private lateinit var defaultDaysPreference: String
    private lateinit var defaultViewsPreference: String

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = getString(R.string.preference_file_name)
        clearHistoryPreference = getString(R.string.key_clear_history)
        defaultDaysPreference = getString(R.string.key_default_days)
        defaultViewsPreference = getString(R.string.key_default_views)

        setPreferencesFromResource(R.xml.pref_settings, rootKey)

        setupDefaultPreference(defaultDaysPreference, 90)
        setupDefaultPreference(defaultViewsPreference, 100)
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        return when (preference.key) {
            clearHistoryPreference -> {
                lifecycleScope.launchWhenCreated {
                    pushedDao.destroyTable()
                }
                showClearHistorySnackbar()
                true
            }

            else -> super.onPreferenceTreeClick(preference)
        }
    }

    private fun showClearHistorySnackbar() {
        view?.let { view ->
            Snackbar.make(
                view,
                R.string.settings_clear_history_finished,
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    private fun setupDefaultPreference(preferenceValue: String, maxValue: Int) {
        preferenceManager.findPreference<EditTextPreference>(preferenceValue)?.let { pref ->
            pref.setOnBindEditTextListener {
                it.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
            }
            pref.setOnPreferenceChangeListener { _, newValue ->
                val new = newValue.toString().toIntOrNull()
                if (new == null) {
                    false
                } else if (new < 1 || new > maxValue) {
                    false
                } else {
                    true
                }
            }
        }
    }
}
