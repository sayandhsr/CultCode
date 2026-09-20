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
    
    fun getStreak(): Int = prefs.getInt("streak_days", 0) // Initialize correctly at 0 for new users
    
    // Personalization Methods
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
}
