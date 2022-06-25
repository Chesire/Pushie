package com.chesire.pushie.pusher.ui

import com.chesire.pushie.datasource.pwpush.remote.PushedModel

/**
 * Different actions that can be used on the [PusherViewModel].
 */
sealed class ViewAction {
    data class PasswordChanged(val newPassword: String) : ViewAction()
    data class ExpiryDaysChanged(val newDays: Int) : ViewAction()
    data class ExpiryViewsChanged(val newViews: Int) : ViewAction()
    data class PushedModelPressed(val model: PushedModel) : ViewAction()
    object SubmitPassword : ViewAction()
}
