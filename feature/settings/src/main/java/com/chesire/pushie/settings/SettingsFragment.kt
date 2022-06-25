package com.chesire.pushie.settings

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
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

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = getString(R.string.preference_file_name)
        clearHistoryPreference = getString(R.string.key_clear_history)

        setPreferencesFromResource(R.xml.pref_settings, rootKey)
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
}
