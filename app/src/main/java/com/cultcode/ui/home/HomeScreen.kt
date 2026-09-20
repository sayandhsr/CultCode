package com.cultcode.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.cultcode.CourseList
import com.cultcode.Practice

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onNavigate: (NavKey) -> Unit) {
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
                Text("\ud83d\udd25 0 DAY STREAK", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
            
            item {
                Card(
                    onClick = { onNavigate(CourseList) },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Continue Learning", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Python", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text("Functions & Modules", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        val animatedProgress by animateFloatAsState(targetValue = 0.0f, animationSpec = tween(1000))
LinearProgressIndicator(progress = { animatedProgress }, modifier = Modifier.fillMaxWidth())
                        Text("0% complete", style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(top = 4.dp))
                    }
                }
            }

            item { Divider() }

            item {
                Text("TODAY'S CHALLENGE", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.outline)
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    onClick = { onNavigate(Practice("SQL-MEDIUM-JOINS-001")) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text("SQL • Medium", fontWeight = FontWeight.Bold)
                            Text("+50 XP", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Top employees by department")
                    }
                }
            }
            
            item { Divider() }
            
            item {
                Text("YOUR COURSES", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.outline)
                Spacer(modifier = Modifier.height(8.dp))
                CourseProgressRow("Python", 0.0f)
                CourseProgressRow("SQL", 0.0f)
                CourseProgressRow("JavaScript", 0.0f)
            }
            
            item { Divider() }
            
            item {
                Text("QUICK PRACTICE", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.outline)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(selected = false, onClick = { onNavigate(Practice("PY-BASICS-001")) }, label = { Text("Python") })
                    FilterChip(selected = false, onClick = { onNavigate(Practice("SQL-MEDIUM-JOINS-001")) }, label = { Text("SQL") })
                    FilterChip(selected = false, onClick = { onNavigate(Practice("DSA-ARRAYS-001")) }, label = { Text("DSA") })
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

