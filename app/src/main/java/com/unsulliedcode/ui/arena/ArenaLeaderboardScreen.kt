package com.unsulliedcode.ui.arena

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.unsulliedcode.ui.navigation.AppScaffold
import com.unsulliedcode.ui.theme.LocalAppColors
import com.unsulliedcode.ui.theme.LocalAppTypography
import com.unsulliedcode.ui.theme.Space
import com.unsulliedcode.ui.theme.Radius

data class LeaderboardEntry(val name: String, val score: Int, val winRate: String, val isCurrentUser: Boolean = false)

@Composable
fun ArenaLeaderboardScreen(
    onBack: () -> Unit
) {
    val colors = LocalAppColors.current
    val typography = LocalAppTypography.current
    
    val entries = listOf(
        LeaderboardEntry("KotlinKing", 9500, "78%"),
        LeaderboardEntry("CodeNinja99", 8200, "65%"),
        LeaderboardEntry("You", 7500, "60%", isCurrentUser = true),
        LeaderboardEntry("JavaMaster", 6000, "55%"),
        LeaderboardEntry("BugHunter", 5400, "50%")
    )

    AppScaffold(
        title = "Leaderboard",
        onBack = onBack
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(Space.md)
        ) {
            itemsIndexed(entries) { index, entry ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = Space.sm)
                        .background(
                            if (entry.isCurrentUser) colors.accentMuted else colors.surface,
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(Radius.sm)
                        )
                        .padding(Space.md),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "#${index + 1}",
                        style = typography.label,
                        color = colors.textPrimary,
                        modifier = Modifier.weight(0.2f)
                    )
                    Text(
                        text = entry.name,
                        style = typography.body,
                        color = if (entry.isCurrentUser) colors.accentPrimary else colors.textPrimary,
                        modifier = Modifier.weight(0.4f)
                    )
                    Text(
                        text = "${entry.score}",
                        style = typography.body,
                        color = colors.textSecondary,
                        modifier = Modifier.weight(0.2f)
                    )
                    Text(
                        text = entry.winRate,
                        style = typography.body,
                        color = colors.textSecondary,
                        modifier = Modifier.weight(0.2f)
                    )
                }
            }
        }
    }
}
