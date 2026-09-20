package com.unsulliedcode.ui.gamification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.unsulliedcode.ui.navigation.AppScaffold
import com.unsulliedcode.ui.theme.*
import com.unsulliedcode.ui.components.*

@Composable
fun GamificationBadgesScreen(onBack: () -> Unit) {
    val colors = LocalAppColors.current
    val typography = LocalAppTypography.current
    AppScaffold(title = "Badges", onBack = onBack) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize().background(colors.bg).padding(padding),
            contentPadding = PaddingValues(Space.md),
            horizontalArrangement = Arrangement.spacedBy(Space.md),
            verticalArrangement = Arrangement.spacedBy(Space.md)
        ) {
            val rarities = listOf(BadgeRarity.COMMON, BadgeRarity.RARE, BadgeRarity.EPIC, BadgeRarity.LEGENDARY, BadgeRarity.MYTHIC)
            items(rarities.size) { i ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Badge(rarity = rarities[i])
                    Spacer(modifier = Modifier.height(Space.xs))
                    Text(text = rarities[i].name, style = typography.caption, color = colors.textSecondary)
                }
            }
        }
    }
}
