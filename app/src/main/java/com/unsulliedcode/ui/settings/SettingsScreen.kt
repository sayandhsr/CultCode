package com.unsulliedcode.ui.settings
import com.unsulliedcode.ui.theme.Space
import com.unsulliedcode.ui.theme.Radius
import com.unsulliedcode.ui.theme.Border

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.unsulliedcode.data.UserProgressRepository
import com.unsulliedcode.LocalThemeUpdater

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val repository = remember { UserProgressRepository(context) }
    
    var skillLevel by remember { mutableStateOf(repository.getSkillLevel()) }
    var tracks by remember { mutableStateOf(repository.getSelectedTracks()) }
    
    var isDark by remember { mutableStateOf(repository.isDarkTheme()) }
    var isMono by remember { mutableStateOf(repository.isMonospace()) }
    val themeUpdater = LocalThemeUpdater.current
    
    val ucScore = repository.getUcScore()
    var earnedBadges = repository.getBadges()
    
    if (earnedBadges.size >= 5 && !earnedBadges.contains("Elite")) {
        repository.addBadge("Elite")
        earnedBadges = repository.getBadges()
    }

    var isLoading by remember { mutableStateOf(false) }
    var loadingText by remember { mutableStateOf("Saving preferences...") }

    LaunchedEffect(isLoading) {
        if (isLoading) {
            delay(1000)
            repository.saveOnboardingPreferences("User", skillLevel, 30, tracks)
            repository.setDarkTheme(isDark)
            repository.setMonospace(isMono)
            themeUpdater()
            onBack()
        }
    }

    if (isLoading) {
        Surface(modifier = Modifier.fillMaxSize(), color = com.unsulliedcode.ui.theme.LocalAppColors.current.bg) {
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(color = com.unsulliedcode.ui.theme.LocalAppColors.current.accentPrimary)
                Spacer(modifier = Modifier.height(Space.lg))
                Text(loadingText, style = com.unsulliedcode.ui.theme.LocalAppTypography.current.body)
            }
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile & Settings") },
                navigationIcon = { IconButton(onClick = onBack) { Text("<") } },
                actions = { Text("UnsulliedCode ", fontWeight = FontWeight.Bold, color = com.unsulliedcode.ui.theme.LocalAppColors.current.accentPrimary) }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = { isLoading = true }) { Text("Save Preferences") }
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(Space.md).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(Space.md)
        ) {
            Card(colors = CardDefaults.cardColors(containerColor = com.unsulliedcode.ui.theme.LocalAppColors.current.surfaceElevated), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(Space.md), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("UC SCORE", style = com.unsulliedcode.ui.theme.LocalAppTypography.current.label)
                    Text("$ucScore", style = com.unsulliedcode.ui.theme.LocalAppTypography.current.h1, fontWeight = FontWeight.Black)
                }
            }

            Text("Your Badges", style = com.unsulliedcode.ui.theme.LocalAppTypography.current.h3, fontWeight = FontWeight.Bold)
            val allBadges = listOf("First Login", "Python Advanced", "SQL Advanced", "101 Badge", "Elite")
            Row(horizontalArrangement = Arrangement.spacedBy(Space.sm), modifier = Modifier.fillMaxWidth()) {
                allBadges.forEach { badgeName ->
                    val isEarned = earnedBadges.contains(badgeName)
                    val isElite = badgeName == "Elite"
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.weight(1f).aspectRatio(1f).alpha(if (isEarned) 1f else 0.3f)
                            .background(
                                color = if (isElite && isEarned) com.unsulliedcode.ui.theme.LocalAppColors.current.textInverse else if (isEarned) com.unsulliedcode.ui.theme.LocalAppColors.current.accentPrimary else com.unsulliedcode.ui.theme.LocalAppColors.current.surfaceHover,
                                shape = CircleShape
                            )
                    ) {
                        Text(
                            text = badgeName.replace(" ", "\n"),
                            color = if (isElite && isEarned) com.unsulliedcode.ui.theme.LocalAppColors.current.textPrimary else if (isEarned) com.unsulliedcode.ui.theme.LocalAppColors.current.textOnAccent else com.unsulliedcode.ui.theme.LocalAppColors.current.textTertiary,
                            style = com.unsulliedcode.ui.theme.LocalAppTypography.current.label,
                            modifier = Modifier.padding(Space.sm), textAlign = TextAlign.Center, lineHeight = 14.sp
                        )
                    }
                }
            }

            HorizontalDivider()

            Text("Appearance", style = com.unsulliedcode.ui.theme.LocalAppTypography.current.h3, fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(Space.md)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(checked = isDark, onCheckedChange = { isDark = it })
                    Spacer(modifier = Modifier.width(Space.sm))
                    Text("Dark Theme")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(checked = isMono, onCheckedChange = { isMono = it })
                    Spacer(modifier = Modifier.width(Space.sm))
                    Text("Monospace")
                }
            }
            
            HorizontalDivider()

            Text("Skill Level", style = com.unsulliedcode.ui.theme.LocalAppTypography.current.h3, fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(Space.sm)) {
                listOf("Beginner", "Intermediate", "Advanced").forEach { level ->
                    FilterChip(selected = skillLevel == level, onClick = { skillLevel = level }, label = { Text(level) })
                }
            }

            HorizontalDivider()

            Text("Interested Languages", style = com.unsulliedcode.ui.theme.LocalAppTypography.current.h3, fontWeight = FontWeight.Bold)
            val allTracks = listOf("Python", "JavaScript", "SQL", "Java", "C", "C++", "HTML", "CSS", "TypeScript", "Docker", "Kubernetes", "YAML")
            
            Column(verticalArrangement = Arrangement.spacedBy(Space.sm)) {
                allTracks.chunked(3).forEach { rowTracks ->
                    Row(horizontalArrangement = Arrangement.spacedBy(Space.sm)) {
                        rowTracks.forEach { track ->
                            FilterChip(
                                selected = tracks.contains(track),
                                onClick = {
                                    val newSet = tracks.toMutableSet()
                                    if (newSet.contains(track)) newSet.remove(track) else newSet.add(track)
                                    tracks = newSet
                                },
                                label = { Text(track) }
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(Space.md)) // padding for FAB
        }
    }
}



