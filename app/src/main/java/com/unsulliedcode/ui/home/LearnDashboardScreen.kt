package com.unsulliedcode.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.unsulliedcode.ui.navigation.AppScaffold
import com.unsulliedcode.ui.theme.*
import com.unsulliedcode.ui.dashboard.*
import com.unsulliedcode.ui.gamification.*
import com.unsulliedcode.data.UserProgressRepository

@Composable
fun LearnDashboardScreen(onBack: () -> Unit) {
    val colors = LocalAppColors.current
    val context = LocalContext.current
    val repo = remember { UserProgressRepository(context) }
    
    val totalXp = repo.getTotalXp()
    val ucScore = repo.getUcScore()
    
    AppScaffold(title = "Learn Dashboard", onBack = onBack) { padding ->
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
                        label = "Problems Solved",
                        value = (ucScore / 10).toString(),
                        modifier = Modifier.weight(1f)
                    )
                    ProgressRing(
                        progress = (totalXp % 250) / 250f,
                        label = "Course Completion",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            item {
                WeeklyActivityChart(data = listOf(0f, 2f, 5f, 1f, 8f, 3f, 4f))
            }
        }
    }
}
