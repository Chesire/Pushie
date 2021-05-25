package com.chesire.pushie.settings.controls

import android.content.Context
import android.util.AttributeSet
import androidx.preference.EditTextPreference
import com.chesire.pushie.settings.R

class NotEmptyEditTextPreference(
    context: Context,
    attrs: AttributeSet
) : EditTextPreference(context, attrs) {

    init {
        setOnBindEditTextListener { editText ->
            editText.hint = context.getString(R.string.default_default_url)
        }
    }

    override fun setText(text: String?) {
        if (text.isNullOrBlank()) {
            super.setText(context.getString(R.string.default_default_url))
        } else {
            super.setText(text)
        }
    }
}
