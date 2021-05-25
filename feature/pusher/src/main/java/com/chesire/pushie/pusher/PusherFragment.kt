package com.chesire.pushie.pusher

import android.content.ClipboardManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chesire.pushie.common.closeKeyboard
import com.chesire.pushie.datasource.pwpush.PWPushRepository
import com.chesire.pushie.datasource.pwpush.remote.PusherApi
import com.chesire.pushie.pusher.databinding.FragmentPusherBinding
import com.google.android.material.snackbar.Snackbar
import okhttp3.OkHttpClient

private const val DAYS_PICKER_BUNDLE_KEY = "DAYS_PICKER_BUNDLE_KEY"
private const val VIEWS_PICKER_BUNDLE_KEY = "VIEWS_PICKER_BUNDLE_KEY"

/**
 * Fragment for the main screen of the application, that allows users to use the api service.
 */
class PusherFragment : Fragment(R.layout.fragment_pusher) {

    private var _binding: FragmentPusherBinding? = null
    private val binding: FragmentPusherBinding
        get() = requireNotNull(_binding)

    private val viewModel: PusherViewModel by viewModels {
        object : ViewModelProvider.Factory {
            private val okHttpClient = OkHttpClient()
            private val pusherApi = PusherApi(okHttpClient)
            private val pusherRepository = PWPushRepository(pusherApi)
            private val pusherInteractor = PusherInteractor(pusherRepository)
            private val clipboard = getSystemService(
                requireContext(),
                ClipboardManager::class.java
            ) as ClipboardManager
            private val clipboardInteractor = ClipboardInteractor(clipboard)

            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return PusherViewModel(pusherInteractor, clipboardInteractor) as T
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentPusherBinding.inflate(inflater).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeMenu()
        initializeViews(savedInstanceState)
        initializeViewModel()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(DAYS_PICKER_BUNDLE_KEY, binding.daysPicker.value)
        outState.putInt(VIEWS_PICKER_BUNDLE_KEY, binding.viewsPicker.value)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun initializeMenu() {
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == R.id.menuSettings) {
                Snackbar.make(binding.root, "Test", Snackbar.LENGTH_LONG).show()
                true
            } else {
                false
            }
        }
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
        binding.passwordEditText.setText(arguments?.getCharSequence(PW_KEY))
        binding.sendButton.setOnClickListener {
            viewModel.execute(
                Action.SubmitPassword(
                    binding.passwordEditText.text.toString(),
                    binding.daysPicker.value,
                    binding.viewsPicker.value
                )
            )

            activity?.closeKeyboard()
        }
    }

    private fun initializeViewModel() {
        viewModel.apiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                PusherViewModel.ApiState.InProgress -> setLoadingIndicatorState(true)
                PusherViewModel.ApiState.Success -> {
                    setLoadingIndicatorState(false)
                    Snackbar
                        .make(binding.root, R.string.result_success, Snackbar.LENGTH_LONG)
                        .show()
                }
                PusherViewModel.ApiState.Failure -> {
                    setLoadingIndicatorState(false)
                    Snackbar
                        .make(binding.root, R.string.result_failure, Snackbar.LENGTH_LONG)
                        .show()
                }
                PusherViewModel.ApiState.EmptyPassword -> {
                    setLoadingIndicatorState(false)
                    Snackbar
                        .make(binding.root, R.string.result_empty_password, Snackbar.LENGTH_LONG)
                        .show()
                }
            }
        }
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

    companion object {
        private const val PW_KEY = "PW_KEY"

        /**
         * Creates a new instance of the fragment.
         * Pass [password] to have it pre-populated in the password edit text.
         */
        fun newInstance(password: CharSequence?): PusherFragment {
            return PusherFragment().apply {
                arguments = bundleOf(PW_KEY to password)
            }
        }
    }
}
