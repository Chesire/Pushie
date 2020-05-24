package com.chesire.passpusher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.passpusher.api.PasswordAPI
import kotlinx.coroutines.launch

/**
 * Main view model to use for the application.
 */
class MainViewModel(private val passwordPusher: PasswordAPI) : ViewModel() {
    /**
     * Send the input details up to the API
     */
    fun sendApiRequest(password: String, expiryDays: Int, expiryViews: Int) {
        // check password isn't blank
        viewModelScope.launch {
            val result = passwordPusher.sendPassword(password, expiryDays, expiryViews)
            val t = ""
        }
    }
}
