package com.unsulliedcode.ui.practice
import com.unsulliedcode.ui.theme.Space
import com.unsulliedcode.ui.theme.Radius
import com.unsulliedcode.ui.theme.Border

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
import com.unsulliedcode.data.QuestionRepository
import com.unsulliedcode.engine.CodeExecutionEngine
import com.unsulliedcode.engine.SyntaxHighlightingTransformation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PracticeScreen(questionId: String, onNavigate: (NavKey) -> Unit) {
    val context = LocalContext.current
    val qRepository = remember { QuestionRepository(context) }
    val uRepository = remember { com.unsulliedcode.data.UserProgressRepository(context) }
    val engine = remember { CodeExecutionEngine(context) }
    
    val question = qRepository.getQuestion(questionId)
    
    if (question == null) {
        Scaffold(topBar = { TopAppBar(title = { Text("Error") }, navigationIcon = { IconButton(onClick = { onNavigate(com.unsulliedcode.Home) }) { Text("<") } }) }) { p ->
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
        val result = engine.execute(question.id.substringBefore("-"), codeText, question.correctAnswer, question.acceptedAnswers)
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
                navigationIcon = { IconButton(onClick = { onNavigate(com.unsulliedcode.Home) }) { Text("<") } },
                title = { Text("Practice", fontWeight = FontWeight.Bold) },
                actions = { Text("UnsulliedCode ", fontWeight = FontWeight.Bold, color = com.unsulliedcode.ui.theme.LocalAppColors.current.accentPrimary) }
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
                .padding(Space.md)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(Space.md)
        ) {
            Text(question.question, style = com.unsulliedcode.ui.theme.LocalAppTypography.current.h3, fontWeight = FontWeight.Bold)
            
            OutlinedTextField(
                value = codeText,
                onValueChange = { codeText = it },
                modifier = Modifier.fillMaxWidth().heightIn(min = Space.md),
                visualTransformation = SyntaxHighlightingTransformation(com.unsulliedcode.ui.theme.LocalAppColors.current),
                textStyle = LocalTextStyle.current.copy(fontFamily = FontFamily.Monospace),
                placeholder = { Text("Write your solution here...") }
            )
            
            if (output != null) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = if (isSuccess) com.unsulliedcode.ui.theme.LocalAppColors.current.successBg else com.unsulliedcode.ui.theme.LocalAppColors.current.errorBg),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(Space.md)) {
                        Text(if (isSuccess) "SOLUTION VERIFIED!" else "EXECUTION FAILED", style = com.unsulliedcode.ui.theme.LocalAppTypography.current.label, color = if (isSuccess) com.unsulliedcode.ui.theme.LocalAppColors.current.success else com.unsulliedcode.ui.theme.LocalAppColors.current.error)
                        Spacer(modifier = Modifier.height(Space.sm))
                        Text(output!!, fontFamily = FontFamily.Monospace)
                        
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
                Card(colors = CardDefaults.cardColors(containerColor = com.unsulliedcode.ui.theme.LocalAppColors.current.surfaceHover), modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(Space.md)) {
                        Text("SOLUTION / HINT", style = com.unsulliedcode.ui.theme.LocalAppTypography.current.label)
                        Spacer(modifier = Modifier.height(Space.sm))
                        Text(question.solution, fontFamily = FontFamily.Monospace)
                        if (question.explanation.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(Space.sm))
                            Text(question.explanation, style = com.unsulliedcode.ui.theme.LocalAppTypography.current.body)
                        }
                    }
                }
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = com.unsulliedcode.ui.theme.LocalAppColors.current.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(Space.md)) {
                    Text("Expected Output:", style = com.unsulliedcode.ui.theme.LocalAppTypography.current.label, color = com.unsulliedcode.ui.theme.LocalAppColors.current.borderStrong)
                    Text(question.expectedOutput, fontFamily = FontFamily.Monospace, color = com.unsulliedcode.ui.theme.LocalAppColors.current.textSecondary)
                }
            }
            
            Spacer(modifier = Modifier.height(Space.md))
        }
    }
}




