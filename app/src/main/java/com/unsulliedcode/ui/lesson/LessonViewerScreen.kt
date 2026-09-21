package com.unsulliedcode.ui.lesson

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.runtime.NavKey
import com.unsulliedcode.ui.navigation.AppScaffold
import com.unsulliedcode.ui.theme.LocalAppColors
import com.unsulliedcode.ui.theme.LocalAppTypography
import com.unsulliedcode.ui.theme.Space
import com.unsulliedcode.ui.theme.Radius
import com.unsulliedcode.data.ContentRepository
import com.unsulliedcode.ui.ide.CodeEditor

@Composable
fun LessonViewerScreen(
    languageId: String,
    lessonId: String,
    onNavigate: (NavKey) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val repo = remember { ContentRepository(context) }
    val colors = LocalAppColors.current
    val typography = LocalAppTypography.current
    val lessons = remember(languageId) { repo.getLessonsForLanguage(languageId) }
    val lesson = lessons.firstOrNull { it.id == lessonId } ?: lessons.firstOrNull()

    AppScaffold(
        title = lesson?.title ?: "Lesson",
        onBack = onBack
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(Space.md)
                .verticalScroll(rememberScrollState())
        ) {
            if (lesson != null) {
                Text(
                    text = lesson.title,
                    style = typography.h2,
                    color = colors.textPrimary,
                    modifier = Modifier.padding(bottom = Space.sm)
                )
                Text(
                    text = lesson.content,
                    style = typography.body,
                    color = colors.textSecondary,
                    modifier = Modifier.padding(bottom = Space.md)
                )
                if (lesson.codeExample.isNotEmpty()) {
                    Text(
                        text = "Example:",
                        style = typography.label,
                        color = colors.textPrimary,
                        modifier = Modifier.padding(bottom = Space.xs)
                    )
                    
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(colors.surface, androidx.compose.foundation.shape.RoundedCornerShape(Radius.sm))
                    ) {
                        CodeEditor(
                            code = lesson.codeExample,
                            onCodeChange = {},
                            language = languageId,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
                Spacer(modifier = Modifier.height(Space.lg))
                Button(
                    onClick = { onNavigate(com.unsulliedcode.PracticeEditor(languageId)) },
                    colors = ButtonDefaults.buttonColors(containerColor = colors.accentPrimary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("START PRACTICE", color = colors.textOnAccent)
                }
            } else {
                Text(
                    text = "Lesson not found.",
                    style = typography.body,
                    color = colors.textSecondary
                )
            }
        }
    }
}
