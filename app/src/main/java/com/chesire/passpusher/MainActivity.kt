package com.chesire.passpusher

import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chesire.passpusher.api.PasswordPusher
import com.chesire.passpusher.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
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
            private val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(passwordApi, clipboard) as T
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
            closeKeyboard()
        }
    }

    private fun initializeViewModel() {
        viewModel.apiState.observe(
            this,
            Observer { state ->
                when (state) {
                    MainViewModel.ApiState.InProgress -> setLoadingIndicatorState(true)
                    MainViewModel.ApiState.Success -> {
                        setLoadingIndicatorState(false)
                        Snackbar
                            .make(binding.root, R.string.result_success, Snackbar.LENGTH_LONG)
                            .show()
                    }
                    MainViewModel.ApiState.Failure -> {
                        setLoadingIndicatorState(false)
                        Snackbar
                            .make(binding.root, R.string.result_failure, Snackbar.LENGTH_LONG)
                            .show()
                    }
                }
            }
        )
    }

    private fun setLoadingIndicatorState(visible: Boolean) {
        if (visible) {
            binding.sendButton.visibility = View.INVISIBLE
            binding.sendProgress.visibility = View.VISIBLE
        } else {
            binding.sendButton.visibility = View.VISIBLE
            binding.sendProgress.visibility = View.INVISIBLE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(DAYS_PICKER_BUNDLE_KEY, binding.daysPicker.value)
        outState.putInt(VIEWS_PICKER_BUNDLE_KEY, binding.viewsPicker.value)
        super.onSaveInstanceState(outState)
    }

    private fun closeKeyboard() {
        currentFocus?.let {
            (getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
                ?.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
}
