package com.chesire.pushie.pusher

import android.content.ClipData
import android.content.ClipboardManager

/**
 * Interacts with the Clipboard to push new values into it.
 */
class ClipboardInteractor(private val clipboard: ClipboardManager) {

    /**
     * Copies [value] directly into the Android clipboard.
     */
    fun copyToClipboard(value: String) = clipboard.setPrimaryClip(
        ClipData.newPlainText("Pushie", value)
    )
}
