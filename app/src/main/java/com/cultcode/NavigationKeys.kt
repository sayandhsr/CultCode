package com.cultcode

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable data object Onboarding : NavKey
@Serializable data object Home : NavKey
@Serializable data object CourseList : NavKey
@Serializable data class LessonViewer(val courseId: String, val lessonId: String) : NavKey
@Serializable data class Practice(val questionId: String) : NavKey
@Serializable data object Profile : NavKey


@Serializable data class Ide(val initialLanguage: String) : NavKey
