package com.unsulliedcode

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.unsulliedcode.data.UserProgressRepository
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.material3.Button
import androidx.compose.foundation.layout.Column

@Composable
fun StubScreen(name: String, onBack: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("$name Screen")
            Button(onClick = onBack) { Text("Back") }
        }
    }
}

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
            entry<Splash> { com.unsulliedcode.ui.settings.SettingsSubScreen("Splash", onBack = goBack) }
            entry<Onboarding> {
                com.unsulliedcode.ui.onboarding.OnboardingScreen(
                    onComplete = { backStack.removeLast(); backStack.add(Home) }
                )
            }
            entry<Home> {
                com.unsulliedcode.ui.home.HomeScreen(
                    onNavigate = { navKey -> backStack.add(navKey) }
                )
            }
            entry<Profile> {
                com.unsulliedcode.ui.settings.SettingsScreen(onBack = goBack)
            }
            entry<Settings> { com.unsulliedcode.ui.settings.SettingsSubScreen("Settings", onBack = goBack) }
            entry<SettingsTheme> { com.unsulliedcode.ui.settings.SettingsSubScreen("Theme Settings", onBack = goBack) }
            entry<SettingsAccount> { com.unsulliedcode.ui.settings.SettingsSubScreen("Account", onBack = goBack) }
            entry<SettingsNotifications> { com.unsulliedcode.ui.settings.SettingsSubScreen("Notifications", onBack = goBack) }
            entry<SettingsStorage> { com.unsulliedcode.ui.settings.SettingsSubScreen("Storage", onBack = goBack) }
            entry<SettingsAbout> { com.unsulliedcode.ui.settings.SettingsSubScreen("About", onBack = goBack) }
            entry<DesignGallery> { com.unsulliedcode.ui.debug.DesignGalleryScreen() }
            entry<ArenaMatchmaking> { com.unsulliedcode.ui.arena.ArenaMatchmakingScreen(onNavigate = { backStack.add(it) }, onBack = goBack) }
            entry<ArenaBattle> { com.unsulliedcode.ui.arena.ArenaMatchmakingScreen(onNavigate = { backStack.add(it) }, onBack = goBack) }
            entry<ArenaResult> { com.unsulliedcode.ui.arena.ArenaMatchmakingScreen(onNavigate = { backStack.add(it) }, onBack = goBack) }
            entry<ArenaLeaderboard> { com.unsulliedcode.ui.arena.ArenaMatchmakingScreen(onNavigate = { backStack.add(it) }, onBack = goBack) }
            entry<LearnDashboard> { com.unsulliedcode.ui.home.LearnDashboardScreen(onBack = goBack) }
            entry<CourseListPython> { com.unsulliedcode.ui.course.GenericCourseListScreen("python", onNavigate = { navKey -> backStack.add(navKey) }, onBack = goBack) }
            entry<CourseListJava> { com.unsulliedcode.ui.course.GenericCourseListScreen("java", onNavigate = { navKey -> backStack.add(navKey) }, onBack = goBack) }
            entry<CourseListCpp> { com.unsulliedcode.ui.course.GenericCourseListScreen("cpp", onNavigate = { navKey -> backStack.add(navKey) }, onBack = goBack) }
            entry<CourseListJavascript> { com.unsulliedcode.ui.course.GenericCourseListScreen("javascript", onNavigate = { navKey -> backStack.add(navKey) }, onBack = goBack) }
            entry<CourseListSql> { com.unsulliedcode.ui.course.GenericCourseListScreen("sql", onNavigate = { navKey -> backStack.add(navKey) }, onBack = goBack) }
            entry<CourseListDocker> { com.unsulliedcode.ui.course.GenericCourseListScreen("docker", onNavigate = { navKey -> backStack.add(navKey) }, onBack = goBack) }
            entry<CourseListK8s> { com.unsulliedcode.ui.course.GenericCourseListScreen("k8s", onNavigate = { navKey -> backStack.add(navKey) }, onBack = goBack) }
            entry<CourseListPandas> { com.unsulliedcode.ui.course.GenericCourseListScreen("pandas", onNavigate = { navKey -> backStack.add(navKey) }, onBack = goBack) }
            entry<CourseListNumpy> { com.unsulliedcode.ui.course.GenericCourseListScreen("numpy", onNavigate = { navKey -> backStack.add(navKey) }, onBack = goBack) }
            entry<CourseListHtml> { com.unsulliedcode.ui.course.GenericCourseListScreen("html", onNavigate = { navKey -> backStack.add(navKey) }, onBack = goBack) }
            entry<CourseListYaml> { com.unsulliedcode.ui.course.GenericCourseListScreen("yaml", onNavigate = { navKey -> backStack.add(navKey) }, onBack = goBack) }
            entry<CourseListMongo> { com.unsulliedcode.ui.course.GenericCourseListScreen("mongo", onNavigate = { navKey -> backStack.add(navKey) }, onBack = goBack) }
            entry<CourseListGo> { com.unsulliedcode.ui.course.GenericCourseListScreen("go", onNavigate = { navKey -> backStack.add(navKey) }, onBack = goBack) }
            entry<CourseListRust> { com.unsulliedcode.ui.course.GenericCourseListScreen("rust", onNavigate = { navKey -> backStack.add(navKey) }, onBack = goBack) }
            entry<LessonViewerTheory> { com.unsulliedcode.ui.settings.SettingsSubScreen("LessonViewerTheory", onBack = goBack) }
            entry<LessonViewerVideo> { com.unsulliedcode.ui.settings.SettingsSubScreen("LessonViewerVideo", onBack = goBack) }
            entry<LessonViewerInteractive> { com.unsulliedcode.ui.settings.SettingsSubScreen("LessonViewerInteractive", onBack = goBack) }
            entry<PracticeEditor> { com.unsulliedcode.ui.practice.PracticeEditorScreen(onBack = goBack) }
            entry<PracticeResult> { com.unsulliedcode.ui.practice.PracticeEditorScreen(onBack = goBack) }
            entry<IdeSandbox> { com.unsulliedcode.ui.ide.IdeSandboxScreen(onBack = goBack) }
            entry<IdeJupyterMode> { com.unsulliedcode.ui.ide.IdeSandboxScreen(onBack = goBack) }
            entry<IdeTerminalMode> { com.unsulliedcode.ui.ide.IdeSandboxScreen(onBack = goBack) }
            entry<GitSimulatorLog> { com.unsulliedcode.ui.git.GitSimulatorScreen(onBack = goBack) }
            entry<GitSimulatorCommit> { com.unsulliedcode.ui.git.GitSimulatorScreen(onBack = goBack) }
            entry<GitSimulatorMerge> { com.unsulliedcode.ui.git.GitSimulatorScreen(onBack = goBack) }
            entry<DataScienceLab> { com.unsulliedcode.ui.settings.SettingsSubScreen("DataScienceLab", onBack = goBack) }
            entry<DataScienceDataset> { com.unsulliedcode.ui.settings.SettingsSubScreen("DataScienceDataset", onBack = goBack) }
            entry<GamificationBadges> { com.unsulliedcode.ui.gamification.GamificationBadgesScreen(onBack = goBack) }
            entry<GamificationStreak> { com.unsulliedcode.ui.gamification.GamificationBadgesScreen(onBack = goBack) }
            entry<GamificationRewards> { com.unsulliedcode.ui.gamification.GamificationBadgesScreen(onBack = goBack) }
            entry<Store> { com.unsulliedcode.ui.settings.SettingsSubScreen("Store", onBack = goBack) }
            entry<StoreCheckout> { com.unsulliedcode.ui.settings.SettingsSubScreen("StoreCheckout", onBack = goBack) }
            entry<AuthLogin> { com.unsulliedcode.ui.settings.SettingsSubScreen("AuthLogin", onBack = goBack) }
            entry<AuthSignup> { com.unsulliedcode.ui.settings.SettingsSubScreen("AuthSignup", onBack = goBack) }
            entry<AuthForgotPassword> { com.unsulliedcode.ui.settings.SettingsSubScreen("AuthForgotPassword", onBack = goBack) }
        },
    )
}

