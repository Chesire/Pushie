package com.chesire.passpusher

import android.content.ClipData
import android.content.ClipboardManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.passpusher.api.PasswordAPI
import kotlinx.coroutines.launch

/**
 * Main view model to use for the application.
 */
class MainViewModel(
    private val passwordPusher: PasswordAPI,
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
        // TODO: check password isn't blank
        _apiState.postValue(ApiState.InProgress)
        viewModelScope.launch {
            val result = passwordPusher.sendPassword(password, expiryDays, expiryViews)
            result.model?.let { model ->
                val url = passwordPusher.createPasswordUrl(model.urlToken)
                copyToClipboard(url)
                _apiState.postValue(ApiState.Success)
            } ?: _apiState.postValue(ApiState.Failure)
        }
    }

    private fun copyToClipboard(value: String) =
        clipboard.setPrimaryClip(ClipData.newPlainText("PassPusher", value))

    /**
     * Different possible states of the api request.
     */
    enum class ApiState {
        InProgress,
        Success,
        Failure
    }
}
