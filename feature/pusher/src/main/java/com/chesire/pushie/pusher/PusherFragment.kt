package com.chesire.pushie.pusher

import android.content.ClipboardManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.chesire.pushie.compose.PushieTheme
import com.chesire.pushie.datasource.pwpush.PWPushRepository
import com.chesire.pushie.datasource.pwpush.remote.PusherApi
import com.chesire.pushie.datastore.PreferenceStore
import okhttp3.OkHttpClient

/**
 * Fragment for the main screen of the application, that allows users to use the api service.
 */
class PusherFragment : Fragment(R.layout.fragment_pusher) {

    inner class PusherViewModelFactory(
        owner: SavedStateRegistryOwner,
        defaultArgs: Bundle? = null
    ) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
        // TODO: Use Hilt instead here
        private val okHttpClient = OkHttpClient()
        private val preferenceStore = PreferenceStore(requireContext().applicationContext)
        private val pusherApi = PusherApi(okHttpClient, preferenceStore)
        private val pusherRepository = PWPushRepository(pusherApi)
        private val pusherInteractor = PusherInteractor(pusherRepository)
        private val clipboard = getSystemService(
            requireContext(),
            ClipboardManager::class.java
        ) as ClipboardManager
        private val clipboardInteractor = ClipboardInteractor(clipboard)

        override fun <T : ViewModel> create(
            key: String,
            modelClass: Class<T>,
            handle: SavedStateHandle
        ): T {
            @Suppress("UNCHECKED_CAST")
            return PusherViewModel(handle, pusherInteractor, clipboardInteractor) as T
        }
    }

    private val viewModel: PusherViewModel by viewModels { PusherViewModelFactory(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setContent {
            PushieTheme {
                PusherScreen(
                    viewState = viewModel.viewState.observeAsState(),
                    onPasswordChanged = { viewModel.execute(Action.PasswordChanged(it)) },
                    onExpiryDaysChanged = { viewModel.execute(Action.ExpiryDaysChanged(it)) },
                    onExpiryViewsChanged = { viewModel.execute(Action.ExpiryViewsChanged(it)) },
                    onSendClicked = {
                        viewModel.execute(Action.SubmitPassword("", 0, 0))
                        // TODO: Close keyboard?
                    }
                )
            }
        }
    }

    private fun initializeViewModel() {
        viewModel.apiState.observe(viewLifecycleOwner) { state ->
            //when (state) {
            //    PusherViewModel.ApiState.InProgress -> setLoadingIndicatorState(true)
            //    PusherViewModel.ApiState.Success -> {
            //        setLoadingIndicatorState(false)
            //        Snackbar
            //            .make(binding.root, R.string.result_success, Snackbar.LENGTH_LONG)
            //            .show()
            //    }
            //    PusherViewModel.ApiState.Failure -> {
            //        setLoadingIndicatorState(false)
            //        Snackbar
            //            .make(binding.root, R.string.result_failure, Snackbar.LENGTH_LONG)
            //            .show()
            //    }
            //    PusherViewModel.ApiState.EmptyPassword -> {
            //        setLoadingIndicatorState(false)
            //        Snackbar
            //            .make(binding.root, R.string.result_empty_password, Snackbar.LENGTH_LONG)
            //            .show()
            //    }
            //}
        }
    }

    private fun setLoadingIndicatorState(visible: Boolean) {
        //if (visible) {
        //    binding.sendButton.visibility = View.INVISIBLE
        //    binding.sendProgress.visibility = View.VISIBLE
        //} else {
        //    binding.sendButton.visibility = View.VISIBLE
        //    binding.sendProgress.visibility = View.INVISIBLE
        //}
    }

    companion object {

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
