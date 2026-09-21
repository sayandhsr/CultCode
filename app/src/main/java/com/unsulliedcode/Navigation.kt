package com.unsulliedcode

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.unsulliedcode.data.UserProgressRepository

@Composable
fun MainNavigation() {
    val context = LocalContext.current
    val repo = remember { UserProgressRepository(context) }
    val startDestination = if (repo.isOnboardingComplete()) Home else Onboarding
    val backStack = rememberNavBackStack(startDestination)
    val goBack: () -> Unit = { backStack.removeLastOrNull() }

    NavDisplay(
        backStack = backStack,
        onBack = goBack,
        entryProvider = entryProvider {
            // ── Core Flow ──
            entry<Splash> {
                com.unsulliedcode.ui.onboarding.OnboardingScreen(
                    onComplete = { backStack.removeLast(); backStack.add(Home) }
                )
            }
            entry<Onboarding> {
                com.unsulliedcode.ui.onboarding.OnboardingScreen(
                    onComplete = { backStack.removeLast(); backStack.add(Home) }
                )
            }
            entry<Home> {
                com.unsulliedcode.ui.home.HomeScreen(
                    onNavigate = { backStack.add(it) }
                )
            }

            // ── Profile & Settings ──
            entry<Profile> {
                com.unsulliedcode.ui.settings.SettingsScreen(onBack = goBack)
            }
            entry<Settings> {
                com.unsulliedcode.ui.settings.SettingsScreen(onBack = goBack)
            }
            entry<SettingsTheme> { com.unsulliedcode.ui.settings.SettingsSubScreen("Theme", onBack = goBack) }
            entry<SettingsAccount> { com.unsulliedcode.ui.settings.SettingsSubScreen("Account", onBack = goBack) }
            entry<SettingsNotifications> { com.unsulliedcode.ui.settings.SettingsSubScreen("Notifications", onBack = goBack) }
            entry<SettingsStorage> { com.unsulliedcode.ui.settings.SettingsSubScreen("Storage", onBack = goBack) }
            entry<SettingsAbout> { com.unsulliedcode.ui.settings.SettingsSubScreen("About", onBack = goBack) }

            // ── Design Gallery ──
            entry<DesignGallery> { com.unsulliedcode.ui.debug.DesignGalleryScreen() }

            // ── Arena (each route → distinct screen) ──
            entry<ArenaMatchmaking> {
                com.unsulliedcode.ui.arena.ArenaMatchmakingScreen(
                    onNavigate = { backStack.add(it) },
                    onBack = goBack
                )
            }
            entry<ArenaBattle> {
                com.unsulliedcode.ui.arena.ArenaBattleScreen(
                    onNavigate = { backStack.add(it) },
                    onBack = goBack
                )
            }
            entry<ArenaResult> {
                com.unsulliedcode.ui.arena.ArenaResultScreen(
                    onNavigate = { backStack.add(it) },
                    onBack = goBack
                )
            }
            entry<ArenaLeaderboard> {
                com.unsulliedcode.ui.arena.ArenaLeaderboardScreen(onBack = goBack)
            }

            // ── Learn ──
            entry<LearnDashboard> {
                com.unsulliedcode.ui.home.LearnDashboardScreen(onBack = goBack)
            }

            // ── Course Lists (Parameterized) ──
            entry<CourseList> { key ->
                com.unsulliedcode.ui.course.GenericCourseListScreen(
                    languageId = key.languageId,
                    onNavigate = { backStack.add(it) },
                    onBack = goBack
                )
            }

            // ── Lesson Viewers (Parameterized) ──
            entry<LessonViewer> { key ->
                com.unsulliedcode.ui.lesson.LessonViewerScreen(
                    languageId = key.languageId,
                    lessonId = key.lessonId,
                    onNavigate = { backStack.add(it) },
                    onBack = goBack
                )
            }

            // 🎯 Practice 🎯
            entry<PracticeEditor> { key ->
                val fallbackId = when(key.languageId.lowercase()) {
                    "sql" -> "SQL-BASICS-001"
                    "python" -> "PY-BASICS-001"
                    "javascript" -> "JS-BASICS-001"
                    "java" -> "JAV-BASICS-001"
                    "html" -> "HTM-BASICS-001"
                    else -> "PY-BASICS-001"
                }
                com.unsulliedcode.ui.practice.PracticeScreen(
                    questionId = if (key.questionId.isNotEmpty()) key.questionId else fallbackId,
                    onNavigate = { backStack.add(it) }
                )
            }

            // ── IDE ──
            entry<IdeSandbox> {
                com.unsulliedcode.ui.ide.IdeScreen("python", onNavigate = { backStack.add(it) })
            }
            entry<IdeJupyterMode> {
                com.unsulliedcode.ui.ide.IdeScreen("python", onNavigate = { backStack.add(it) })
            }
            entry<IdeTerminalMode> {
                com.unsulliedcode.ui.ide.IdeScreen("python", onNavigate = { backStack.add(it) })
            }

            // ── Git Simulator ──
            entry<GitSimulatorLog> { com.unsulliedcode.ui.git.GitSimulatorScreen(onBack = goBack) }
            entry<GitSimulatorCommit> { com.unsulliedcode.ui.git.GitSimulatorScreen(onBack = goBack) }
            entry<GitSimulatorMerge> { com.unsulliedcode.ui.git.GitSimulatorScreen(onBack = goBack) }

            // ── Data Science ──
            entry<DataScienceLab> { com.unsulliedcode.ui.settings.SettingsSubScreen("Data Science Lab", onBack = goBack) }
            entry<DataScienceDataset> { com.unsulliedcode.ui.settings.SettingsSubScreen("Datasets", onBack = goBack) }

            // ── Gamification ──
            entry<GamificationBadges> { com.unsulliedcode.ui.gamification.GamificationBadgesScreen(onBack = goBack) }
            entry<GamificationStreak> { com.unsulliedcode.ui.gamification.GamificationBadgesScreen(onBack = goBack) }
            entry<GamificationRewards> { com.unsulliedcode.ui.gamification.GamificationBadgesScreen(onBack = goBack) }
        },
    )
}
