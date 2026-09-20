package com.cultcode

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.cultcode.ui.course.CourseListScreen
import com.cultcode.ui.home.HomeScreen
import com.cultcode.ui.lesson.LessonViewerScreen
import com.cultcode.ui.onboarding.OnboardingScreen
import com.cultcode.ui.practice.PracticeScreen
import com.cultcode.ui.ide.IdeScreen

@Composable
fun MainNavigation() {
  val backStack = rememberNavBackStack(Onboarding) // Start at Onboarding for now

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
                backStack.removeLast()
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
            Text("Profile Placeholder")
        }
      },
  )
}

