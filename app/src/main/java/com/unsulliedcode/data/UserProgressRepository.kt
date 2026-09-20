package com.unsulliedcode.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class UserProgressRepository(val context: Context) {
    
    private val rootDir = File(context.filesDir, "UnsulliedCode_Data").apply { if (!exists()) mkdirs() }
    private val profileFile = File(rootDir, "user_profile.json")
    
    // We'll keep SharedPreferences specifically for UI configs (Dark theme, monospace, etc)
    // and rely on JSON for the user data as requested by the architect.
    private val prefs = context.getSharedPreferences("cultcode_ui_prefs", Context.MODE_PRIVATE)

    private fun getProfileJson(): JSONObject {
        if (!profileFile.exists()) {
            val defaultJson = JSONObject().apply {
                put("name", "User")
                put("total_xp", 0)
                put("streak_days", 0)
                put("skill_level", "Beginner")
                put("uc_score", 0)
                put("badges", JSONArray().put("First Login"))
                put("selected_tracks", JSONArray().put("Python").put("SQL"))
                put("time_commitment", 15)
                put("onboarding_complete", false)
            }
            profileFile.writeText(defaultJson.toString())
            return defaultJson
        }
        return try {
            JSONObject(profileFile.readText())
        } catch (e: Exception) {
            JSONObject()
        }
    }

    private fun saveProfileJson(json: JSONObject) {
        profileFile.writeText(json.toString())
    }

    fun getTotalXp(): Int = getProfileJson().optInt("total_xp", 0)
    
    fun addXp(amount: Int) {
        val json = getProfileJson()
        json.put("total_xp", json.optInt("total_xp", 0) + amount)
        saveProfileJson(json)
    }

    fun getStreak(): Int = getProfileJson().optInt("streak_days", 0)

    fun isOnboardingComplete(): Boolean = getProfileJson().optBoolean("onboarding_complete", false)

    fun saveOnboardingPreferences(userName: String, skillLevel: String, timeCommitmentDays: Int, selectedTracks: Set<String>) {
        val json = getProfileJson()
        json.put("name", userName)
        json.put("skill_level", skillLevel)
        json.put("time_commitment", timeCommitmentDays)
        json.put("onboarding_complete", true)
        
        val tracksArray = JSONArray()
        selectedTracks.forEach { tracksArray.put(it) }
        json.put("selected_tracks", tracksArray)
        
        saveProfileJson(json)
        
        // Ensure curriculum schedule exists
        val scheduleFile = File(rootDir, "curriculum_schedule.json")
        if (!scheduleFile.exists()) {
            val sched = JSONObject()
            sched.put("status", "active")
            sched.put("days", timeCommitmentDays)
            scheduleFile.writeText(sched.toString(4))
        }
    }

    fun getSkillLevel(): String = getProfileJson().optString("skill_level", "Beginner")
    
    fun getTimeCommitment(): Int = getProfileJson().optInt("time_commitment", 15)
    
    fun getSelectedTracks(): Set<String> {
        val json = getProfileJson()
        val array = json.optJSONArray("selected_tracks")
        val set = mutableSetOf<String>()
        if (array != null) {
            for (i in 0 until array.length()) {
                set.add(array.getString(i))
            }
        } else {
            set.add("Python")
            set.add("SQL")
        }
        return set
    }

    fun getUcScore(): Int = getProfileJson().optInt("uc_score", 0)
    
    fun addUcScore(points: Int) {
        val json = getProfileJson()
        json.put("uc_score", json.optInt("uc_score", 0) + points)
        saveProfileJson(json)
    }

    fun getBadges(): Set<String> {
        val json = getProfileJson()
        val array = json.optJSONArray("badges")
        val set = mutableSetOf<String>()
        if (array != null) {
            for (i in 0 until array.length()) {
                set.add(array.getString(i))
            }
        }
        return set
    }
    
    fun addBadge(badge: String) {
        val json = getProfileJson()
        val array = json.optJSONArray("badges") ?: JSONArray()
        var exists = false
        for (i in 0 until array.length()) {
            if (array.getString(i) == badge) exists = true
        }
        if (!exists) {
            array.put(badge)
            json.put("badges", array)
            saveProfileJson(json)
        }
    }

    // UI Preferences stay in SharedPreferences
    fun isDarkTheme(): Boolean = prefs.getBoolean("dark_theme", true)
    fun setDarkTheme(isDark: Boolean) = prefs.edit().putBoolean("dark_theme", isDark).apply()

    fun isMonospace(): Boolean = prefs.getBoolean("use_monospace", true)
    fun setMonospace(isMono: Boolean) = prefs.edit().putBoolean("use_monospace", isMono).apply()
}
