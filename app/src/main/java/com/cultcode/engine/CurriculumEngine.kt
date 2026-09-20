package com.cultcode.engine

object CurriculumEngine {
    
    data class DailyModule(val day: Int, val title: String, val practiceId: String)

    fun generateRoadmap(courseId: String, userLevel: String, timeframeDays: Int): List<DailyModule> {
        val modules = mutableListOf<DailyModule>()
        
        val paceMultiplier: Double = when (timeframeDays) {
            15 -> 2.0
            30 -> 1.0
            60 -> 0.5
            else -> 1.0
        }
        
        val prefix = when(courseId.lowercase()) {
            "sql" -> "SQL"
            "javascript", "js" -> "JS"
            else -> "PY"
        }
        
        val levelPrefix = when (userLevel.lowercase()) {
            "beginner" -> "BASICS"
            "intermediate" -> "INTERMEDIATE"
            "advanced" -> "ADVANCED"
            else -> "BASICS"
        }

        val startingIndex = when (userLevel.lowercase()) {
            "beginner" -> 1
            "intermediate" -> 101
            "advanced" -> 201
            else -> 1
        }

        for (day in 1..timeframeDays) {
            val adjustedTopicIndex = startingIndex + (day.toDouble() * paceMultiplier).toInt()
            
            // Limit to max bounds per category
            val upperBound = startingIndex + 99
            val safeIndex = if (adjustedTopicIndex > upperBound) upperBound else adjustedTopicIndex
            
            val practiceId = "$prefix-$levelPrefix-${safeIndex.toString().padStart(3, '0')}"
            
            val title = if (day == 1 && userLevel == "Beginner") "Introduction to $courseId"
                        else if (day == timeframeDays) "Final Project & Assessment"
                        else "Concept #$safeIndex"
            
            modules.add(DailyModule(day, title, practiceId))
        }
        
        return modules
    }
}
