package com.cultcode.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStreamReader

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
        val fileName = if (id.startsWith("PY-")) "python_questions.json" else "sql_questions.json"
        
        return try {
            val stream = context.assets.open(fileName)
            val jsonString = InputStreamReader(stream).readText()
            val jsonArray = JSONArray(jsonString)
            
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                if (obj.getString("id") == id) {
                    val acceptedAnswers = mutableListOf<String>()
                    val acceptedArray = obj.getJSONArray("acceptedAnswers")
                    for (j in 0 until acceptedArray.length()) {
                        acceptedAnswers.add(acceptedArray.getString(j))
                    }
                    
                    val hints = mutableListOf<String>()
                    val hintsArray = obj.getJSONArray("hints")
                    for (j in 0 until hintsArray.length()) {
                        hints.add(hintsArray.getString(j))
                    }
                    
                    return Question(
                        id = obj.getString("id"),
                        question = obj.getString("question"),
                        code = obj.getString("code"),
                        correctAnswer = obj.getString("correctAnswer"),
                        acceptedAnswers = acceptedAnswers,
                        hints = hints,
                        solution = obj.getString("solution"),
                        explanation = obj.getString("explanation")
                    )
                }
            }
            null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
