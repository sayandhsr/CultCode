package com.unsulliedcode.engine

/**
 * v3.0 Phase 4: Evaluation Engine — grades user answers, tracks accuracy,
 * awards XP, and manages spaced repetition scheduling.
 */
class EvaluationEngine {

    data class EvalResult(
        val isCorrect: Boolean,
        val xpEarned: Int,
        val streakBonus: Int,
        val explanation: String
    )

    private var currentStreak = 0

    fun evaluateAnswer(
        selectedIndex: Int,
        correctIndex: Int,
        baseXp: Int,
        explanation: String
    ): EvalResult {
        val isCorrect = selectedIndex == correctIndex

        if (isCorrect) {
            currentStreak++
        } else {
            currentStreak = 0
        }

        val streakBonus = if (currentStreak >= 3) {
            (baseXp * 0.5 * (currentStreak - 2).coerceAtMost(5)).toInt()
        } else 0

        val xpEarned = if (isCorrect) baseXp + streakBonus else 0

        return EvalResult(
            isCorrect = isCorrect,
            xpEarned = xpEarned,
            streakBonus = streakBonus,
            explanation = explanation
        )
    }

    fun getStreak() = currentStreak
    fun resetStreak() { currentStreak = 0 }

    /**
     * Spaced repetition scheduling using SM-2 algorithm variant.
     * Returns the next review interval in days.
     */
    fun calculateNextReview(
        consecutiveCorrect: Int,
        easeFactor: Float = 2.5f
    ): Int {
        return when (consecutiveCorrect) {
            0 -> 1
            1 -> 1
            2 -> 3
            else -> {
                val interval = (consecutiveCorrect * easeFactor).toInt()
                interval.coerceAtMost(30) // Cap at 30 days
            }
        }
    }

    /**
     * Difficulty calibration — adjusts question difficulty based on accuracy.
     */
    fun suggestDifficulty(
        totalAnswered: Int,
        correctAnswered: Int
    ): String {
        if (totalAnswered < 5) return "easy"
        val accuracy = correctAnswered.toFloat() / totalAnswered
        return when {
            accuracy >= 0.9 -> "expert"
            accuracy >= 0.75 -> "hard"
            accuracy >= 0.5 -> "medium"
            else -> "easy"
        }
    }
}
