package com.chesire.pushie

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
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
            handleNavigateEvents()
            navigateToPusher()
        }
    }

    private fun handleNavigateEvents() {
        supportFragmentManager.registerFragmentLifecycleCallbacks(
            object : FragmentManager.FragmentLifecycleCallbacks() {
                override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
                    when (f) {
                        is PusherFragment -> setupPusherMenu()
                        is SettingsFragment -> setupSettingsMenu()
                    }
                    super.onFragmentStarted(fm, f)
                }
            },
            true
        )
    }

    private fun navigateToPusher() {
        val intentPassword = parseIntent(intent)
        val pusherFragment = PusherFragment.newInstance(intentPassword)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, pusherFragment)
            .commit()
    }

    private fun setupPusherMenu() {
        findViewById<MaterialToolbar>(R.id.activityToolbar).apply {
            menu.clear()
            inflateMenu(R.menu.menu_pusher)
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
    }

    private fun setupSettingsMenu() {
        findViewById<MaterialToolbar>(R.id.activityToolbar).menu.clear()
    }

    private fun parseIntent(intent: Intent): CharSequence? {
        if (intent.action != Intent.ACTION_PROCESS_TEXT) return null
        return intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)
    }
}
