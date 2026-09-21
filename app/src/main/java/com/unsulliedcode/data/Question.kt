package com.unsulliedcode.data

enum class QuestionType {
    MULTIPLE_CHOICE,
    CODE_CHALLENGE
}

data class Question(
    val id: String,
    val languageId: String,
    val lessonId: String,
    val type: QuestionType,
    val text: String, // Maps to 'question' in old code challenges
    val explanation: String,
    val difficulty: String,
    val xpReward: Int,
    
    // Multiple Choice specific
    val options: List<String> = emptyList(),
    val correctIndex: Int = -1,
    
    // Code Challenge specific
    val code: String = "",
    val correctAnswer: String = "",
    val expectedOutput: String = "",
    val acceptedAnswers: List<String> = emptyList(),
    val hints: List<String> = emptyList(),
    val solution: String = ""
)
