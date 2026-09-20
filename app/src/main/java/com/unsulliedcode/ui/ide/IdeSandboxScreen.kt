package com.unsulliedcode.ui.ide

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.unsulliedcode.ui.navigation.AppScaffold
import com.unsulliedcode.ui.theme.*

@Composable
fun IdeSandboxScreen(onBack: () -> Unit) {
    val colors = LocalAppColors.current
    var code by remember { mutableStateOf("def hello_world():\n    print(\"Hello\")") }
    
    AppScaffold(title = "Sandbox IDE", onBack = onBack) { padding ->
        Box(modifier = Modifier.fillMaxSize().background(colors.bg).padding(padding)) {
            CodeEditor(
                code = code,
                language = "python",
                onCodeChange = { code = it }
            )
        }
    }
}
