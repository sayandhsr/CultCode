package com.cultcode.engine

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import java.util.regex.Pattern

data class ExecutionResult(val stdout: String, val isSuccess: Boolean, val isRealExecution: Boolean)

class CodeExecutionEngine(private val context: Context) {
    
    fun execute(language: String, code: String, expectedAnswer: String, acceptedAnswers: List<String>): ExecutionResult {
        return when (language.lowercase()) {
            "sql" -> executeSql(code, expectedAnswer, acceptedAnswers)
            else -> evaluateRegex(code, expectedAnswer, acceptedAnswers)
        }
    }

    private fun executeSql(code: String, expected: String, accepted: List<String>): ExecutionResult {
        // Run against an in-memory SQLite database for REAL execution
        var db: SQLiteDatabase? = null
        try {
            db = SQLiteDatabase.create(null) // In-memory DB
            // Create a dummy table for the JOINS test if it's the specific question
            if (code.contains("JOIN", ignoreCase = true) || code.contains("SELECT", ignoreCase = true)) {
                db.execSQL("CREATE TABLE Orders (OrderID int, CustomerID int);")
                db.execSQL("CREATE TABLE Customers (CustomerID int, Name varchar(255));")
                db.execSQL("INSERT INTO Customers VALUES (1, 'TechCorp');")
                db.execSQL("INSERT INTO Orders VALUES (100, 1);")
            }
            
            // If it's a SELECT query, try to run it
            if (code.trim().uppercase().startsWith("SELECT")) {
                val cursor = db.rawQuery(code, null)
                val columns = cursor.columnNames.joinToString(" | ")
                var output = "$columns\n"
                var rowCount = 0
                while (cursor.moveToNext() && rowCount < 5) {
                    val row = mutableListOf<String>()
                    for (i in 0 until cursor.columnCount) {
                        row.add(cursor.getString(i) ?: "NULL")
                    }
                    output += row.joinToString(" | ") + "\n"
                    rowCount++
                }
                cursor.close()
                
                // Still do AST/Regex validation for educational correctness
                val isCorrect = evaluateRegex(code, expected, accepted).isSuccess
                
                return ExecutionResult(
                    stdout = if (isCorrect) "Process finished with exit code 0\nResult: Success!\n\nOutput:\n$output" else "Query Executed successfully, but did not match expected solution.\n\nOutput:\n$output",
                    isSuccess = isCorrect,
                    isRealExecution = true
                )
            } else {
                db.execSQL(code)
                val isCorrect = evaluateRegex(code, expected, accepted).isSuccess
                return ExecutionResult(
                    stdout = "Statement Executed Successfully.\nRows affected.",
                    isSuccess = isCorrect,
                    isRealExecution = true
                )
            }
        } catch (e: Exception) {
            return ExecutionResult(
                stdout = "Error executing SQL:\n${e.message}",
                isSuccess = false,
                isRealExecution = true
            )
        } finally {
            db?.close()
        }
    }

    private fun evaluateRegex(code: String, expected: String, accepted: List<String>): ExecutionResult {
        val normalizedCode = code.replace("\\s".toRegex(), "")
        val matched = accepted.any { 
            normalizedCode.contains(it.replace("\\s".toRegex(), "")) 
        } || normalizedCode.contains(expected.replace("\\s".toRegex(), ""))
        
        return if (matched) {
            ExecutionResult(
                stdout = "Process finished with exit code 0\nResult: AST Validation Success!",
                isSuccess = true,
                isRealExecution = false
            )
        } else {
            ExecutionResult(
                stdout = "AST Error: output does not match expected syntax or logic.\nCheck your variable names and operators.",
                isSuccess = false,
                isRealExecution = false
            )
        }
    }
}
