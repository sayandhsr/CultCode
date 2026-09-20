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
        prefs.edit().putBoolean("lesson_\$lessonId", true).apply()
    }

    fun isLessonComplete(lessonId: String): Boolean = prefs.getBoolean("lesson_\$lessonId", false)
    
    fun getStreak(): Int = prefs.getInt("streak_days", 1) // Default to 1 for demo
}

