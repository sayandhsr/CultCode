package com.unsulliedcode.ui.practice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.unsulliedcode.ui.navigation.AppScaffold
import com.unsulliedcode.ui.theme.*

@Composable
fun PracticeEditorScreen(onBack: () -> Unit) {
    val colors = LocalAppColors.current
    var selected by remember { mutableStateOf<Int?>(null) }
    
    AppScaffold(title = "Daily Practice", onBack = onBack) { padding ->
        Box(modifier = Modifier.fillMaxSize().background(colors.bg).padding(padding).padding(Space.md)) {
            QuestionCard(
                question = com.unsulliedcode.data.Question(
                    id = "q1",
                    languageId = "python",
                    lessonId = "py_01",
                    type = com.unsulliedcode.data.QuestionType.MULTIPLE_CHOICE,
                    text = "What is the time complexity of a binary search tree lookup?",
                    options = listOf("O(1)", "O(log n)", "O(n)", "O(n^2)"),
                    correctIndex = 1,
                    explanation = "A balanced BST divides the search space in half at each step, yielding a logarithmic time complexity.",
                    difficulty = "Medium",
                    xpReward = 15
                ),
                selectedAnswer = selected,
                onSelectAnswer = { selected = it }
            )
        }
    }
}
