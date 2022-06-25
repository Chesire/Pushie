package com.chesire.pushie.pusher.core

import com.chesire.pushie.common.toDisplayDate
import com.chesire.pushie.datasource.pwpush.PWPushRepository
import com.chesire.pushie.pusher.data.PusherDomain
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapEither
import javax.inject.Inject
import kotlinx.coroutines.flow.map

/**
 * Interacts with the [PWPushRepository] to send up passwords and generate urls.
 */
class PusherInteractor @Inject constructor(private val repository: PWPushRepository) {

    /**
     * Flow of models from the repository.
     */
    val models = repository.pushedModels.map { models ->
        models.map { PusherDomain(it.createdAt.toDisplayDate(), it.url) }
    }

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
