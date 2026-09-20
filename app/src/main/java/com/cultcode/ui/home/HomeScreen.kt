package com.cultcode.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.cultcode.CourseList
import com.cultcode.Practice
import com.cultcode.data.UserProgressRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onNavigate: (NavKey) -> Unit) {
    val context = LocalContext.current
    val repository = remember { UserProgressRepository(context) }
    
    val tracks = repository.getSelectedTracks()
    val totalDays = repository.getTimeCommitment()
    val level = repository.getSkillLevel()
    val primaryTrack = tracks.firstOrNull() ?: "Python"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("UNSULLIED CODE_", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("0 DAY STREAK", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    Text("$level Schedule", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            
            item {
                Card(
                    onClick = { onNavigate(CourseList) },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Day 1 of $totalDays", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(primaryTrack, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text("Core Fundamentals", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        val animatedProgress by animateFloatAsState(targetValue = 0.0f, animationSpec = tween(1000))
                        LinearProgressIndicator(progress = { animatedProgress }, modifier = Modifier.fillMaxWidth())
                        Text("0% complete", style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(top = 4.dp))
                    }
                }
            }

            item { HorizontalDivider() }

            item {
                Text("YOUR SCHEDULED COURSES", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.outline)
                Spacer(modifier = Modifier.height(8.dp))
                tracks.forEach { track ->
                    CourseProgressRow(track, 0.0f)
                }
            }
            
            item { HorizontalDivider() }
            
            item {
                Text("QUICK PRACTICE", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.outline)
                Spacer(modifier = Modifier.height(8.dp))
                // Create rows of 3 to fit all tracks neatly
                tracks.chunked(3).forEach { row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        row.forEach { track ->
                            FilterChip(
                                selected = false, 
                                onClick = { onNavigate(Practice("${track.uppercase().take(3)}-BASICS-001")) }, 
                                label = { Text(track) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CourseProgressRow(name: String, progress: Float) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(name, style = MaterialTheme.typography.bodyMedium)
        Text("${(progress * 100).toInt()}%", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
    }
}
