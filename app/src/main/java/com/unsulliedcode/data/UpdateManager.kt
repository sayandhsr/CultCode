package com.unsulliedcode.data

import android.content.Context
import org.json.JSONObject
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UpdateManager(private val context: Context) {

    suspend fun updateQuestions(languageId: String, newJsonData: String): Boolean = withContext(Dispatchers.IO) {
        val rootDir = File(context.filesDir, "UnsulliedCode_Data/content")
        if (!rootDir.exists()) rootDir.mkdirs()

        val activeFile = File(rootDir, "${languageId}_questions.json")
        val tempFile = File(rootDir, "${languageId}_questions.json.tmp")

        try {
            // FULL DOWNLOAD COMPLETED -> PARSE -> VALIDATE
            val jsonObject = JSONObject(newJsonData)
            val questionsArray = jsonObject.optJSONArray("questions")
            
            // CHECK QUESTION COUNT
            if (questionsArray == null || questionsArray.length() == 0) {
                return@withContext false
            }

            // CHECK REQUIRED FIELDS & DATA INTEGRITY
            for (i in 0 until questionsArray.length()) {
                val q = questionsArray.optJSONObject(i)
                if (q == null) return@withContext false
                
                val id = q.optString("id")
                val text = q.optString("question")
                val correctAnswer = q.optString("correctAnswer")
                
                if (id.isBlank() || text.isBlank() || correctAnswer.isBlank()) {
                    return@withContext false
                }
            }

            // IF VALID -> ATOMICALLY REPLACE OLD DATA
            tempFile.writeText(newJsonData)
            
            if (activeFile.exists()) {
                activeFile.delete()
            }
            
            val success = tempFile.renameTo(activeFile)
            return@withContext success
        } catch (e: Exception) {
            // IF INVALID -> KEEP OLD VALID DATA
            e.printStackTrace()
            if (tempFile.exists()) {
                tempFile.delete()
            }
            return@withContext false
        }
    }
}
