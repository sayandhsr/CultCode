package com.unsulliedcode.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import com.unsulliedcode.ui.navigation.AppScaffold
import com.unsulliedcode.ui.theme.*
import com.unsulliedcode.ui.dashboard.*
import com.unsulliedcode.ui.gamification.*
import com.unsulliedcode.PracticeEditor
import com.unsulliedcode.CourseListPython

@Composable
fun HomeScreen(onNavigate: (NavKey) -> Unit) {
    val colors = LocalAppColors.current
    AppScaffold(title = "Home", onBack = {}) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.bg)
                .padding(padding),
            contentPadding = PaddingValues(Space.md),
            verticalArrangement = Arrangement.spacedBy(Space.md)
        ) {
            item {
                XpBar(currentXp = 450, targetXp = 1000, level = 4)
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(Space.md)) {
                    StatCard(label = "Problems Solved", value = "124", modifier = Modifier.weight(1f))
                    ProgressRing(progress = 0.65f, label = "Course Completion", modifier = Modifier.weight(1f))
                }
            }
            item {
                WeeklyActivityChart(data = listOf(0f, 2f, 5f, 1f, 8f, 3f, 4f))
            }
            item {
                DashboardGrid(items = listOf("Python", "JavaScript", "Java", "Docker", "SQL", "Kubernetes"))
            }
        }
    }
}

