package com.chesire.pushie.settings.controls

import android.content.Context
import android.util.AttributeSet
import androidx.preference.EditTextPreference

class NotEmptyEditTextPreference(
    context: Context,
    attrs: AttributeSet
) : EditTextPreference(context, attrs) {

    override fun setText(text: String?) {
        if (!text.isNullOrBlank()) {
            super.setText(text)
        }
    }
}
