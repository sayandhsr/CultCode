package com.cultcode.ui.onboarding

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

@Composable
fun OnboardingScreen(onComplete: () -> Unit) {
    val context = LocalContext.current
    val repository = remember { UserProgressRepository(context) }
    
    var step by remember { mutableStateOf(0) }
    var userName by remember { mutableStateOf("") }
    var skillLevel by remember { mutableStateOf("Beginner") }
    var days by remember { mutableStateOf(15) }
    var tracks by remember { mutableStateOf(setOf<String>()) }
    
    // Loading State
    var isLoading by remember { mutableStateOf(false) }
    var loadingText by remember { mutableStateOf("Scanning question repository for modules...") }

    LaunchedEffect(isLoading) {
        if (isLoading) {
            loadingText = "Scanning question repository for modules..."
            delay(2000)
            loadingText = "Building personalized study roadmap..."
            delay(2500)
            loadingText = "Optimizing local IDE runtime environments..."
            delay(2500)
            repository.saveOnboardingPreferences(skillLevel, days, tracks)
            // also we can save userName to repository, but we need to update it first
            onComplete()
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

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (step) {
                0 -> {
                    Text("WELCOME TO UNSULLIED CODE", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(32.dp))
                    OutlinedTextField(
                        value = userName,
                        onValueChange = { userName = it },
                        label = { Text("What is your name?") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = { if(userName.isNotBlank()) step = 1 },
                        modifier = Modifier.fillMaxWidth().height(56.dp)
                    ) {
                        Text("CONTINUE")
                    }
                }
                1 -> {
                    Text("YOUR SKILL LEVEL", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(32.dp))
                    listOf("Beginner", "Intermediate", "Advanced").forEach { level ->
                        OutlinedButton(
                            onClick = { skillLevel = level; step = 2 },
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).height(56.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (skillLevel == level) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.background
                            )
                        ) {
                            Text(level, color = MaterialTheme.colorScheme.onBackground)
                        }
                    }
                }
                2 -> {
                    Text("LEARNING TRACKS", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Text("Select at least one", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(16.dp))
                    val allTracks = listOf("Python", "Java", "C++", "SQL", "JavaScript", "TypeScript", "HTML/CSS", "Pandas/EDA", "Kubernetes", "Docker", "YAML")
                    allTracks.chunked(2).forEach { row ->
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            row.forEach { track ->
                                FilterChip(
                                    selected = tracks.contains(track),
                                    onClick = { 
                                        tracks = if (tracks.contains(track)) tracks - track else tracks + track
                                    },
                                    label = { Text(track) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = { step = 3 },
                        enabled = tracks.isNotEmpty(),
                        modifier = Modifier.fillMaxWidth().height(56.dp)
                    ) {
                        Text("NEXT")
                    }
                }
                3 -> {
                    Text("TIME COMMITMENT", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(32.dp))
                    listOf(15, 30, 60).forEach { d ->
                        OutlinedButton(
                            onClick = { days = d },
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).height(56.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (days == d) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.background
                            )
                        ) {
                            Text("$d Days", color = MaterialTheme.colorScheme.onBackground)
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = { isLoading = true },
                        modifier = Modifier.fillMaxWidth().height(56.dp)
                    ) {
                        Text("GENERATE SCHEDULE")
                    }
                }
            }
        }
    }
}
