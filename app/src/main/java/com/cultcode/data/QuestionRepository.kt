package com.cultcode.data

import android.content.Context
import org.json.JSONObject

data class Question(
    val id: String,
    val question: String,
    val code: String,
    val correctAnswer: String,
    val acceptedAnswers: List<String>,
    val hints: List<String>,
    val solution: String,
    val explanation: String
)

class QuestionRepository(private val context: Context) {
    
    fun getQuestion(id: String): Question? {
        // Determine the correct file based on ID prefix
        val fileName = when {
            id.startsWith("SQL") -> "sql_questions.json"
            id.startsWith("PY") -> "python_questions.json"
            id.startsWith("YAM") -> "yaml_questions.json"
            id.startsWith("DOC") -> "docker_questions.json"
            id.startsWith("K8S") -> "k8s_questions.json"
            id.startsWith("JAV") -> "java_questions.json"
            id.startsWith("DAT") -> "data_science_questions.json"
            id.startsWith("HTM") -> "html_questions.json"
            id.startsWith("CSS") -> "css_questions.json"
            id.startsWith("TS") -> "typescript_questions.json"
            else -> "python_questions.json" // Fallback
        }
        
        try {
            val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
            val jsonObject = JSONObject(jsonString)
            val questionsArray = jsonObject.getJSONArray("questions")
            
            for (i in 0 until questionsArray.length()) {
                val q = questionsArray.getJSONObject(i)
                if (q.getString("id") == id) {
                    val acceptedAnswers = mutableListOf<String>()
                    val acceptedArray = q.getJSONArray("acceptedAnswers")
                    for (j in 0 until acceptedArray.length()) {
                        acceptedAnswers.add(acceptedArray.getString(j))
                    }
                    
                    val hints = mutableListOf<String>()
                    val hintsArray = q.getJSONArray("hints")
                    for (j in 0 until hintsArray.length()) {
                        hints.add(hintsArray.getString(j))
                    }
                    
                    return Question(
                        id = q.getString("id"),
                        question = q.getString("question"),
                        code = q.getString("code"),
                        correctAnswer = q.getString("correctAnswer"),
                        acceptedAnswers = acceptedAnswers,
                        hints = hints,
                        solution = q.getString("solution"),
                        explanation = q.getString("explanation")
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}
