package com.unsulliedcode

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable data object Splash : NavKey
@Serializable data object Onboarding : NavKey
@Serializable data object Home : NavKey
@Serializable data object Profile : NavKey
@Serializable data object Settings : NavKey
@Serializable data object SettingsTheme : NavKey
@Serializable data object SettingsAccount : NavKey
@Serializable data object SettingsNotifications : NavKey
@Serializable data object SettingsStorage : NavKey
@Serializable data object SettingsAbout : NavKey
@Serializable data object DesignGallery : NavKey

@Serializable data object ArenaMatchmaking : NavKey
@Serializable data object ArenaBattle : NavKey
@Serializable data object ArenaResult : NavKey
@Serializable data object ArenaLeaderboard : NavKey
@Serializable data object LearnDashboard : NavKey

@Serializable data class CourseList(val languageId: String) : NavKey
@Serializable data class LessonViewer(val languageId: String, val lessonId: String) : NavKey

@Serializable data class PracticeEditor(val languageId: String, val questionId: String = "") : NavKey
@Serializable data class PracticeResult(val languageId: String, val questionId: String = "", val isCorrect: Boolean = false) : NavKey

@Serializable data object IdeSandbox : NavKey
@Serializable data object IdeJupyterMode : NavKey
@Serializable data object IdeTerminalMode : NavKey

@Serializable data object GitSimulatorLog : NavKey
@Serializable data object GitSimulatorCommit : NavKey
@Serializable data object GitSimulatorMerge : NavKey
@Serializable data object DataScienceLab : NavKey
@Serializable data object DataScienceDataset : NavKey

@Serializable data object GamificationBadges : NavKey
@Serializable data object GamificationStreak : NavKey
@Serializable data object GamificationRewards : NavKey
