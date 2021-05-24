package com.chesire.pushie.datasource.pwpush

import com.chesire.pushie.datasource.pwpush.remote.ApiResult
import com.chesire.pushie.datasource.pwpush.remote.PusherApi

/**
 * Repository to interact with the [PusherApi] remote data source, and to interact with any local
 * data sources that store urls.
 */
class PWPushRepository(private val passwordAPI: PusherApi) {

    /**
     * Sends the [password] up to the API, returning an [ApiResult] in response.
     */
    suspend fun sendPassword(
        password: String,
        expiryDays: Int,
        expiryViews: Int
    ): ApiResult {
        val result = passwordAPI.sendPassword(password, expiryDays, expiryViews)
        // TODO: store in local db, with a flow available to return these urls
        return result
    }
}

