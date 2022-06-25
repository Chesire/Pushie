package com.chesire.pushie.pusher.ui

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.pushie.common.getStateFlow
import com.chesire.pushie.datasource.pwpush.remote.PushedModel
import com.chesire.pushie.pusher.DAYS_PICKER_BUNDLE_KEY
import com.chesire.pushie.pusher.PW_KEY
import com.chesire.pushie.pusher.R
import com.chesire.pushie.pusher.VIEWS_PICKER_BUNDLE_KEY
import com.chesire.pushie.pusher.core.ClipboardInteractor
import com.chesire.pushie.pusher.core.PusherInteractor
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import com.hadilq.liveevent.LiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

private const val STATE_KEY = "PUSHER_STATE_KEY"

/**
 * ViewModel to use with the [PusherFragment].
 */
@HiltViewModel
class PusherViewModel @Inject constructor(
    state: SavedStateHandle,
    private val pushInteractor: PusherInteractor,
    private val clipboardInteractor: ClipboardInteractor
) : ViewModel() {

    private val _apiState = LiveEvent<ApiResult>()

    /**
     * The result of the api request.
     */
    val apiResult: LiveData<ApiResult> get() = _apiState

    /**
     * The current model of the UI.
     */
    val viewState = state.getStateFlow(
        STATE_KEY,
        viewModelScope,
        ViewState(
            passwordText = state.get<String>(PW_KEY) ?: "",
            expiryDays = state.get<Int>(DAYS_PICKER_BUNDLE_KEY) ?: 7,
            expiryViews = state.get<Int>(VIEWS_PICKER_BUNDLE_KEY) ?: 5,
            isLoading = false,
            previousModels = emptyList()
        )
    )
    private val _viewState: ViewState
        get() = requireNotNull(viewState.value)

    init {
        viewModelScope.launch {
            pushInteractor.models.collect {
                viewState.value = _viewState.copy(previousModels = it)
            }
        }
    }

    /**
     * Execute an [viewAction] on the ViewModel.
     */
    fun execute(viewAction: ViewAction) {
        when (viewAction) {
            is ViewAction.PasswordChanged -> {
                viewState.value = _viewState.copy(passwordText = viewAction.newPassword)
            }
            is ViewAction.ExpiryDaysChanged -> {
                viewState.value = _viewState.copy(expiryDays = viewAction.newDays)
            }
            is ViewAction.ExpiryViewsChanged -> {
                viewState.value = _viewState.copy(expiryViews = viewAction.newViews)
            }
            is ViewAction.PushedModelPressed -> handlePushedModelPressed(viewAction.model)
            is ViewAction.SubmitPassword -> sendApiRequest(
                _viewState.passwordText,
                _viewState.expiryDays,
                _viewState.expiryViews
            )
        }
    }

    private fun sendApiRequest(password: String, expiryDays: Int, expiryViews: Int) {
        if (password.isBlank()) {
            _apiState.postValue(ApiResult.EmptyPassword)
            return
        }

        viewState.value = _viewState.copy(isLoading = true)
        viewModelScope.launch {
            pushInteractor.sendNewPassword(password, expiryDays, expiryViews)
                .onSuccess {
                    viewState.value = _viewState.copy(isLoading = false)
                    clipboardInteractor.copyToClipboard(it.url)
                    _apiState.postValue(ApiResult.Success)
                }
                .onFailure {
                    viewState.value = _viewState.copy(isLoading = false)
                    _apiState.postValue(ApiResult.Failure)
                }
        }
    }

    private fun handlePushedModelPressed(model: PushedModel) {
        clipboardInteractor.copyToClipboard(model.url)
        _apiState.postValue(ApiResult.Success)
    }
}

/**
 * Different possible states of the api request.
 */
sealed class ApiResult(@StringRes val stringId: Int) {
    object Success : ApiResult(R.string.result_success)
    object Failure : ApiResult(R.string.result_failure)
    object EmptyPassword : ApiResult(R.string.result_empty_password)
}
