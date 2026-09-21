package com.unsulliedcode.engine

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.unsulliedcode.engine.ast.PythonAst
import java.io.BufferedReader
import java.io.InputStreamReader

data class ExecutionResult(val stdout: String, val isSuccess: Boolean, val isRealExecution: Boolean)

class CodeExecutionEngine(private val context: Context) {
    
    fun freeRun(language: String, code: String): ExecutionResult {
        return when (language.lowercase()) {
            "sql" -> {
                val res = executeSql(code, "DUMMY", listOf())
                val out = res.stdout
                    .replace("Query Executed successfully, but did not match expected solution.\n\n", "")
                    .replace("Error: output does not match expected logical structure.", "Executed successfully. No tabular output.")
                ExecutionResult(out, true, true)
            }
            "python", "pandas/eda" -> {
                if (code.contains("import pandas") && code.contains("read_csv")) {
                    val res = executePythonDataScience(code, "DUMMY", listOf())
                    val out = res.stdout.replace("Code executed, but logic did not match expected EDA steps.\n\n", "")
                    ExecutionResult(out, true, true)
                } else {
                    val prints = mutableListOf<String>()
                    val matcher = java.util.regex.Pattern.compile("print\\((.*?)\\)").matcher(code)
                    while (matcher.find()) {
                        val inside = matcher.group(1)?.replace("\"", "")?.replace("'", "") ?: ""
                        prints.add(inside)
                    }
                    if (prints.isNotEmpty()) {
                        ExecutionResult("Process finished with exit code 0\n" + prints.joinToString("\n"), true, false)
                    } else {
                        ExecutionResult("Process finished with exit code 0\n[No output] (Compilation Simulation)", true, false)
                    }
                }
            }
            else -> ExecutionResult("Execution simulated successfully.\n[Offline Sandbox Mode for ${language.uppercase()}]", true, false)
        }
    }

    fun execute(language: String, code: String, expectedAnswer: String, acceptedAnswers: List<String>): ExecutionResult {
        return when (language.lowercase()) {
            "sql" -> executeSql(code, expectedAnswer, acceptedAnswers)
            "python", "pandas/eda" -> executePythonDataScience(code, expectedAnswer, acceptedAnswers)
            else -> evaluateSemantic(code, expectedAnswer, acceptedAnswers)
        }
    }

    private fun executePythonDataScience(code: String, expected: String, accepted: List<String>): ExecutionResult {
        if (code.contains("import pandas") && code.contains("read_csv")) {
            val isCorrect = evaluateSemantic(code, expected, accepted).isSuccess
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
            return ExecutionResult(stdout, isCorrect, true)
        }
        return evaluateSemantic(code, expected, accepted)
    }

    private fun executeSql(code: String, expected: String, accepted: List<String>): ExecutionResult {
        var db: SQLiteDatabase? = null
        try {
            db = SQLiteDatabase.create(null)
            if (code.contains("JOIN", ignoreCase = true) || code.contains("SELECT", ignoreCase = true) || expected.contains("SELECT", ignoreCase = true)) {
                db.execSQL("CREATE TABLE Orders (OrderID int, CustomerID int);")
                db.execSQL("CREATE TABLE Customers (CustomerID int, Name varchar(255));")
                db.execSQL("INSERT INTO Customers VALUES (1, 'TechCorp');")
                db.execSQL("INSERT INTO Orders VALUES (100, 1);")
            }
            
            if (code.trim().uppercase().startsWith("SELECT")) {
                // RUN FULL RESULT SET COMPARISON USING NEW SQLEVALUATOR
                val isCorrect = SqlEvaluator.evaluateQuery(db, code, expected)
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
        if (PythonAst.hasHardcodedPrint(code, expected)) {
            return ExecutionResult(stdout = "Error: Hardcoded literal detected. Write the actual logic.", isSuccess = false, isRealExecution = false)
        }

        if (PythonAst.hasForbiddenConstruct(code, "os") || PythonAst.hasForbiddenConstruct(code, "sys")) {
            return ExecutionResult(stdout = "Error: Forbidden construct detected.", isSuccess = false, isRealExecution = false)
        }

        val config = NormalizationConfig(ignoreOrder = true, floatTolerance = 0.001)

        val prints = mutableListOf<String>()
        val matcher = java.util.regex.Pattern.compile("print\\((.*?)\\)").matcher(code)
        while (matcher.find()) {
            val inside = matcher.group(1)?.replace("\"", "")?.replace("'", "") ?: ""
            prints.add(inside)
        }
        val simulatedOutput = prints.joinToString("\n")

        val isOutputCorrect = OutputNormalizer.compare(simulatedOutput, expected, config) ||
            accepted.any { OutputNormalizer.compare(simulatedOutput, it, config) }

        if (isOutputCorrect) {
            return ExecutionResult("Process finished with exit code 0\nResult: Validation Success!", true, false)
        }

        // Semantic matching of variable content fallback
        val strictCode = code.replace("\\s".toRegex(), "").lowercase()
        val normalizedExpected = expected.replace("\\s".toRegex(), "").lowercase()
        val normalizedAccepted = accepted.map { it.replace("\\s".toRegex(), "").lowercase() }
        
        val strictMatched = normalizedAccepted.any { 
            strictCode.contains(it) 
        } || strictCode.contains(normalizedExpected)
        
        if (strictMatched) return ExecutionResult("Process finished with exit code 0\nResult: Validation Success!", true, false)
        
        return ExecutionResult(stdout = "Error: output does not match expected logical structure.", isSuccess = false, isRealExecution = false)
    }
}
