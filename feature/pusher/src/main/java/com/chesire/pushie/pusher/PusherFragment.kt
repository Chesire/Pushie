package com.chesire.pushie.pusher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.chesire.pushie.compose.PushieTheme
import com.chesire.pushie.pusher.ui.PusherScreen
import com.chesire.pushie.pusher.ui.PusherViewModel
import com.chesire.pushie.pusher.ui.ViewAction
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment for the main screen of the application, that allows users to use the api service.
 */
@AndroidEntryPoint
class PusherFragment : Fragment() {

    private val viewModel by viewModels<PusherViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setContent {
            PushieTheme {
                PusherScreen(
                    viewState = viewModel.viewState.collectAsState(),
                    onPasswordChanged = { viewModel.execute(ViewAction.PasswordChanged(it)) },
                    onExpiryDaysChanged = { viewModel.execute(ViewAction.ExpiryDaysChanged(it)) },
                    onExpiryViewsChanged = { viewModel.execute(ViewAction.ExpiryViewsChanged(it)) },
                    onPreviousModelPressed = { viewModel.execute(ViewAction.PreviousModelPressed(it)) },
                    onSendClicked = { viewModel.execute(ViewAction.SubmitPassword) }
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ideally this would be handled in Compose, but would be better if this screen also
        // contained the toolbar.
        viewModel.apiResult.observe(viewLifecycleOwner) { state ->
            Snackbar.make(view, state.stringId, Snackbar.LENGTH_LONG).show()
        }
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
