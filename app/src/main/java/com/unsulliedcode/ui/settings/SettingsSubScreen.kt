package com.unsulliedcode.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.unsulliedcode.ui.navigation.AppScaffold
import com.unsulliedcode.ui.theme.*

@Composable
fun SettingsSubScreen(title: String, onBack: () -> Unit) {
    val colors = LocalAppColors.current
    val typography = LocalAppTypography.current

    AppScaffold(title = title, onBack = onBack) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.bg)
                .padding(padding)
                .padding(Space.md)
        ) {
            Text(
                text = "Preferences for $title are stored securely offline.",
                style = typography.body,
                color = colors.textSecondary
            )
            // Switches and UI will go here depending on the screen
        }
    }
}
