package com.unsulliedcode.ui.settings

import com.unsulliedcode.ui.theme.Space
import com.unsulliedcode.ui.theme.Radius
import com.unsulliedcode.ui.theme.Border
import com.unsulliedcode.ui.theme.LocalAppColors
import com.unsulliedcode.ui.theme.LocalAppTypography

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
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.unsulliedcode.data.UserProgressRepository
import com.unsulliedcode.LocalThemeUpdater

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val repository = remember { UserProgressRepository(context) }
    
    val userName by remember { mutableStateOf(repository.getName()) }
    var skillLevel by remember { mutableStateOf(repository.getSkillLevel()) }
    var tracks by remember { mutableStateOf(repository.getSelectedTracks()) }
    val timeCommitment by remember { mutableStateOf(repository.getTimeCommitment()) }
    
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
            repository.saveOnboardingPreferences(userName, skillLevel, timeCommitment, tracks)
            repository.setDarkTheme(isDark)
            repository.setMonospace(isMono)
            themeUpdater()
            onBack()
        }
    }

    val colors = LocalAppColors.current
    val typography = LocalAppTypography.current

    if (isLoading) {
        Surface(modifier = Modifier.fillMaxSize(), color = colors.bg) {
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(color = colors.accentPrimary)
                Spacer(modifier = Modifier.height(Space.lg))
                Text(loadingText, style = typography.body, color = colors.textPrimary)
            }
        }
        return
    }

    Scaffold(
        containerColor = colors.bg,
        topBar = {
            TopAppBar(
                title = { Text("Profile & Settings", color = colors.textPrimary, style = typography.h2) },
                navigationIcon = { IconButton(onClick = onBack) { Text("<", color = colors.textPrimary) } },
                actions = { Text("UnsulliedCode ", fontWeight = FontWeight.Bold, color = colors.accentPrimary) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colors.surface)
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { isLoading = true },
                containerColor = colors.accentPrimary,
                contentColor = colors.textOnAccent
            ) { Text("Save Preferences") }
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(Space.md).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(Space.md)
        ) {
            Text("Appearance", style = typography.h3, fontWeight = FontWeight.Bold, color = colors.textPrimary)
            Row(horizontalArrangement = Arrangement.spacedBy(Space.md)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(checked = isDark, onCheckedChange = { isDark = it })
                    Spacer(modifier = Modifier.width(Space.sm))
                    Text("Dark Theme", color = colors.textPrimary, style = typography.body)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(checked = isMono, onCheckedChange = { isMono = it })
                    Spacer(modifier = Modifier.width(Space.sm))
                    Text("Monospace", color = colors.textPrimary, style = typography.body)
                }
            }
            
            HorizontalDivider(color = colors.border)

            Text("Skill Level", style = typography.h3, fontWeight = FontWeight.Bold, color = colors.textPrimary)
            Row(horizontalArrangement = Arrangement.spacedBy(Space.sm)) {
                listOf("Beginner", "Intermediate", "Advanced").forEach { level ->
                    FilterChip(
                        selected = skillLevel == level,
                        onClick = { skillLevel = level },
                        label = { Text(level, color = colors.textPrimary) },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = colors.surface,
                            selectedContainerColor = colors.surfaceHover
                        )
                    )
                }
            }

            HorizontalDivider(color = colors.border)

            Text("Interested Languages", style = typography.h3, fontWeight = FontWeight.Bold, color = colors.textPrimary)
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
                                label = { Text(track, color = colors.textPrimary) },
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = colors.surface,
                                    selectedContainerColor = colors.surfaceHover
                                )
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(Space.md)) // padding for FAB
        }
    }
}
