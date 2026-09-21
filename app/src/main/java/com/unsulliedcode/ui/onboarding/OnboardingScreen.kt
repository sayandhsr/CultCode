package com.unsulliedcode.ui.onboarding
import com.unsulliedcode.ui.theme.Space
import com.unsulliedcode.ui.theme.Radius
import com.unsulliedcode.ui.theme.Border
import com.unsulliedcode.ui.theme.LocalAppColors
import com.unsulliedcode.ui.theme.LocalAppTypography

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import kotlinx.coroutines.delay
import com.unsulliedcode.data.UserProgressRepository

@Composable
fun OnboardingScreen(onComplete: () -> Unit) {
    val context = LocalContext.current
    val repository = remember { UserProgressRepository(context) }
    val colors = LocalAppColors.current
    val typography = LocalAppTypography.current
    
    var step by remember { mutableStateOf(0) }
    var userName by remember { mutableStateOf("") }
    var skillLevel by remember { mutableStateOf("Beginner") }
    var days by remember { mutableStateOf(15) }
    val tracks = setOf<String>() // Tracks are no longer selected in onboarding
    
    // Loading State
    var isLoading by remember { mutableStateOf(false) }
    var loadingText by remember { mutableStateOf("Building personalized study roadmap...") }

    LaunchedEffect(isLoading) {
        if (isLoading) {
            loadingText = "Building personalized study roadmap..."
            delay(2500)
            loadingText = "Optimizing local IDE runtime environments..."
            delay(2500)
            repository.saveOnboardingPreferences(userName, skillLevel, days, tracks)
            onComplete()
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize().background(colors.bg)) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(color = colors.accentPrimary, trackColor = colors.surfaceInset)
                Spacer(modifier = Modifier.height(Space.lg))
                Text(loadingText, style = typography.body, color = colors.textPrimary)
            }
        }
        return
    }

    Box(modifier = Modifier.fillMaxSize().background(colors.bg)) {
        Column(
            modifier = Modifier.fillMaxSize().padding(Space.lg),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (step) {
                0 -> {
                    Text("WELCOME TO UNSULLIED CODE", style = typography.h2, fontWeight = FontWeight.Bold, color = colors.accentPrimary)
                    Spacer(modifier = Modifier.height(Space.xl))
                    OutlinedTextField(
                        value = userName,
                        onValueChange = { userName = it },
                        label = { Text("What is your name?", color = colors.textSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = colors.textPrimary,
                            unfocusedTextColor = colors.textPrimary,
                            focusedBorderColor = colors.accentPrimary,
                            unfocusedBorderColor = colors.borderStrong
                        )
                    )
                    Spacer(modifier = Modifier.height(Space.xl))
                    Button(
                        onClick = { if(userName.isNotBlank()) step = 1 },
                        modifier = Modifier.fillMaxWidth().height(Space.xxl),
                        colors = ButtonDefaults.buttonColors(containerColor = colors.accentPrimary, contentColor = colors.textInverse)
                    ) {
                        Text("CONTINUE", style = typography.label)
                    }
                }
                1 -> {
                    Text("YOUR SKILL LEVEL", style = typography.h2, fontWeight = FontWeight.Bold, color = colors.accentPrimary)
                    Spacer(modifier = Modifier.height(Space.xl))
                    listOf("Beginner", "Intermediate", "Advanced").forEach { level ->
                        OutlinedButton(
                            onClick = { skillLevel = level; step = 2 },
                            modifier = Modifier.fillMaxWidth().padding(vertical = Space.sm).height(Space.xxl),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (skillLevel == level) colors.surfaceHover else colors.surface,
                                contentColor = colors.textPrimary
                            ),
                            border = androidx.compose.foundation.BorderStroke(Border.hairline, if (skillLevel == level) colors.accentPrimary else colors.borderStrong)
                        ) {
                            Text(level, style = typography.label)
                        }
                    }
                }
                2 -> {
                    Text("TIME COMMITMENT", style = typography.h2, fontWeight = FontWeight.Bold, color = colors.accentPrimary)
                    Spacer(modifier = Modifier.height(Space.xl))
                    listOf(15, 30, 60).forEach { d ->
                        OutlinedButton(
                            onClick = { days = d },
                            modifier = Modifier.fillMaxWidth().padding(vertical = Space.sm).height(Space.xxl),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (days == d) colors.surfaceHover else colors.surface,
                                contentColor = colors.textPrimary
                            ),
                            border = androidx.compose.foundation.BorderStroke(Border.hairline, if (days == d) colors.accentPrimary else colors.borderStrong)
                        ) {
                            Text("$d Mins/Day", style = typography.label)
                        }
                    }
                    Spacer(modifier = Modifier.height(Space.xl))
                    Button(
                        onClick = { isLoading = true },
                        modifier = Modifier.fillMaxWidth().height(Space.xxl),
                        colors = ButtonDefaults.buttonColors(containerColor = colors.accentPrimary, contentColor = colors.textInverse)
                    ) {
                        Text("GENERATE SCHEDULE", style = typography.label)
                    }
                }
            }
        }
    }
}
