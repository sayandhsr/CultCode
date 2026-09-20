package com.unsulliedcode.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun UnsulliedCodeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    useMonospace: Boolean = true,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) Obsidian else Porcelain
    val gradients = if (darkTheme) ObsidianGradients else PorcelainGradients
    val typography = buildTypography(isLight = !darkTheme)

    CompositionLocalProvider(
        LocalAppColors provides colors,
        LocalAppGradients provides gradients,
        LocalAppTypography provides typography,
        LocalAppSpacing provides Space,
        LocalAppRadius provides Radius,
        LocalAppBorder provides Border
    ) {
        content()
    }
}
