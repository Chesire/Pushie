package com.chesire.pushie.datasource.pwpush.remote

/**
 * Model containing the result data from pushing a password using [PasswordAPI.sendPassword].
 */
data class PushedModel(
    /**
     * Unique ID of the password that was sent up.
     */
    val id: Int,
    /**
     * The URL to append to the base url of the service to retrieve the stored password.
     */
    val urlToken: String,
    /**
     * Timestamp of when the password was created, in format 2020-05-23T17:45:15.602Z.
     */
    val createdAt: String
)
