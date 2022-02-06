package com.chesire.pushie.pusher

import com.chesire.pushie.datasource.pwpush.PWPushRepository
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapEither

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
    ): Result<SendPasswordResult.Success, SendPasswordResult.Error> {
        return repository.sendPassword(password, expiryDays, expiryViews)
            .mapEither(
                success = { SendPasswordResult.Success(it.url) },
                failure = { SendPasswordResult.Error }
            )
    }
}

/**
 * Result object for using [PusherInteractor.sendNewPassword].
 */
sealed class SendPasswordResult {
    data class Success(val url: String) : SendPasswordResult()
    object Error : SendPasswordResult()
}
