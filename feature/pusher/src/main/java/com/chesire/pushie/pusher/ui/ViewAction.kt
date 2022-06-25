package com.chesire.pushie.pusher.ui

import com.chesire.pushie.pusher.data.PusherDomain

/**
 * Different actions that can be used on the [PusherViewModel].
 */
sealed class ViewAction {
    data class PasswordChanged(val newPassword: String) : ViewAction()
    data class ExpiryDaysChanged(val newDays: Int) : ViewAction()
    data class ExpiryViewsChanged(val newViews: Int) : ViewAction()
    data class PreviousModelPressed(val domain: PusherDomain) : ViewAction()
    object SubmitPassword : ViewAction()
}
