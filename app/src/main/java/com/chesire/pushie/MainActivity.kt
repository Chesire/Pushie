package com.chesire.pushie

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chesire.pushie.pusher.PusherFragment
import com.chesire.pushie.settings.SettingsFragment
import com.google.android.material.appbar.MaterialToolbar

/**
 * The main activity for the application.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            navigateToPusher()
        }
    }

    private fun navigateToPusher() {
        val intentPassword = parseIntent(intent)
        val pusherFragment = PusherFragment.newInstance(intentPassword)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, pusherFragment)
            .commit()

        findViewById<MaterialToolbar>(R.id.activityToolbar).apply {
            inflateMenu(R.menu.menu_pusher)
            // TODO Menu doesn't come back
            setOnMenuItemClickListener { menuItem ->
                if (menuItem.itemId == R.id.menuSettings) {
                    navigateToSettings()
                    true
                } else {
                    false
                }
            }
        }
    }

    private fun navigateToSettings() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, SettingsFragment())
            .addToBackStack("PusherFragment")
            .commit()
        findViewById<MaterialToolbar>(R.id.activityToolbar).apply {
            menu.clear()
        }
    }

    private fun parseIntent(intent: Intent): CharSequence? {
        if (intent.action != Intent.ACTION_PROCESS_TEXT) return null
        return intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)
    }
}
