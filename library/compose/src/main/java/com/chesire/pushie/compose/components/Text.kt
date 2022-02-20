package com.chesire.pushie.compose.components

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun PushieText(text: String) {
    Text(
        text = text,
        color = MaterialTheme.colors.primary
    )
}
