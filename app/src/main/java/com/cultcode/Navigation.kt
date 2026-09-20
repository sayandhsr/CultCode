package com.cultcode

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.cultcode.data.UserProgressRepository
import com.cultcode.ui.course.CourseListScreen
import com.cultcode.ui.home.HomeScreen
import com.cultcode.ui.lesson.LessonViewerScreen
import com.cultcode.ui.onboarding.OnboardingScreen
import com.cultcode.ui.practice.PracticeScreen
import com.cultcode.ui.ide.IdeScreen
import com.cultcode.ui.settings.SettingsScreen

@Composable
fun MainNavigation() {
  val context = LocalContext.current
  val repo = remember { UserProgressRepository(context) }
  val startDestination = if (repo.isOnboardingComplete()) Home else Onboarding
  val backStack = rememberNavBackStack(startDestination)

  NavDisplay(
    backStack = backStack,
    onBack = { backStack.removeLastOrNull() },
    entryProvider =
      entryProvider {
        entry<Home> {
          HomeScreen(onNavigate = { navKey -> backStack.add(navKey) })
        }
        entry<Onboarding> {
            OnboardingScreen(onComplete = { 
                backStack.removeLast() // Remove Onboarding from stack
                backStack.add(Home) 
            })
        }
        entry<CourseList> {
            CourseListScreen(onNavigate = { navKey -> backStack.add(navKey) })
        }
        entry<LessonViewer> {
            LessonViewerScreen(
                courseId = it.courseId, 
                lessonId = it.lessonId,
                onNavigate = { navKey -> backStack.add(navKey) }
            )
        }
        entry<Ide> {
          IdeScreen(initialLanguage = it.initialLanguage, onNavigate = { navKey -> backStack.add(navKey) })
        }
        entry<Practice> {
            PracticeScreen(
                questionId = it.questionId,
                onNavigate = { navKey -> backStack.add(navKey) }
            )
        }
        entry<Profile> {
            SettingsScreen(onBack = { backStack.removeLastOrNull() })
        }
      },
  )
}
