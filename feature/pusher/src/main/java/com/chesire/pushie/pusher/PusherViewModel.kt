package com.chesire.pushie.pusher

import android.os.Parcelable
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

private const val VIEW_KEY = "PusherViewKey"

/**
 * ViewModel to use with the [PusherFragment].
 */
class PusherViewModel(
    state: SavedStateHandle,
    private val pushInteractor: PusherInteractor,
    private val clipboardInteractor: ClipboardInteractor
) : ViewModel() {

    private val _apiState = LiveEvent<ApiResult>()

    /**
     * The result of the api request.
     */
    val apiResult: LiveData<ApiResult>
        get() = _apiState

    /**
     * The current model of the UI.
     */
    val viewState = state.getLiveData(
        VIEW_KEY,
        ViewState(
            passwordText = state.get<String>(PW_KEY) ?: "",
            expiryDays = state.get<Int>(DAYS_PICKER_BUNDLE_KEY) ?: 7,
            expiryViews = state.get<Int>(VIEWS_PICKER_BUNDLE_KEY) ?: 5,
            isLoading = false
        )
    )
    private val _viewState: ViewState
        get() = requireNotNull(viewState.value)

    /**
     * Execute an [action] on the ViewModel.
     */
    fun execute(action: Action) {
        when (action) {
            is Action.PasswordChanged -> viewState.postValue(_viewState.copy(passwordText = action.newPassword))
            is Action.ExpiryDaysChanged -> viewState.postValue(_viewState.copy(expiryDays = action.newDays))
            is Action.ExpiryViewsChanged -> viewState.postValue(_viewState.copy(expiryViews = action.newViews))
            is Action.SubmitPassword -> sendApiRequest(
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

        viewState.postValue(_viewState.copy(isLoading = true))
        viewModelScope.launch {
            pushInteractor.sendNewPassword(password, expiryDays, expiryViews)
                .onSuccess {
                    viewState.postValue(_viewState.copy(isLoading = false))
                    clipboardInteractor.copyToClipboard(it.url)
                    _apiState.postValue(ApiResult.Success)
                }
                .onFailure {
                    viewState.postValue(_viewState.copy(isLoading = false))
                    _apiState.postValue(ApiResult.Failure)
                }
        }
    }
}

/**
 * Different actions that can be used on this view model.
 */
sealed class Action {
    data class PasswordChanged(val newPassword: String) : Action()
    data class ExpiryDaysChanged(val newDays: Int) : Action()
    data class ExpiryViewsChanged(val newViews: Int) : Action()
    object SubmitPassword : Action()
}

@Parcelize
data class ViewState(
    val passwordText: String,
    val expiryDays: Int,
    val expiryViews: Int,
    val isLoading: Boolean
) : Parcelable

/**
 * Different possible states of the api request.
 */
sealed class ApiResult(@StringRes val stringId: Int) {
    object Success : ApiResult(R.string.result_success)
    object Failure : ApiResult(R.string.result_failure)
    object EmptyPassword : ApiResult(R.string.result_empty_password)
}
