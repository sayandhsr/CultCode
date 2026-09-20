package com.cultcode.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.cultcode.Practice
import com.cultcode.Ide
import com.cultcode.Profile
import com.cultcode.data.UserProgressRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onNavigate: (NavKey) -> Unit) {
    val context = LocalContext.current
    val repository = remember { UserProgressRepository(context) }
    
    val tracks = repository.getSelectedTracks()
    val totalXp = repository.getTotalXp()
    val streak = repository.getStreak()
    val isSetupComplete = repository.isOnboardingComplete()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard", fontWeight = FontWeight.Black) },
                actions = {
                    Text("UnsulliedCode ", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    IconButton(onClick = { onNavigate(Profile) }) { Text("⚙️") }
                },
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
            
            if (!isSetupComplete) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("ACTION REQUIRED", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Before going to the code, please visit Settings to configure your Skill Level, interested Programming Languages, and select a Theme.", style = MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = { onNavigate(Profile) },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error, contentColor = MaterialTheme.colorScheme.onError)
                            ) {
                                Text("Go to Settings")
                            }
                        }
                    }
                }
            }

            item {
                Text("YOUR STATS", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("XP PROGRESS", style = MaterialTheme.typography.labelSmall)
                            Text("$totalXp", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
                        }
                    }
                    Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("DAYS VISITED", style = MaterialTheme.typography.labelSmall)
                            Text("$streak", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
            
            item {
                if (tracks.isNotEmpty()) {
                    Text("INTERESTED IN", style = MaterialTheme.typography.labelSmall)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        tracks.take(4).forEach { track ->
                            Box(modifier = Modifier.border(1.dp, MaterialTheme.colorScheme.onBackground).padding(horizontal = 8.dp, vertical = 4.dp)) {
                                Text(track, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onBackground)
                            }
                        }
                    }
                }
            }

            item { HorizontalDivider(color = MaterialTheme.colorScheme.outline) }

            item {
                Card(
                    onClick = { onNavigate(Ide("Python")) },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("COMMON IDE", color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.labelMedium, )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("OPEN SANDBOX EDITOR", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            }

            item { HorizontalDivider(color = MaterialTheme.colorScheme.outline) }

            item {
                Text("PROGRAMMING LANGUAGES", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            
            item {
                val languages = listOf(
                    "Python" to "PY", "JavaScript" to "JS", "SQL" to "SQL", 
                    "Java" to "JAV", "C" to "C", "C++" to "CPP",
                    "HTML" to "HTM", "CSS" to "CSS", "TypeScript" to "TS",
                    "Docker" to "DOC", "Kubernetes" to "K8S", "YAML" to "YAM"
                )
                
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    languages.chunked(2).forEach { rowItems ->
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                            rowItems.forEach { (name, prefix) ->
                                OutlinedCard(
                                    onClick = { onNavigate(Practice("$prefix-BASICS-001")) },
                                    modifier = Modifier.weight(1f),
                                    colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.background)
                                ) {
                                    Text(
                                        text = name,
                                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
