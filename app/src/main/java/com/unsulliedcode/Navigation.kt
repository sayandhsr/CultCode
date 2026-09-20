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
            entry<Splash> { StubScreen("Splash", onBack = goBack) }
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
            entry<Settings> { StubScreen("Settings", onBack = goBack) }
            entry<SettingsTheme> { StubScreen("SettingsTheme", onBack = goBack) }
            entry<SettingsAccount> { StubScreen("SettingsAccount", onBack = goBack) }
            entry<SettingsNotifications> { StubScreen("SettingsNotifications", onBack = goBack) }
            entry<SettingsStorage> { StubScreen("SettingsStorage", onBack = goBack) }
            entry<SettingsAbout> { StubScreen("SettingsAbout", onBack = goBack) }
            entry<DesignGallery> { com.unsulliedcode.ui.debug.DesignGalleryScreen() }
            entry<ArenaMatchmaking> { StubScreen("ArenaMatchmaking", onBack = goBack) }
            entry<ArenaBattle> { StubScreen("ArenaBattle", onBack = goBack) }
            entry<ArenaResult> { StubScreen("ArenaResult", onBack = goBack) }
            entry<ArenaLeaderboard> { StubScreen("ArenaLeaderboard", onBack = goBack) }
            entry<LearnDashboard> { StubScreen("LearnDashboard", onBack = goBack) }
            entry<CourseListPython> { StubScreen("CourseListPython", onBack = goBack) }
            entry<CourseListJava> { StubScreen("CourseListJava", onBack = goBack) }
            entry<CourseListCpp> { StubScreen("CourseListCpp", onBack = goBack) }
            entry<CourseListJavascript> { StubScreen("CourseListJavascript", onBack = goBack) }
            entry<CourseListSql> { StubScreen("CourseListSql", onBack = goBack) }
            entry<CourseListDocker> { StubScreen("CourseListDocker", onBack = goBack) }
            entry<CourseListK8s> { StubScreen("CourseListK8s", onBack = goBack) }
            entry<CourseListPandas> { StubScreen("CourseListPandas", onBack = goBack) }
            entry<CourseListNumpy> { StubScreen("CourseListNumpy", onBack = goBack) }
            entry<CourseListHtml> { StubScreen("CourseListHtml", onBack = goBack) }
            entry<CourseListYaml> { StubScreen("CourseListYaml", onBack = goBack) }
            entry<CourseListMongo> { StubScreen("CourseListMongo", onBack = goBack) }
            entry<CourseListGo> { StubScreen("CourseListGo", onBack = goBack) }
            entry<CourseListRust> { StubScreen("CourseListRust", onBack = goBack) }
            entry<LessonViewerTheory> { StubScreen("LessonViewerTheory", onBack = goBack) }
            entry<LessonViewerVideo> { StubScreen("LessonViewerVideo", onBack = goBack) }
            entry<LessonViewerInteractive> { StubScreen("LessonViewerInteractive", onBack = goBack) }
            entry<PracticeEditor> { StubScreen("PracticeEditor", onBack = goBack) }
            entry<PracticeResult> { StubScreen("PracticeResult", onBack = goBack) }
            entry<IdeSandbox> { StubScreen("IdeSandbox", onBack = goBack) }
            entry<IdeJupyterMode> { StubScreen("IdeJupyterMode", onBack = goBack) }
            entry<IdeTerminalMode> { StubScreen("IdeTerminalMode", onBack = goBack) }
            entry<GitSimulatorLog> { StubScreen("GitSimulatorLog", onBack = goBack) }
            entry<GitSimulatorCommit> { StubScreen("GitSimulatorCommit", onBack = goBack) }
            entry<GitSimulatorMerge> { StubScreen("GitSimulatorMerge", onBack = goBack) }
            entry<DataScienceLab> { StubScreen("DataScienceLab", onBack = goBack) }
            entry<DataScienceDataset> { StubScreen("DataScienceDataset", onBack = goBack) }
            entry<GamificationBadges> { StubScreen("GamificationBadges", onBack = goBack) }
            entry<GamificationStreak> { StubScreen("GamificationStreak", onBack = goBack) }
            entry<GamificationRewards> { StubScreen("GamificationRewards", onBack = goBack) }
            entry<Store> { StubScreen("Store", onBack = goBack) }
            entry<StoreCheckout> { StubScreen("StoreCheckout", onBack = goBack) }
            entry<AuthLogin> { StubScreen("AuthLogin", onBack = goBack) }
            entry<AuthSignup> { StubScreen("AuthSignup", onBack = goBack) }
            entry<AuthForgotPassword> { StubScreen("AuthForgotPassword", onBack = goBack) }
        },
    )
}
