package com.chesire.pushie.compose

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

object PushieTheme {
    // private val DarkGrey = Color(0xFF171717)
    private val Yellow = Color(0xFFFEFE53)
    private val Test = Color.Green

    internal val DarkColors = darkColors(
        primary = Yellow,
        primaryVariant = Yellow,
        // secondary = x,
        // secondaryVariant = x,
        // background = x,
        // surface = x,
        // error = x,
        onPrimary = Color.White,
        // onSecondary = x,
        // onBackground = x,
        onSurface = Yellow,
        onError = Test
    )
    internal val LightColors = lightColors()
}

@Composable
fun PushieTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider {
        // val colours = if (isSystemInDarkTheme()) PushieTheme.DarkColors else PushieTheme.LightColors
        MaterialTheme(
            colors = PushieTheme.DarkColors,
            typography = MaterialTheme.typography,
            shapes = MaterialTheme.shapes,
            content = content
        )
    }
}
