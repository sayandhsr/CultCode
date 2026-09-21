package com.unsulliedcode.ui.arena

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import com.unsulliedcode.ArenaMatchmaking
import com.unsulliedcode.Home
import com.unsulliedcode.ui.navigation.AppScaffold
import com.unsulliedcode.ui.theme.LocalAppColors
import com.unsulliedcode.ui.theme.LocalAppTypography
import com.unsulliedcode.ui.theme.Space

@Composable
fun ArenaResultScreen(
    onNavigate: (NavKey) -> Unit,
    onBack: () -> Unit
) {
    val colors = LocalAppColors.current
    val typography = LocalAppTypography.current
    val isWin = true
    val xpEarned = 50
    val opponentName = "CodeNinja99"

    AppScaffold(
        title = "Match Results",
        onBack = onBack
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(Space.lg),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = if (isWin) "VICTORY!" else "DEFEAT",
                style = typography.h1,
                color = if (isWin) colors.success else colors.error,
                modifier = Modifier.padding(bottom = Space.md)
            )

            Text(
                text = "+$xpEarned XP",
                style = typography.h3,
                color = colors.xpGold,
                modifier = Modifier.padding(bottom = Space.xl)
            )

            Text(
                text = "Opponent: $opponentName",
                style = typography.body,
                color = colors.textPrimary,
                modifier = Modifier.padding(bottom = Space.xs)
            )

            Text(
                text = "Score: 85",
                style = typography.body,
                color = colors.textSecondary,
                modifier = Modifier.padding(bottom = Space.xl)
            )

            Button(
                onClick = { onNavigate(ArenaMatchmaking) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.accentPrimary,
                    contentColor = colors.textOnAccent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Space.sm)
            ) {
                Text("PLAY AGAIN", style = typography.label)
            }

            OutlinedButton(
                onClick = { onNavigate(Home) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("HOME", style = typography.label, color = colors.textPrimary)
            }
        }
    }
}
