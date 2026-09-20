package com.cultcode.ui.practice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PracticeScreen(questionId: String, onNavigate: (NavKey) -> Unit) {
    var codeText by remember { mutableStateOf("# Write your code below\n\n") }
    var showHintDialog by remember { mutableStateOf(false) }
    var hintIndex by remember { mutableStateOf(0) }
    var showSolutionDialog by remember { mutableStateOf(false) }
    var executionResult by remember { mutableStateOf<String?>(null) }
    var isRealExecution by remember { mutableStateOf(false) }

    val hints = listOf(
        "In Python, you use the `=` operator to assign values to variables.",
        "The variable name goes on the left, and the value on the right."
    )

    fun runCode() {
        if (codeText.contains("score = 10") || codeText.contains("score=10")) {
            executionResult = "Process finished with exit code 0\nVariable 'score' successfully assigned."
            isRealExecution = false // Simulated for this MVP demo
        } else {
            executionResult = "Error: variable 'score' not found or incorrect value."
            isRealExecution = false
        }
    }

    if (showHintDialog) {
        AlertDialog(
            onDismissRequest = { showHintDialog = false },
            title = { Text("Hint ${hintIndex + 1}") },
            text = { Text(hints[hintIndex]) },
            confirmButton = {
                if (hintIndex < hints.size - 1) {
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
                    Text("score = 10", fontFamily = FontFamily.Monospace, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Variables are containers for storing data values. Python has no command for declaring a variable; it is created the moment you first assign a value to it.", style = MaterialTheme.typography.bodySmall)
                }
            },
            confirmButton = { TextButton(onClick = { showSolutionDialog = false }) { Text("Close") } }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(questionId, style = MaterialTheme.typography.titleMedium) },
                actions = {
                    TextButton(onClick = { showHintDialog = true }) {
                        Text("Hint")
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
                    TextButton(onClick = { codeText = "# Write your code below\n\n" }) {
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
            // Question Description
            Text("Assign the integer value 10 to a variable named `score`.", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
            
            // Expected Result or Console Output Placeholder
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Expected:", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                    Text("score = 10", fontFamily = FontFamily.Monospace, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Phase 10: Full Code Editor Placeholder
            Text("Code Editor", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = codeText,
                onValueChange = { codeText = it },
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
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text("Console Output", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                            Text(if (isRealExecution) "REAL EXECUTION" else "SIMULATED RESULT", style = MaterialTheme.typography.labelSmall, color = if (isRealExecution) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(executionResult!!, fontFamily = FontFamily.Monospace, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

