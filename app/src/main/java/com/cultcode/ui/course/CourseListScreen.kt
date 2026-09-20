package com.cultcode.ui.course

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.cultcode.LessonViewer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseListScreen(onNavigate: (NavKey) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Courses") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { CourseCategory("Programming") }
            item { CourseCard("Python", "From Zero to Advanced", 0.0f, onNavigate) }
            item { CourseCard("JavaScript", "Modern Web Development", 0.0f, onNavigate) }
            
            item { CourseCategory("DevOps") }
            item { CourseCard("Docker", "Containerization Fundamentals", 0.0f, onNavigate) }
            item { CourseCard("Kubernetes", "Container Orchestration", 0.0f, onNavigate) }
            
            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@Composable
fun CourseCategory(title: String) {
    Text(
        text = title.uppercase(),
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
    )
}

@Composable
fun CourseCard(name: String, description: String, progress: Float, onNavigate: (NavKey) -> Unit) {
    Card(
        onClick = { onNavigate(LessonViewer(name.lowercase(), "lesson-1")) },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                LinearProgressIndicator(progress = { progress }, modifier = Modifier.weight(1f).padding(end = 16.dp, top = 8.dp))
                Text("${(progress * 100).toInt()}%", style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

