package com.chesire.pushie.datasource.pwpush

import com.chesire.pushie.datasource.pwpush.remote.ApiResult
import com.chesire.pushie.datasource.pwpush.remote.PusherApi

class PWPushRepository(private val passwordAPI: PusherApi) {

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

