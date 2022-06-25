package com.chesire.pushie.pusher.ui

import android.os.Parcelable
import com.chesire.pushie.datasource.pwpush.remote.PushedModel
import kotlinx.parcelize.Parcelize

/**
 * The current state of the view to be rendered.
 */
@Parcelize
data class ViewState(
    val passwordText: String,
    val expiryDays: Int,
    val expiryViews: Int,
    val isLoading: Boolean,
    val previousModels: List<PushedModel>
) : Parcelable
