package com.unsulliedcode

import org.junit.Test
import org.junit.Assert.*
import java.io.File

class SettingsDataLossGuardTest {
    
    @Test
    fun testSettingsScreenDoesNotHardcodeUser() {
        val settingsFile = File("src/main/java/com/unsulliedcode/ui/settings/SettingsScreen.kt")
        if (!settingsFile.exists()) {
            return
        }
        val content = settingsFile.readText()
        
        // Assert that the file does not hardcode the string "User" in saveOnboardingPreferences
        // as that would clobber the user's real name.
        val hasHardcodedUser = content.contains("saveOnboardingPreferences(\"User\"")
        assertFalse("SettingsScreen must not overwrite data with hardcoded \"User\"", hasHardcodedUser)
        
        val hasHardcoded30 = content.contains("saveOnboardingPreferences(userName, skillLevel, 30")
        assertFalse("SettingsScreen must not overwrite time commitment with hardcoded 30", hasHardcoded30)
    }
}
