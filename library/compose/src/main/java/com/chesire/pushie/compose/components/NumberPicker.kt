package com.chesire.pushie.compose.components

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import com.chargemap.compose.numberpicker.NumberPicker

@Composable
fun PushieNumberPicker(value: Int, range: Iterable<Int>) {
    var valueState by remember { mutableStateOf(value) }

    NumberPicker(
        value = valueState,
        range = range,
        onValueChange = { valueState = it },
        textStyle = TextStyle.Default.copy(color = MaterialTheme.colors.primary)
    )
}
