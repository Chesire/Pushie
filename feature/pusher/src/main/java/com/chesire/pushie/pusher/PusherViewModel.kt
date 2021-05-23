package com.chesire.pushie.pusher

import android.content.ClipData
import android.content.ClipboardManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PusherViewModel(
    private val pushInteractor: PusherInteractor,
    private val clipboard: ClipboardManager
) : ViewModel() {

    private val _apiState = MutableLiveData<ApiState>()

    /**
     * The current state of the api request.
     */
    val apiState: LiveData<ApiState>
        get() = _apiState

    /**
     * Send the current details up to the api, result will be synced along the [apiState] live data.
     */
    fun sendApiRequest(password: String, expiryDays: Int, expiryViews: Int) {
        if (password.isBlank()) {
            _apiState.postValue(ApiState.EmptyPassword)
            return
        }

        _apiState.postValue(ApiState.InProgress)
        viewModelScope.launch {
            when (val result = pushInteractor.sendNewPassword(password, expiryDays, expiryViews)) {
                is SendPasswordResult.Success -> {
                    copyToClipboard(result.url)
                    _apiState.postValue(ApiState.Success)
                }
                SendPasswordResult.Error -> _apiState.postValue(ApiState.Failure)
            }
        }
    }

    private fun copyToClipboard(value: String) =
        clipboard.setPrimaryClip(ClipData.newPlainText("Pushie", value))

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
