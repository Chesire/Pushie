package com.chesire.pushie.api

/**
 * Provides methods for pushing passwords up to an API.
 */
interface PasswordAPI {

    /**
     * Creates the full url to be copied to the clipboard, using the [token] where appropriate.
     */
    fun createPasswordUrl(token: String): String

    /**
     * Sends [password] up to the API.
     * The expiry time in days can be configured by passing a value to [expiryDays].
     * The expiry time by how many views can be configured by passing a value to [expiryViews].
     */
    suspend fun sendPassword(
        password: String,
        expiryDays: Int,
        expiryViews: Int
    ): SendPasswordResult

    /**
     * Data class containing the result data for when executing [sendPassword].
     */
    data class SendPasswordResult(val statusCode: Int, val model: PushedModel?)
}
