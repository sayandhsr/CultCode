package com.cultcode.ui.practice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.cultcode.data.Question
import com.cultcode.data.QuestionRepository
import com.cultcode.engine.CodeExecutionEngine
import com.cultcode.engine.SyntaxHighlightingTransformation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PracticeScreen(questionId: String, onNavigate: (NavKey) -> Unit) {
    val context = LocalContext.current
    val repository = remember { QuestionRepository(context) }
    val engine = remember { CodeExecutionEngine(context) }
    val question = remember(questionId) { repository.getQuestion(questionId) }
    
    if (question == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
            Text("Question not found")
        }
        return
    }

    val prefs = context.getSharedPreferences("code_workspace", android.content.Context.MODE_PRIVATE)
    var codeText by remember(questionId) { mutableStateOf(prefs.getString(questionId, question.code) ?: question.code) }
    
    LaunchedEffect(codeText) {
        prefs.edit().putString(questionId, codeText).apply()
    }
    var showHintDialog by remember { mutableStateOf(false) }
    var hintIndex by remember { mutableStateOf(0) }
    var showSolutionDialog by remember { mutableStateOf(false) }
    var executionResult by remember { mutableStateOf<String?>(null) }
    var isRealExecution by remember { mutableStateOf(false) }
    var isCorrect by remember { mutableStateOf(false) }

    fun runCode() {
        val language = if (questionId.startsWith("SQL", ignoreCase = true)) "sql" else "python"
        val result = engine.execute(language, codeText, question.correctAnswer, question.acceptedAnswers)
        executionResult = result.stdout
        isCorrect = result.isSuccess
        isRealExecution = result.isRealExecution
    }

    if (showHintDialog) {
        AlertDialog(
            onDismissRequest = { showHintDialog = false },
            title = { Text("Hint ${hintIndex + 1}") },
            text = { Text(question.hints.getOrElse(hintIndex) { "No more hints" }) },
            confirmButton = {
                if (hintIndex < question.hints.size - 1) {
                    TextButton(onClick = { hintIndex++ }) { Text("Next Hint") }
                } else {
                    TextButton(onClick = { showHintDialog = false }) { Text("Close") }
                }
            },
            dismissButton = {
                TextButton(onClick = { showSolutionDialog = true; showHintDialog = false }) { Text("View Solution") }
            }
        )
    }

    if (showSolutionDialog) {
        AlertDialog(
            onDismissRequest = { showSolutionDialog = false },
            title = { Text("Solution") },
            text = { 
                Column {
                    Text(question.solution, fontFamily = FontFamily.Monospace, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(question.explanation, style = MaterialTheme.typography.bodySmall)
                }
            },
            confirmButton = { TextButton(onClick = { showSolutionDialog = false }) { Text("Close") } }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = { IconButton(onClick = { onNavigate(com.cultcode.Home) }) { Text("<") } },
                title = { Text(questionId, style = MaterialTheme.typography.titleMedium) },
                actions = {
                    Text("UnsulliedCode ", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    if (question.hints.isNotEmpty()) {
                        TextButton(onClick = { showHintDialog = true }) {
                            Text("Hint")
                        }
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = { codeText = question.code }) {
                        Text("Reset")
                    }
                    Button(onClick = { runCode() }) {
                        Text("Run Code")
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)
        ) {
            Text(question.question, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
            
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Expected:", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                    Text(question.correctAnswer, fontFamily = FontFamily.Monospace, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text("Code Editor", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = codeText,
                onValueChange = { codeText = it },
                visualTransformation = SyntaxHighlightingTransformation(),
                modifier = Modifier.fillMaxWidth().weight(1f),
                textStyle = LocalTextStyle.current.copy(fontFamily = FontFamily.Monospace),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )
            
            if (executionResult != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = if (isCorrect) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text("Console Output", style = MaterialTheme.typography.labelSmall, color = if (isCorrect) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onErrorContainer)
                            Text(if (isRealExecution) "REAL EXECUTION" else "SIMULATED RESULT", style = MaterialTheme.typography.labelSmall, color = if (isCorrect) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onErrorContainer)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(executionResult!!, fontFamily = FontFamily.Monospace, color = if (isCorrect) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onErrorContainer)
                    }
                }
            }
        }
    }
}
