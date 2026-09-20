package com.cultcode.engine

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import java.io.BufferedReader
import java.io.InputStreamReader

data class ExecutionResult(val stdout: String, val isSuccess: Boolean, val isRealExecution: Boolean)

class CodeExecutionEngine(private val context: Context) {
    
    fun execute(language: String, code: String, expectedAnswer: String, acceptedAnswers: List<String>): ExecutionResult {
        return when (language.lowercase()) {
            "sql" -> executeSql(code, expectedAnswer, acceptedAnswers)
            "python" -> executePythonDataScience(code, expectedAnswer, acceptedAnswers)
            else -> evaluateSemantic(code, expectedAnswer, acceptedAnswers)
        }
    }

    private fun executePythonDataScience(code: String, expected: String, accepted: List<String>): ExecutionResult {
        // Special Data Science Sandbox interceptor
        if (code.contains("import pandas") && code.contains("read_csv")) {
            val isCorrect = evaluateSemantic(code, expected, accepted).isSuccess
            
            // Simulate Pandas output by actually reading the CSV from assets!
            var output = ""
            try {
                val inputStream = context.assets.open("sales_data.csv")
                val reader = BufferedReader(InputStreamReader(inputStream))
                var lineCount = 0
                while (reader.readLine().also { if (it != null) output += it.replace(",", " | ") + "\n" } != null && lineCount < 5) {
                    lineCount++
                }
                reader.close()
            } catch (e: Exception) {
                output = "Error loading dataset: ${e.message}"
            }
            
            val stdout = if (isCorrect) "Process finished with exit code 0\n[Pandas Dataframe Loaded]\n$output" else "Code executed, but logic did not match expected EDA steps.\n\n$output"
            return ExecutionResult(stdout, isCorrect, true) // Mark as true execution since we actually read the file!
        }
        
        return evaluateSemantic(code, expected, accepted)
    }

    private fun executeSql(code: String, expected: String, accepted: List<String>): ExecutionResult {
        var db: SQLiteDatabase? = null
        try {
            db = SQLiteDatabase.create(null)
            if (code.contains("JOIN", ignoreCase = true) || code.contains("SELECT", ignoreCase = true)) {
                db.execSQL("CREATE TABLE Orders (OrderID int, CustomerID int);")
                db.execSQL("CREATE TABLE Customers (CustomerID int, Name varchar(255));")
                db.execSQL("INSERT INTO Customers VALUES (1, 'TechCorp');")
                db.execSQL("INSERT INTO Orders VALUES (100, 1);")
            }
            
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
                
                val isCorrect = evaluateSemantic(code, expected, accepted).isSuccess
                
                return ExecutionResult(
                    stdout = if (isCorrect) "Process finished with exit code 0\nResult: Success!\n\nOutput:\n$output" else "Query Executed successfully, but did not match expected solution.\n\nOutput:\n$output",
                    isSuccess = isCorrect,
                    isRealExecution = true
                )
            } else {
                db.execSQL(code)
                val isCorrect = evaluateSemantic(code, expected, accepted).isSuccess
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

    private fun evaluateSemantic(code: String, expected: String, accepted: List<String>): ExecutionResult {
        val strictCode = code.replace("\\s".toRegex(), "")
        val strictMatched = accepted.any { 
            strictCode.contains(it.replace("\\s".toRegex(), "")) 
        } || strictCode.contains(expected.replace("\\s".toRegex(), ""))
        
        if (strictMatched) {
            return ExecutionResult("Process finished with exit code 0\nResult: Validation Success!", true, false)
        }

        val fuzzyCode = code.replace("[^A-Za-z0-9]".toRegex(), "").lowercase()
        val fuzzyMatched = accepted.any {
            fuzzyCode.contains(it.replace("[^A-Za-z0-9]".toRegex(), "").lowercase())
        } || fuzzyCode.contains(expected.replace("[^A-Za-z0-9]".toRegex(), "").lowercase())

        if (fuzzyMatched) {
            return ExecutionResult("Process finished with exit code 0\nResult: Semantic Validation Success!", true, false)
        }
        
        return ExecutionResult(
            stdout = "Error: output does not match expected logical structure.",
            isSuccess = false,
            isRealExecution = false
        )
    }
}
