package com.chesire.passpusher

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chesire.passpusher.databinding.ActivityMainBinding

private const val DAYS_PICKER_BUNDLE_KEY = "DAYS_PICKER_BUNDLE_KEY"

/**
 * The main activity for the application.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeViews(savedInstanceState)
    }

    private fun initializeViews(savedInstanceState: Bundle?) {
        binding.daysPicker.apply {
            minValue = 1
            maxValue = 90
            value = savedInstanceState?.getInt(DAYS_PICKER_BUNDLE_KEY) ?: 7
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(DAYS_PICKER_BUNDLE_KEY, binding.daysPicker.value)
        super.onSaveInstanceState(outState)
    }
}
