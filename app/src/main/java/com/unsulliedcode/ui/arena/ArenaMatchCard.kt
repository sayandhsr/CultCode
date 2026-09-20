package com.unsulliedcode.ui.arena

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.unsulliedcode.ui.theme.*

@Composable
fun ArenaMatchCard(
    opponentName: String,
    language: String,
    difficulty: String,
    onAccept: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current
    val typography = LocalAppTypography.current
    val gradients = LocalAppGradients.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .themeDepth(isLight = colors.isLight, accentColor = colors.accentPrimary)
            .background(
                brush = gradients.G01_Nebula,
                shape = RoundedCornerShape(Radius.lg)
            )
            .padding(Space.lg)
    ) {
        Column {
            Text(
                text = "⚔️ CODE ARENA",
                style = typography.overline,
                color = colors.textOnAccent
            )
            Spacer(modifier = Modifier.height(Space.sm))
            Text(
                text = "vs $opponentName",
                style = typography.h2,
                color = colors.textOnAccent
            )
            Spacer(modifier = Modifier.height(Space.xs))
            Row {
                Text(
                    text = language,
                    style = typography.label,
                    color = colors.textOnAccent.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.width(Space.md))
                Text(
                    text = difficulty,
                    style = typography.label,
                    color = when (difficulty.lowercase()) {
                        "easy" -> colors.difficultyEasy
                        "medium" -> colors.difficultyMedium
                        "hard" -> colors.difficultyHard
                        else -> colors.difficultyExpert
                    }
                )
            }
            Spacer(modifier = Modifier.height(Space.md))
            Box(
                modifier = Modifier
                    .background(colors.textOnAccent, shape = RoundedCornerShape(Radius.sm))
                    .interactive(onClick = onAccept)
                    .padding(horizontal = Space.lg, vertical = Space.sm),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "ACCEPT CHALLENGE",
                    style = typography.label,
                    color = colors.accentPrimary
                )
            }
        }
    }
}
