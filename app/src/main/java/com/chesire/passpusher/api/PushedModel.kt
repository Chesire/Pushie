package com.chesire.passpusher.api

/**
 * Model containing the result data from pushing a password using [PasswordAPI.sendPassword].
 */
data class PushedModel(
    /**
     * The URL to append to the base url of the service to retrieve the stored password.
     */
    val urlToken: String
)
