package com.cultcode.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import com.cultcode.data.UserProgressRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val repository = remember { UserProgressRepository(context) }
    
    var skillLevel by remember { mutableStateOf(repository.getSkillLevel()) }
    var days by remember { mutableStateOf(repository.getTimeCommitment()) }
    var tracks by remember { mutableStateOf(repository.getSelectedTracks()) }
    
    var isLoading by remember { mutableStateOf(false) }
    var loadingText by remember { mutableStateOf("Analyzing skill level...") }

    LaunchedEffect(isLoading) {
        if (isLoading) {
            loadingText = "Analyzing skill level..."
            delay(1500)
            loadingText = "Generating customized $skillLevel curriculum..."
            delay(2000)
            loadingText = "Building $days-day study plan..."
            delay(2000)
            repository.saveOnboardingPreferences(skillLevel, days, tracks)
            onBack()
        }
    }

    if (isLoading) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(24.dp))
                Text(loadingText, style = MaterialTheme.typography.bodyLarge)
            }
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Update Goals") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Text("<") }
                },
                actions = {
                    Text("UnsulliedCode ", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { isLoading = true }
            ) {
                Text("Save Preferences")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Skill Level", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Beginner", "Intermediate", "Advanced").forEach { level ->
                    FilterChip(
                        selected = skillLevel == level,
                        onClick = { skillLevel = level },
                        label = { Text(level) }
                    )
                }
            }

            HorizontalDivider()

            Text("Time Commitment", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(15, 30, 60).forEach { d ->
                    FilterChip(
                        selected = days == d,
                        onClick = { days = d },
                        label = { Text("$d Days") }
                    )
                }
            }

            HorizontalDivider()

            Text("Learning Tracks", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            val allTracks = listOf("Python", "Java", "C++", "SQL", "JavaScript", "TypeScript", "HTML/CSS", "Pandas/EDA", "Kubernetes", "Docker", "YAML")
            allTracks.chunked(3).forEach { row ->
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    row.forEach { track ->
                        FilterChip(
                            selected = tracks.contains(track),
                            onClick = { 
                                tracks = if (tracks.contains(track)) tracks - track else tracks + track
                            },
                            label = { Text(track) }
                        )
                    }
                }
            }
        }
    }
}
