package com.unsulliedcode.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.runtime.NavKey
import com.unsulliedcode.*
import com.unsulliedcode.ui.navigation.AppScaffold
import com.unsulliedcode.ui.theme.*
import com.unsulliedcode.ui.dashboard.*
import com.unsulliedcode.ui.gamification.*
import com.unsulliedcode.ui.components.PremiumButton
import com.unsulliedcode.data.UserProgressRepository

@Composable
fun HomeScreen(onNavigate: (NavKey) -> Unit) {
    val colors = LocalAppColors.current
    val context = LocalContext.current
    val repo = remember { UserProgressRepository(context) }
    
    val totalXp = repo.getTotalXp()
    val ucScore = repo.getUcScore()
    val badgesCount = repo.getBadges().size
    val streak = repo.getStreak()
    
    AppScaffold(
        title = "Home",
        showBackButton = false,
        onBack = {},
        actions = {
            androidx.compose.material3.IconButton(onClick = { onNavigate(com.unsulliedcode.Settings) }) {
                androidx.compose.material3.Text("⚙", color = colors.accentPrimary, fontSize = androidx.compose.ui.unit.TextUnit(20f, androidx.compose.ui.unit.TextUnitType.Sp))
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.bg)
                .padding(padding),
            contentPadding = PaddingValues(Space.md),
            verticalArrangement = Arrangement.spacedBy(Space.md)
        ) {
            item {
                XpBar(
                    currentXp = totalXp,
                    targetXp = ((totalXp / 250) + 1) * 250,
                    level = totalXp / 250
                )
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(Space.md)) {
                    StatCard(
                        label = "Badges",
                        value = badgesCount.toString(),
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        label = "Streak",
                        value = "$streak Days",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(Space.md)) {
                    PremiumButton(
                        text = "Practice",
                        onClick = { onNavigate(com.unsulliedcode.PracticeEditor("python")) },
                        modifier = Modifier.weight(1f)
                    )
                    PremiumButton(
                        text = "IDE",
                        onClick = { onNavigate(IdeSandbox) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(Space.md)) {
                    PremiumButton(
                        text = "Arena",
                        onClick = { onNavigate(ArenaMatchmaking) },
                        modifier = Modifier.weight(1f)
                    )
                    PremiumButton(
                        text = "Badges",
                        onClick = { onNavigate(GamificationBadges) },
                        modifier = Modifier.weight(1f)
                    )
                    PremiumButton(
                        text = "Settings",
                        onClick = { onNavigate(Profile) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            item {
                WeeklyActivityChart(data = listOf(0f, 2f, 5f, 1f, 8f, 3f, 4f))
            }
            item {
                DashboardGrid(
                    items = listOf(
                        "Python", "JavaScript", "Java", "Docker", "SQL", 
                        "Kubernetes", "C++", "HTML"
                    ),
                    onItemClick = { item ->
                        val langId = when (item) {
                            "Python" -> "python"
                            "JavaScript" -> "javascript"
                            "Java" -> "java"
                            "Docker" -> "docker"
                            "SQL" -> "sql"
                            "Kubernetes" -> "k8s"
                            "C++" -> "cpp"
                            "HTML" -> "html"
                            else -> item.lowercase()
                        }
                        onNavigate(CourseList(langId))
                    }
                )
            }
        }
    }
}
