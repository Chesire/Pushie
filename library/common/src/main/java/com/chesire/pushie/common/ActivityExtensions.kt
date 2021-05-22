package com.chesire.pushie.common

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

/**
 * Closes the soft keyboard.
 */
fun Activity.closeKeyboard() {
    currentFocus?.let {
        (getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
            ?.hideSoftInputFromWindow(it.windowToken, 0)
    }
}
