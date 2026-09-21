package com.unsulliedcode.ui.practice

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation3.runtime.NavKey
import com.unsulliedcode.data.QuestionRepository
import com.unsulliedcode.engine.CodeExecutionEngine
import com.unsulliedcode.engine.SyntaxHighlightingTransformation
import com.unsulliedcode.ui.navigation.AppScaffold
import com.unsulliedcode.ui.theme.LocalAppColors
import com.unsulliedcode.ui.theme.LocalAppTypography
import com.unsulliedcode.ui.theme.Space

@Composable
fun PracticeScreen(questionId: String, onNavigate: (NavKey) -> Unit) {
    val context = LocalContext.current
    val qRepository = remember { QuestionRepository(context) }
    val uRepository = remember { com.unsulliedcode.data.UserProgressRepository(context) }
    val engine = remember { CodeExecutionEngine(context) }
    
    val question = qRepository.getQuestion(questionId)
    val colors = LocalAppColors.current
    val typography = LocalAppTypography.current
    
    if (question == null) {
        AppScaffold(title = "Error", onBack = { onNavigate(com.unsulliedcode.Home) }) { p ->
            Box(modifier = Modifier.padding(p).padding(Space.md)) { Text("Question not found.") }
        }
        return
    }

    val prefs = context.getSharedPreferences("practice_workspace", android.content.Context.MODE_PRIVATE)
    var codeText by remember { mutableStateOf(prefs.getString("prac_${question.id}", question.code) ?: question.code) }
    
    LaunchedEffect(codeText) {
        prefs.edit().putString("prac_${question.id}", codeText).apply()
    }
    
    var output by remember { mutableStateOf<String?>(null) }
    var isSuccess by remember { mutableStateOf(false) }
    var isRealExecution by remember { mutableStateOf(false) }
    var showSolution by remember { mutableStateOf(false) }

    val runCode = {
        val evalEngine = com.unsulliedcode.engine.EvaluationEngine()
        val result = engine.execute(question.languageId, codeText, question.correctAnswer, question.acceptedAnswers)
        output = result.stdout
        isSuccess = result.isSuccess
        isRealExecution = result.isRealExecution
        if (result.isSuccess) {
            val eval = evalEngine.evaluateAnswer(
                selectedIndex = 1,
                correctIndex = 1,
                baseXp = question.xpReward,
                explanation = question.explanation
            )
            uRepository.addUcScore(eval.xpEarned)
            
            if (questionId.contains("ADVANCED")) {
                if (question.languageId == "python") uRepository.addBadge("Python Advanced")
                if (question.languageId == "sql") uRepository.addBadge("SQL Advanced")
            }
        }
    }

    AppScaffold(
        title = "Practice",
        onBack = { onNavigate(com.unsulliedcode.Home) }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(Space.md)
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = Space.xxxl), // leave space for FAB
                verticalArrangement = Arrangement.spacedBy(Space.md)
            ) {
                Text(question.text, style = typography.h3, fontWeight = FontWeight.Bold, color = colors.textPrimary)
                
                OutlinedTextField(
                    value = codeText,
                    onValueChange = { codeText = it },
                    modifier = Modifier.fillMaxWidth().heightIn(min = Space.md),
                    visualTransformation = SyntaxHighlightingTransformation(colors),
                    textStyle = LocalTextStyle.current.copy(fontFamily = FontFamily.Monospace),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = colors.textPrimary,
                        unfocusedTextColor = colors.textPrimary,
                        cursorColor = colors.accentPrimary
                    ),
                    placeholder = { Text("Write your solution here...") }
                )
                
                if (output != null) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = if (isSuccess) colors.successBg else colors.errorBg),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(Space.md)) {
                            Text(if (isSuccess) "SOLUTION VERIFIED!" else "EXECUTION FAILED", style = typography.label, color = if (isSuccess) colors.success else colors.error)
                            Spacer(modifier = Modifier.height(Space.sm))
                            Text(output!!, fontFamily = FontFamily.Monospace, color = colors.textPrimary)
                            
                            Spacer(modifier = Modifier.height(Space.md))
                            Row(horizontalArrangement = Arrangement.spacedBy(Space.sm)) {
                                if (isSuccess) {
                                    Button(onClick = { onNavigate(com.unsulliedcode.Home) }) { Text("Next Question") }
                                    OutlinedButton(onClick = { showSolution = true }) { Text("View Solution") }
                                } else {
                                    Button(onClick = runCode) { Text("Retry") }
                                    OutlinedButton(onClick = { showSolution = true }) { Text("Unlock Hint") }
                                }
                            }
                        }
                    }
                }

                if (showSolution) {
                    Card(colors = CardDefaults.cardColors(containerColor = colors.surfaceHover), modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(Space.md)) {
                            Text("SOLUTION / HINT", style = typography.label, color = colors.textSecondary)
                            Spacer(modifier = Modifier.height(Space.sm))
                            Text(question.solution, fontFamily = FontFamily.Monospace, color = colors.textPrimary)
                            if (question.explanation.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(Space.sm))
                                Text(question.explanation, style = typography.body, color = colors.textSecondary)
                            }
                        }
                    }
                }

                Card(
                    colors = CardDefaults.cardColors(containerColor = colors.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(Space.md)) {
                        Text("Expected Output:", style = typography.label, color = colors.textSecondary)
                        Text(question.expectedOutput, fontFamily = FontFamily.Monospace, color = colors.textSecondary)
                    }
                }
            }
            
            ExtendedFloatingActionButton(
                onClick = runCode,
                modifier = Modifier.align(androidx.compose.ui.Alignment.BottomEnd).padding(Space.md)
            ) {
                Text("Run Code")
            }
        }
    }
}
