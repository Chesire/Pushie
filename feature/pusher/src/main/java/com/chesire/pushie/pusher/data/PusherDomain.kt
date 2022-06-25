package com.chesire.pushie.pusher.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PusherDomain(
    /**
     * Formatted timestamp of when the password was created, ready for display.
     */
    val createdAt: String,

    /**
     * The URL the password can be found at.
     */
    val url: String
) : Parcelable
