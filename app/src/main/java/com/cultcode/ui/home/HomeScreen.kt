package com.cultcode.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
    val ucScore = repository.getUcScore()
    val isSetupComplete = repository.isOnboardingComplete()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ANALYTICS DASHBOARD", fontWeight = FontWeight.Black) },
                actions = {
                    Text("UnsulliedCode ", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    IconButton(onClick = { onNavigate(Profile) }) { Text("⚙️") }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            
            item {
                Text("SKILL METRICS", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    MetricCard(title = "TOTAL XP", value = "$totalXp", modifier = Modifier.weight(1f))
                    MetricCard(title = "UC SCORE", value = "$ucScore", modifier = Modifier.weight(1f))
                    MetricCard(title = "STREAK", value = "${streak}d", modifier = Modifier.weight(1f))
                }
            }

            item {
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant), modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("MASTERY BREAKDOWN", style = MaterialTheme.typography.labelSmall)
                        Spacer(modifier = Modifier.height(16.dp))
                        tracks.take(4).forEach { track ->
                            val progress = (0..100).random() / 100f
                            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text(track, modifier = Modifier.weight(0.3f), style = MaterialTheme.typography.bodySmall)
                                LinearProgressIndicator(progress = { progress }, modifier = Modifier.weight(0.7f).height(8.dp).clip(RoundedCornerShape(4.dp)))
                            }
                        }
                    }
                }
            }

            item { HorizontalDivider() }

            item {
                Card(
                    onClick = { onNavigate(Ide("Python")) },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("UNIVERSAL IDE / PLAYGROUND", color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.labelMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("OPEN SANDBOX", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            }

            item { HorizontalDivider() }

            item {
                Text("TECHNOLOGY MATRIX", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            
            item {
                val languages = listOf(
                    "Python" to "PY", "JavaScript" to "JS", "SQL" to "SQL", 
                    "Java" to "JAV", "C" to "C", "C++" to "CPP",
                    "HTML" to "HTM", "CSS" to "CSS", "TypeScript" to "TS",
                    "Docker" to "DOC", "Kubernetes" to "K8S", "YAML" to "YAM",
                    "NumPy" to "NUM", "Pandas" to "PAN", "MongoDB" to "MON"
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

@Composable
fun MetricCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
        Column(modifier = Modifier.padding(12.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(title, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
        }
    }
}
