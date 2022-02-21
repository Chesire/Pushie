package com.chesire.pushie.pusher

import android.os.Parcelable
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
    private val state: SavedStateHandle,
    private val pushInteractor: PusherInteractor,
    private val clipboardInteractor: ClipboardInteractor
) : ViewModel() {

    private val _apiState = LiveEvent<ApiState>()

    /**
     * The current state of the api request.
     */
    val apiState: LiveData<ApiState>
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
            is Action.PasswordChanged -> {
                val newData = _viewState.copy(passwordText = action.newPassword)
                state.set(VIEW_KEY, newData)
            }
            is Action.ExpiryDaysChanged -> {
                val newData = _viewState.copy(expiryDays = action.newDays)
                state.set(VIEW_KEY, newData)
            }
            is Action.ExpiryViewsChanged -> {
                val newData = _viewState.copy(expiryViews = action.newViews)
                state.set(VIEW_KEY, newData)
            }
            is Action.SubmitPassword -> sendApiRequest(
                _viewState.passwordText,
                _viewState.expiryDays,
                _viewState.expiryViews
            )
        }
    }

    private fun sendApiRequest(password: String, expiryDays: Int, expiryViews: Int) {
        if (password.isBlank()) {
            // TODO: Post error somehow
            // _apiState.postValue(ApiState.EmptyPassword)
            return
        }

        viewState.postValue(_viewState.copy(isLoading = true))
        viewModelScope.launch {
            pushInteractor.sendNewPassword(password, expiryDays, expiryViews)
                .onSuccess {
                    viewState.postValue(_viewState.copy(isLoading = false))
                    clipboardInteractor.copyToClipboard(it.url)
                    // TODO: Post success somehow
                    // _apiState.postValue(ApiState.Success)
                }
                .onFailure {
                    viewState.postValue(_viewState.copy(isLoading = false))
                    // TODO: Post error somehow
                    // _apiState.postValue(ApiState.Failure)
                }
        }
    }

    /**
     * Different possible states of the api request.
     */
    enum class ApiState {
        Success,
        Failure,
        EmptyPassword
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
