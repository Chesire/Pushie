package com.chesire.passpusher

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
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
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(passwordApi) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeViews(savedInstanceState)
        initializeViewModel()
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
        binding.sendButton.setOnClickListener {
            viewModel.sendApiRequest(
                binding.passwordEditText.text.toString(),
                binding.daysPicker.value,
                binding.viewsPicker.value
            )
        }
    }

    private fun initializeViewModel() {
        viewModel.apiState.observe(this, Observer { state ->
            when (state) {
                MainViewModel.ApiState.InProgress -> {
                    binding.sendButton.visibility = View.INVISIBLE
                    binding.sendProgress.visibility = View.VISIBLE
                }
                MainViewModel.ApiState.Complete -> {
                    binding.sendButton.visibility = View.VISIBLE
                    binding.sendProgress.visibility = View.INVISIBLE
                }
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(DAYS_PICKER_BUNDLE_KEY, binding.daysPicker.value)
        outState.putInt(VIEWS_PICKER_BUNDLE_KEY, binding.viewsPicker.value)
        super.onSaveInstanceState(outState)
    }
}
