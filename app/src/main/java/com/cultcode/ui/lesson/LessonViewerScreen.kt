package com.cultcode.ui.lesson

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.cultcode.Practice

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonViewerScreen(courseId: String, lessonId: String, onNavigate: (NavKey) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(courseId.replaceFirstChar { it.uppercase() }) }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { onNavigate(Practice("PY-EASY-VARIABLES-001")) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Text("Start Practice")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Python Variables", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    IconButton(onClick = { /* TTS Placeholder */ }) {
                        Text("▶")
                    }
                }
                Text("Objective: Understand how to store data in variables.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            
            item {
                Text("Explanation", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Variables are containers for storing data values. In Python, you do not need to declare a variable before using it.")
            }
            
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        "name = \"Developer\"\nscore = 100", 
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            item {
                Text("Common Mistakes", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Using a double equals sign (==) for assignment instead of a single equals sign (=).")
            }
            
            item { Spacer(modifier = Modifier.height(64.dp)) }
        }
    }
}

