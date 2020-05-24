package com.chesire.passpusher

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chesire.passpusher.api.PasswordPusher
import com.chesire.passpusher.databinding.ActivityMainBinding
import okhttp3.OkHttpClient

private const val DAYS_PICKER_BUNDLE_KEY = "DAYS_PICKER_BUNDLE_KEY"
private const val VIEWS_PICKER_BUNDLE_KEY = "VIEWS_PICKER_BUNDLE_KEY"

/**
 * The main activity for the application.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels {
        object : ViewModelProvider.Factory {
            private val okHttpClient = OkHttpClient()
            private val passwordApi = PasswordPusher(okHttpClient)

            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    MainViewModel(passwordApi) as T
                } else {
                    throw IllegalArgumentException("ViewModel Not Found")
                }
            }
        }
    }

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
        binding.viewsPicker.apply {
            minValue = 1
            maxValue = 100
            value = savedInstanceState?.getInt(VIEWS_PICKER_BUNDLE_KEY) ?: 5
        }
        binding.press.setOnClickListener {
            viewModel.sendApiRequest(
                binding.passwordEditText.text.toString(),
                binding.daysPicker.value,
                binding.viewsPicker.value
            )
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(DAYS_PICKER_BUNDLE_KEY, binding.daysPicker.value)
        outState.putInt(VIEWS_PICKER_BUNDLE_KEY, binding.viewsPicker.value)
        super.onSaveInstanceState(outState)
    }
}
