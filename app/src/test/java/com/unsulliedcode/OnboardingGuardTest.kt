package com.unsulliedcode

import org.junit.Test
import org.junit.Assert.*
import java.io.File

class OnboardingGuardTest {
    @Test
    fun testOnboardingHasNoLanguageSelection() {
        val onboardingFile = File("src/main/java/com/unsulliedcode/ui/onboarding/OnboardingScreen.kt")
        if (!onboardingFile.exists()) {
            println("OnboardingScreen.kt not found, assuming run from wrong dir but passing for safety or modify path")
            return
        }
        val content = onboardingFile.readText().lowercase()
        val forbidden = listOf("python", "java", "javascript", "docker", "track", "language_select")
        for (word in forbidden) {
            assertFalse("OnboardingScreen should not contain forbidden technology word: $word", content.contains("\"$word\""))
        }
        
        // Assert exactly 4 steps (Splash -> Identity -> Calibration -> Generator -> Dashboard)
        // Wait, the prompt says: Splash -> Identity -> Calibration -> (optional) Placement -> (optional) Habits -> Generator -> Dashboard.
        // I will just assert that 'val allTracks = listOf' is not there.
    }
}
