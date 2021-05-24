package com.chesire.pushie.pusher

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * ViewModel to use with the [PusherFragment].
 */
class PusherViewModel(
    private val pushInteractor: PusherInteractor,
    private val clipboardInteractor: ClipboardInteractor
) : ViewModel() {

    private val _apiState = MutableLiveData<ApiState>()

    /**
     * The current state of the api request.
     */
    val apiState: LiveData<ApiState>
        get() = _apiState

    /**
     * Execute an [action] on the ViewModel.
     */
    fun execute(action: Action) {
        when (action) {
            is Action.SubmitPassword -> sendApiRequest(
                action.password,
                action.expiryDays,
                action.expiryViews
            )
        }
    }

    private fun sendApiRequest(password: String, expiryDays: Int, expiryViews: Int) {
        if (password.isBlank()) {
            _apiState.postValue(ApiState.EmptyPassword)
            return
        }

        _apiState.postValue(ApiState.InProgress)
        viewModelScope.launch {
            when (val result = pushInteractor.sendNewPassword(password, expiryDays, expiryViews)) {
                is SendPasswordResult.Success -> {
                    clipboardInteractor.copyToClipboard(result.url)
                    _apiState.postValue(ApiState.Success)
                }
                SendPasswordResult.Error -> _apiState.postValue(ApiState.Failure)
            }
        }
    }

    /**
     * Different possible states of the api request.
     */
    enum class ApiState {
        InProgress,
        Success,
        Failure,
        EmptyPassword
    }
}

/**
 * Different actions that can be used on this view model.
 */
sealed class Action {
    data class SubmitPassword(
        val password: String,
        val expiryDays: Int,
        val expiryViews: Int
    ) : Action()
}
