package com.cultcode.ui.practice

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.cultcode.data.QuestionRepository
import com.cultcode.engine.CodeExecutionEngine
import com.cultcode.engine.SyntaxHighlightingTransformation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PracticeScreen(questionId: String, onNavigate: (NavKey) -> Unit) {
    val context = LocalContext.current
    val qRepository = remember { QuestionRepository(context) }
    val uRepository = remember { com.cultcode.data.UserProgressRepository(context) }
    val engine = remember { CodeExecutionEngine(context) }
    
    val question = qRepository.getQuestion(questionId)
    
    if (question == null) {
        Scaffold(topBar = { TopAppBar(title = { Text("Error") }, navigationIcon = { IconButton(onClick = { onNavigate(com.cultcode.Home) }) { Text("<") } }) }) { p ->
            Box(modifier = Modifier.padding(p).padding(16.dp)) { Text("Question not found.") }
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
        val result = engine.evaluateSemantic(question.id, codeText, question.correctAnswer, question.acceptedAnswers)
        output = result.stdout
        isSuccess = result.isSuccess
        isRealExecution = result.isRealExecution
        if (result.isSuccess) {
            uRepository.addUcScore(10)
            if (questionId.contains("ADVANCED")) {
                if (questionId.startsWith("PY")) uRepository.addBadge("Python Advanced")
                if (questionId.startsWith("SQL")) uRepository.addBadge("SQL Advanced")
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = { IconButton(onClick = { onNavigate(com.cultcode.Home) }) { Text("<") } },
                title = { Text("Practice", fontWeight = FontWeight.Bold) },
                actions = { Text("UnsulliedCode ", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary) }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = runCode) {
                Text("Run Code")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(question.question, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            
            OutlinedTextField(
                value = codeText,
                onValueChange = { codeText = it },
                modifier = Modifier.fillMaxWidth().heightIn(min = 150.dp),
                visualTransformation = SyntaxHighlightingTransformation(),
                textStyle = LocalTextStyle.current.copy(fontFamily = FontFamily.Monospace),
                placeholder = { Text("Write your solution here...") }
            )
            
            if (output != null) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = if (isSuccess) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(if (isSuccess) "SOLUTION VERIFIED!" else "EXECUTION FAILED", style = MaterialTheme.typography.labelMedium, color = if (isSuccess) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onErrorContainer)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(output!!, fontFamily = FontFamily.Monospace)
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            if (isSuccess) {
                                Button(onClick = { onNavigate(com.cultcode.Home) }) { Text("Next Question") }
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
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant), modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("SOLUTION / HINT", style = MaterialTheme.typography.labelSmall)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(question.solution, fontFamily = FontFamily.Monospace)
                        if (question.explanation.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(question.explanation, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Expected Output:", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                    Text(question.expectedOutput, fontFamily = FontFamily.Monospace, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
