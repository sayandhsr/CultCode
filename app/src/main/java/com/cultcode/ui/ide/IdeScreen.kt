package com.cultcode.ui.ide

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.cultcode.engine.CodeExecutionEngine
import com.cultcode.engine.SyntaxHighlightingTransformation

data class IdeCell(val id: Int, var code: String, var output: String? = null)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IdeScreen(initialLanguage: String, onNavigate: (NavKey) -> Unit) {
    val context = LocalContext.current
    val engine = remember { CodeExecutionEngine(context) }
    
    var language by remember { mutableStateOf(initialLanguage) }
    var isJupyterMode by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    
    val languages = listOf("Python", "SQL", "JavaScript", "Java", "C++")
    
    // State for VS Code mode
    val prefs = context.getSharedPreferences("ide_workspace", android.content.Context.MODE_PRIVATE)
    var vsCodeText by remember { mutableStateOf(prefs.getString("vscode_text", "") ?: "") }
    LaunchedEffect(vsCodeText) { prefs.edit().putString("vscode_text", vsCodeText).apply() }
    var vsCodeOutput by remember { mutableStateOf<String?>(null) }
    
    // State for Jupyter mode
    var cells by remember { mutableStateOf(listOf(IdeCell(1, ""))) }
    var nextCellId by remember { mutableStateOf(2) }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = { IconButton(onClick = { onNavigate(com.cultcode.Home) }) { Text("<") } },
                title = { Text("IDE") },
                actions = {
                    Box {
                        TextButton(onClick = { expanded = true }) {
                            Text(language)
                        }
                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            languages.forEach { lang ->
                                DropdownMenuItem(
                                    text = { Text(lang) },
                                    onClick = { language = lang; expanded = false }
                                )
                            }
                        }
                    }
                    TextButton(onClick = { isJupyterMode = !isJupyterMode }) {
                        Text(if (isJupyterMode) "Jupyter Mode" else "VS Code Mode")
                    }
                }
            )
        },
        floatingActionButton = {
            if (isJupyterMode) {
                FloatingActionButton(onClick = {
                    cells = cells + IdeCell(nextCellId++, "")
                }) {
                    Text("+ Cell")
                }
            } else {
                FloatingActionButton(onClick = {
                    vsCodeOutput = engine.freeRun(language, vsCodeText).stdout
                }) {
                    Text("Run")
                }
            }
        }
    ) { padding ->
        if (isJupyterMode) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(cells) { cell ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Cell [${cell.id}]", style = MaterialTheme.typography.labelSmall)
                                TextButton(onClick = {
                                    val newCells = cells.map { 
                                        if (it.id == cell.id) it.copy(output = engine.freeRun(language, it.code).stdout) else it
                                    }
                                    cells = newCells
                                }) {
                                    Text("Run Cell")
                                }
                            }
                            OutlinedTextField(
                                value = cell.code,
                                onValueChange = { newCode ->
                                    cells = cells.map { if (it.id == cell.id) it.copy(code = newCode) else it }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                visualTransformation = SyntaxHighlightingTransformation(),
                                textStyle = LocalTextStyle.current.copy(fontFamily = FontFamily.Monospace)
                            )
                            if (cell.output != null) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(cell.output!!, fontFamily = FontFamily.Monospace, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        } else {
            Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
                OutlinedTextField(
                    value = vsCodeText,
                    onValueChange = { vsCodeText = it },
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    visualTransformation = SyntaxHighlightingTransformation(),
                    textStyle = LocalTextStyle.current.copy(fontFamily = FontFamily.Monospace),
                    placeholder = { Text("Write your code here...") }
                )
                if (vsCodeOutput != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp, max = 250.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("TERMINAL OUTPUT", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(vsCodeOutput!!, fontFamily = FontFamily.Monospace)
                        }
                    }
                }
            }
        }
    }
}
