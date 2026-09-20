package com.unsulliedcode.ui.course

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.runtime.NavKey
import com.unsulliedcode.data.ContentRepository
import com.unsulliedcode.ui.navigation.AppScaffold
import com.unsulliedcode.ui.theme.*
import com.unsulliedcode.LessonViewerTheory
import com.unsulliedcode.LessonViewerVideo
import com.unsulliedcode.LessonViewerInteractive

@Composable
fun GenericCourseListScreen(
    languageId: String,
    onNavigate: (NavKey) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val repo = remember { ContentRepository(context) }
    val lessons = remember(languageId) { repo.getLessonsForLanguage(languageId) }
    val colors = LocalAppColors.current
    val typography = LocalAppTypography.current

    AppScaffold(
        title = "${languageId.replaceFirstChar { it.uppercase() }} Curriculum",
        onBack = onBack
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.bg)
                .padding(padding),
            contentPadding = PaddingValues(Space.md),
            verticalArrangement = Arrangement.spacedBy(Space.sm)
        ) {
            items(lessons) { lesson ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colors.surfaceElevated, androidx.compose.foundation.shape.RoundedCornerShape(Radius.md))
                        .interactive { onNavigate(LessonViewerTheory) }
                        .padding(Space.md)
                ) {
                    Column {
                        Text(text = "Lesson ${lesson.order}", style = typography.overline, color = colors.textTertiary)
                        Spacer(modifier = Modifier.height(Space.xs))
                        Text(text = lesson.title, style = typography.h3, color = colors.textPrimary)
                        Spacer(modifier = Modifier.height(Space.xxs))
                        Text(
                            text = lesson.difficulty.uppercase(),
                            style = typography.caption,
                            color = when (lesson.difficulty.lowercase()) {
                                "easy" -> colors.difficultyEasy
                                "medium" -> colors.difficultyMedium
                                "hard" -> colors.difficultyHard
                                else -> colors.difficultyExpert
                            }
                        )
                    }
                }
            }
        }
    }
}
