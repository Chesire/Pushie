package com.chesire.pushie.datasource.pwpush

import com.chesire.pushie.common.ApiError
import com.chesire.pushie.datasource.pwpush.local.dao.PushedDao
import com.chesire.pushie.datasource.pwpush.local.entity.PushedEntity
import com.chesire.pushie.datasource.pwpush.remote.PushedModel
import com.chesire.pushie.datasource.pwpush.remote.PusherApi
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onSuccess
import java.util.UUID
import javax.inject.Inject

/**
 * Repository to interact with the [PusherApi] remote data source, and to interact with any local
 * data sources that store urls.
 */
class PWPushRepository @Inject constructor(
    private val pusherApi: PusherApi,
    private val pushedDao: PushedDao
) {

    /**
     * Sends the [password] up to the API.
     */
    suspend fun sendPassword(
        password: String,
        expiryDays: Int,
        expiryViews: Int
    ): Result<PushedModel, ApiError> {
        return pusherApi
            .sendPassword(password, expiryDays, expiryViews)
            .onSuccess {
                pushedDao.insert(
                    PushedEntity(
                        UUID.randomUUID().toString(),
                        it.url,
                        it.createdAt
                    )
                )
            }
    }
}
