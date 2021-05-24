package com.chesire.pushie.pusher

import com.chesire.pushie.datasource.pwpush.PWPushRepository
import com.chesire.pushie.datasource.pwpush.remote.ApiResult

/**
 * Interacts with the [PWPushRepository] to send up passwords and generate urls.
 */
class PusherInteractor(private val repository: PWPushRepository) {

    /**
     * Sends a new password to the API, and returns a [SendPasswordResult].
     */
    suspend fun sendNewPassword(
        password: String,
        expiryDays: Int,
        expiryViews: Int
    ): SendPasswordResult {
        return when (val sendResult = repository.sendPassword(password, expiryDays, expiryViews)) {
            is ApiResult.Success -> SendPasswordResult.Success(sendResult.model.url)
            is ApiResult.Error -> SendPasswordResult.Error
            is ApiResult.ExceptionalError -> SendPasswordResult.Error
        }
    }
}

/**
 * Result object for using [PusherInteractor.sendNewPassword].
 */
sealed class SendPasswordResult {
    data class Success(val url: String) : SendPasswordResult()
    object Error : SendPasswordResult()
}
