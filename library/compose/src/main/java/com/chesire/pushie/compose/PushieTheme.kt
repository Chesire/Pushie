package com.chesire.pushie.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

object PushieTheme {
    internal val DarkColors = darkColors(
        primary = PushieColors.Yellow,
        primaryVariant = PushieColors.Yellow,
        // secondary = x,
        // secondaryVariant = x,
        background = PushieColors.DarkGrey,
        surface = PushieColors.DarkGrey,
        // error = x,
        onPrimary = Color.Black,
        // onSecondary = x,
        onBackground = PushieColors.Yellow,
        onSurface = PushieColors.Yellow,
        onError = PushieColors.Test
    )
    internal val LightColors = DarkColors // TODO: Implement a light theme
}

@Composable
fun PushieTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider {
        val colours = if (isSystemInDarkTheme()) PushieTheme.DarkColors else PushieTheme.LightColors

        MaterialTheme(
            colors = colours,
            typography = MaterialTheme.typography,
            shapes = MaterialTheme.shapes,
            content = content
        )
    }
}
