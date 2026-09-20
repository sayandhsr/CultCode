package com.unsulliedcode.ui.arena

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.unsulliedcode.ui.navigation.AppScaffold
import com.unsulliedcode.ui.theme.*
import androidx.navigation3.runtime.NavKey
import com.unsulliedcode.ArenaBattle

@Composable
fun ArenaMatchmakingScreen(onNavigate: (NavKey) -> Unit, onBack: () -> Unit) {
    val colors = LocalAppColors.current
    AppScaffold(title = "Code Arena Matchmaking", onBack = onBack) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.bg)
                .padding(padding)
                .padding(Space.md),
            verticalArrangement = Arrangement.spacedBy(Space.md)
        ) {
            ArenaMatchCard(
                opponentName = "AlgorithmKing",
                language = "Python",
                difficulty = "Hard",
                onAccept = { onNavigate(ArenaBattle) }
            )
            ArenaMatchCard(
                opponentName = "SyntaxError99",
                language = "JavaScript",
                difficulty = "Medium",
                onAccept = { onNavigate(ArenaBattle) }
            )
        }
    }
}
