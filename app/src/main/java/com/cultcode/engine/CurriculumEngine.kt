package com.cultcode.engine

object CurriculumEngine {
    
    data class DailyModule(val day: Int, val title: String, val practiceId: String)

    fun generateRoadmap(courseId: String, userLevel: String, timeframeDays: Int): List<DailyModule> {
        val modules = mutableListOf<DailyModule>()
        
        // Dynamic pacing algorithm
        val paceMultiplier: Double = when (timeframeDays) {
            15 -> 2.0 // Fast pace, pack more topics per day or skip basics
            30 -> 1.0 // Normal pace
            60 -> 0.5 // Slow pace, spread topics
            else -> 1.0
        }
        
        val prefix = when(courseId.lowercase()) {
            "sql" -> "SQL-AUTO"
            "javascript", "js" -> "JS-AUTO"
            else -> "PY-AUTO"
        }
        
        val startingIndex = when (userLevel.lowercase()) {
            "beginner" -> 2
            "intermediate" -> 15
            "advanced" -> 30
            else -> 2
        }

        for (day in 1..timeframeDays) {
            val adjustedTopicIndex = startingIndex + (day.toDouble() * paceMultiplier).toInt()
            val safeIndex = if (adjustedTopicIndex > 50) 50 else adjustedTopicIndex
            val practiceId = "$prefix-${safeIndex.toString().padStart(3, '0')}"
            
            val title = if (day == 1 && userLevel == "Beginner") "Introduction to $courseId"
                        else if (day == timeframeDays) "Final Project & Assessment"
                        else "Advanced Concept #$safeIndex"
            
            modules.add(DailyModule(day, title, practiceId))
        }
        
        return modules
    }
}
