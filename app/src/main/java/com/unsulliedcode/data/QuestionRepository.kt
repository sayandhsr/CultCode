package com.unsulliedcode.data

import android.content.Context
import org.json.JSONObject

class QuestionRepository(private val context: Context) {
    
    fun getQuestion(id: String): Question? {
        val fileName = when {
            id.startsWith("SQL") -> "sql_questions.json"
            id.startsWith("PY") -> "python_questions.json"
            id.startsWith("DSA") -> "python_questions.json"
            id.startsWith("JS") -> "javascript_questions.json"
            id.startsWith("YAM") -> "yaml_questions.json"
            id.startsWith("DOC") -> "docker_questions.json"
            id.startsWith("K8S") -> "k8s_questions.json"
            id.startsWith("JAV") -> "java_questions.json"
            id.startsWith("DAT") -> "data_science_questions.json"
            id.startsWith("HTM") -> "html_questions.json"
            id.startsWith("CSS") -> "css_questions.json"
            id.startsWith("TS") -> "typescript_questions.json"
            id.startsWith("NUM") -> "numpy_questions.json"
            id.startsWith("PAN") -> "pandas_questions.json"
            id.startsWith("MON") -> "mongodb_questions.json"
            id.startsWith("CPP") -> "cpp_questions.json"
            id.startsWith("C-") -> "c_questions.json"
            else -> "python_questions.json"
        }
        
        try {
            val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
            val jsonObject = JSONObject(jsonString)
            val questionsArray = jsonObject.getJSONArray("questions")
            
            for (i in 0 until questionsArray.length()) {
                val q = questionsArray.getJSONObject(i)
                if (q.getString("id") == id) {
                    val acceptedAnswers = mutableListOf<String>()
                    val acceptedArray = q.optJSONArray("acceptedAnswers")
                    if (acceptedArray != null) {
                        for (j in 0 until acceptedArray.length()) {
                            acceptedAnswers.add(acceptedArray.getString(j))
                        }
                    }
                    
                    val hints = mutableListOf<String>()
                    val hintsArray = q.optJSONArray("hints")
                    if (hintsArray != null) {
                        for (j in 0 until hintsArray.length()) {
                            hints.add(hintsArray.getString(j))
                        }
                    }
                    
                    val languageId = fileName.removeSuffix("_questions.json").removeSuffix("_questions")

                    return Question(
                        id = q.getString("id"),
                        languageId = languageId,
                        lessonId = "", // Legacy code challenge JSON doesn't have lesson_id
                        type = QuestionType.CODE_CHALLENGE,
                        text = q.getString("question"),
                        explanation = q.getString("explanation"),
                        difficulty = q.optString("difficulty", "medium"),
                        xpReward = 15,
                        code = q.optString("code", ""),
                        correctAnswer = q.optString("correctAnswer", ""),
                        expectedOutput = q.optString("expectedOutput", "Output unavailable"),
                        acceptedAnswers = acceptedAnswers,
                        hints = hints,
                        solution = q.optString("solution", "")
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}
