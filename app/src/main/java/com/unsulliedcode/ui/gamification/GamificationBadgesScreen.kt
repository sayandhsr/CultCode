package com.unsulliedcode.ui.gamification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import com.unsulliedcode.data.UserProgressRepository
import com.unsulliedcode.ui.navigation.AppScaffold
import com.unsulliedcode.ui.theme.*
import com.unsulliedcode.ui.components.*

data class BadgeDefinition(
    val name: String,
    val rarity: BadgeRarity,
    val condition: String
)

val allBadgeDefinitions = listOf(
    BadgeDefinition("First Login", BadgeRarity.COMMON, "Log in for the first time"),
    BadgeDefinition("101 Badge", BadgeRarity.COMMON, "Complete your first lesson"),
    BadgeDefinition("Python Advanced", BadgeRarity.RARE, "Complete Python track"),
    BadgeDefinition("SQL Advanced", BadgeRarity.RARE, "Complete SQL track"),
    BadgeDefinition("Elite", BadgeRarity.LEGENDARY, "Earn 5 other badges")
)

@Composable
fun GamificationBadgesScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val repository = remember { UserProgressRepository(context) }
    val earnedBadges = remember { repository.getBadges() }
    
    val colors = LocalAppColors.current
    val typography = LocalAppTypography.current
    
    AppScaffold(title = "Badges", onBack = onBack) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize().background(colors.bg).padding(padding),
            contentPadding = PaddingValues(Space.md),
            horizontalArrangement = Arrangement.spacedBy(Space.md),
            verticalArrangement = Arrangement.spacedBy(Space.md)
        ) {
            items(allBadgeDefinitions.size) { i ->
                val badgeDef = allBadgeDefinitions[i]
                val isEarned = earnedBadges.contains(badgeDef.name)
                
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.alpha(if (isEarned) 1f else 0.5f)
                ) {
                    Badge(rarity = badgeDef.rarity)
                    Spacer(modifier = Modifier.height(Space.xs))
                    Text(
                        text = badgeDef.name, 
                        style = typography.caption, 
                        color = colors.textPrimary,
                        textAlign = TextAlign.Center
                    )
                    if (!isEarned) {
                        Spacer(modifier = Modifier.height(Space.xs))
                        Text(
                            text = "Locked: ${badgeDef.condition}", 
                            style = typography.caption, 
                            color = colors.textSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
