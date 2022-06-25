package com.chesire.pushie.datasource.pwpush.remote

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Model containing the result data from pushing a password using [PusherApi.sendPassword].
 */
@Parcelize
data class PushedModel(
    /**
     * Timestamp of when the password was created, in format 2020-05-23T17:45:15.602Z.
     */
    val createdAt: String,

    /**
     * The URL the password can be found at.
     */
    val url: String
) : Parcelable
