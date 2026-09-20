package com.cultcode.data

import android.content.Context
import android.content.SharedPreferences

class UserProgressRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("cultcode_progress", Context.MODE_PRIVATE)

    fun addXp(amount: Int) {
        val current = prefs.getInt("total_xp", 0)
        prefs.edit().putInt("total_xp", current + amount).apply()
    }

    fun getTotalXp(): Int = prefs.getInt("total_xp", 0)

    fun markLessonComplete(lessonId: String) {
        prefs.edit().putBoolean("lesson_$lessonId", true).apply()
    }

    fun isLessonComplete(lessonId: String): Boolean = prefs.getBoolean("lesson_$lessonId", false)
    
    fun getStreak(): Int = prefs.getInt("streak_days", 0)
    
    fun saveOnboardingPreferences(skillLevel: String, timeCommitmentDays: Int, selectedTracks: Set<String>) {
        prefs.edit()
            .putString("skill_level", skillLevel)
            .putInt("time_commitment", timeCommitmentDays)
            .putStringSet("selected_tracks", selectedTracks)
            .putBoolean("onboarding_complete", true)
            .apply()
    }
    
    fun isOnboardingComplete(): Boolean = prefs.getBoolean("onboarding_complete", false)
    fun getSkillLevel(): String = prefs.getString("skill_level", "Beginner") ?: "Beginner"
    fun getTimeCommitment(): Int = prefs.getInt("time_commitment", 15)
    fun getSelectedTracks(): Set<String> = prefs.getStringSet("selected_tracks", setOf("Python", "SQL")) ?: setOf("Python", "SQL")

    fun isDarkTheme(): Boolean = prefs.getBoolean("dark_theme", true)
    fun setDarkTheme(isDark: Boolean) = prefs.edit().putBoolean("dark_theme", isDark).apply()

    fun isMonospace(): Boolean = prefs.getBoolean("use_monospace", true)
    fun setMonospace(isMono: Boolean) = prefs.edit().putBoolean("use_monospace", isMono).apply()

    fun getUcScore(): Int = prefs.getInt("uc_score", 0)
    fun addUcScore(points: Int) = prefs.edit().putInt("uc_score", getUcScore() + points).apply()

    fun getBadges(): Set<String> = prefs.getStringSet("badges", setOf("First Login")) ?: setOf("First Login")
    fun addBadge(badge: String) = prefs.edit().putStringSet("badges", getBadges() + badge).apply()
}
