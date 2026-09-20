package com.unsulliedcode.ui.git

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.unsulliedcode.ui.navigation.AppScaffold
import com.unsulliedcode.ui.theme.*

@Composable
fun GitSimulatorScreen(onBack: () -> Unit) {
    val colors = LocalAppColors.current
    AppScaffold(title = "Git Simulator", onBack = onBack) { padding ->
        GitLogView(
            commits = listOf(
                GitCommit("a1b2c3d", "Merge pull request #42", "Linus", "main", isMerge = true),
                GitCommit("f9e8d7c", "Implement Fast Inverse Square Root", "John", "feature/math"),
                GitCommit("b3c4d5e", "Initial commit", "Satoshi", "main")
            ),
            currentBranch = "main",
            modifier = Modifier.padding(padding)
        )
    }
}
