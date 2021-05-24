package com.chesire.pushie

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chesire.pushie.pusher.PusherFragment

/**
 * The main activity for the application.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val intentPassword = parseIntent(intent)
            val startFragment = PusherFragment.newInstance(intentPassword)
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, startFragment)
                .commit()
        }
    }

    private fun parseIntent(intent: Intent): CharSequence? {
        if (intent.action != Intent.ACTION_PROCESS_TEXT) return null
        return intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)
    }
}
