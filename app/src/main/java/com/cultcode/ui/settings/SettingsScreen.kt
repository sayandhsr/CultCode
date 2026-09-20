package com.cultcode.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import com.cultcode.data.UserProgressRepository
import com.cultcode.LocalThemeUpdater

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val repository = remember { UserProgressRepository(context) }
    
    var skillLevel by remember { mutableStateOf(repository.getSkillLevel()) }
    var days by remember { mutableStateOf(repository.getTimeCommitment()) }
    var tracks by remember { mutableStateOf(repository.getSelectedTracks()) }
    
    var isDark by remember { mutableStateOf(repository.isDarkTheme()) }
    var isMono by remember { mutableStateOf(repository.isMonospace()) }
    val themeUpdater = LocalThemeUpdater.current
    
    val ucScore = repository.getUcScore()
    var earnedBadges = repository.getBadges()
    
    // Automatically award Elite if > 5 badges
    if (earnedBadges.size >= 5 && !earnedBadges.contains("Elite")) {
        repository.addBadge("Elite")
        earnedBadges = repository.getBadges()
    }

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
            repository.setDarkTheme(isDark)
            repository.setMonospace(isMono)
            themeUpdater()
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
                title = { Text("Profile & Settings") },
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
            // UC SCORE
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("UC SCORE", style = MaterialTheme.typography.labelMedium)
                    Text("$ucScore", style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.Black)
                }
            }

            // BADGES
            Text("Your Badges", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            val allBadges = listOf("First Login", "Python Advanced", "SQL Advanced", "101 Badge", "Elite")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                allBadges.forEach { badgeName ->
                    val isEarned = earnedBadges.contains(badgeName)
                    val isElite = badgeName == "Elite"
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .alpha(if (isEarned) 1f else 0.3f)
                            .background(
                                color = if (isElite && isEarned) Color.Black else if (isEarned) MaterialTheme.colorScheme.primary else Color.Gray,
                                shape = RoundedCornerShape(8.dp)
                            )
                    ) {
                        Text(
                            text = badgeName.replace(" ", "\n"),
                            color = if (isElite && isEarned) Color.White else if (isEarned) MaterialTheme.colorScheme.onPrimary else Color.DarkGray,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }
            }

            HorizontalDivider()

            Text("Appearance", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(checked = isDark, onCheckedChange = { isDark = it })
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Dark Theme")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(checked = isMono, onCheckedChange = { isMono = it })
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Monospace")
                }
            }
            
            HorizontalDivider()

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
        }
    }
}
