package com.unsulliedcode.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

/**
 * v3.0 Phase 3: Data Layer — curriculum content, lessons, and question banks.
 * All data is stored as JSON files in the app's internal storage for offline-first access.
 */
class ContentRepository(private val context: Context) {

    private val contentDir = File(context.filesDir, "UnsulliedCode_Data/content").apply { if (!exists()) mkdirs() }

    data class Language(
        val id: String,
        val name: String,
        val category: String,
        val icon: String,
        val lessonCount: Int,
        val questionCount: Int
    )

    data class Lesson(
        val id: String,
        val languageId: String,
        val title: String,
        val order: Int,
        val content: String,
        val codeExample: String,
        val difficulty: String
    )



    fun getLanguages(): List<Language> = listOf(
        Language("python", "Python", "Core Programming", "🐍", 25, 100),
        Language("javascript", "JavaScript", "Core Programming", "🟨", 25, 100),
        Language("java", "Java", "Core Programming", "☕", 25, 100),
        Language("c", "C", "Core Programming", "⚙️", 20, 80),
        Language("cpp", "C++", "Core Programming", "🔧", 20, 80),
        Language("html", "HTML & CSS", "Core Programming", "🌐", 15, 60),
        Language("yaml", "YAML", "Core Programming", "📄", 10, 40),
        Language("go", "Go", "Core Programming", "🐹", 20, 80),
        Language("rust", "Rust", "Core Programming", "🦀", 20, 80),
        Language("numpy", "NumPy", "Data Science", "🔢", 15, 60),
        Language("pandas", "Pandas", "Data Science", "🐼", 15, 60),
        Language("docker", "Docker", "DevOps & Cloud", "🐳", 15, 60),
        Language("k8s", "Kubernetes", "DevOps & Cloud", "☸️", 15, 60),
        Language("sql", "SQL", "Database Engineering", "🗃️", 20, 80),
        Language("mongo", "MongoDB", "Database Engineering", "🍃", 15, 60)
    )

    fun getLessonsForLanguage(languageId: String): List<Lesson> {
        val file = File(contentDir, "${languageId}_lessons.json")
        if (!file.exists()) {
            seedLessons(languageId, file)
        }
        return try {
            val array = JSONArray(file.readText())
            (0 until array.length()).map { i ->
                val obj = array.getJSONObject(i)
                Lesson(
                    id = obj.getString("id"),
                    languageId = obj.getString("language_id"),
                    title = obj.getString("title"),
                    order = obj.getInt("order"),
                    content = obj.getString("content"),
                    codeExample = obj.getString("code_example"),
                    difficulty = obj.getString("difficulty")
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun getQuestionsForLesson(languageId: String, lessonId: String): List<Question> {
        val file = File(contentDir, "${languageId}_questions.json")
        if (!file.exists()) {
            seedQuestions(languageId, file)
        }
        return try {
            val array = JSONArray(file.readText())
            (0 until array.length()).mapNotNull { i ->
                val obj = array.getJSONObject(i)
                if (obj.getString("lesson_id") == lessonId) {
                    val optArray = obj.getJSONArray("options")
                    Question(
                        id = obj.getString("id"),
                        languageId = obj.getString("language_id"),
                        lessonId = obj.getString("lesson_id"),
                        type = QuestionType.MULTIPLE_CHOICE,
                        text = obj.getString("text"),
                        options = (0 until optArray.length()).map { optArray.getString(it) },
                        correctIndex = obj.getInt("correct_index"),
                        explanation = obj.getString("explanation"),
                        difficulty = obj.getString("difficulty"),
                        xpReward = obj.getInt("xp_reward")
                    )
                } else null
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun seedLessons(languageId: String, file: File) {
        val lessons = when (languageId) {
            "python" -> listOf(
                lesson("py_01", languageId, "Hello World", 1, "Learn how to print your first message in Python.", "print(\"Hello, World!\")", "easy"),
                lesson("py_02", languageId, "Variables & Types", 2, "Understand how Python stores data using variables and basic data types like int, float, str, and bool.", "name = \"Alice\"\nage = 30\npi = 3.14\nis_active = True", "easy"),
                lesson("py_03", languageId, "Strings", 3, "Master string operations: slicing, formatting, and common methods.", "greeting = \"Hello\"\nprint(greeting.upper())\nprint(f\"Name: {name}\")", "easy"),
                lesson("py_04", languageId, "Lists", 4, "Learn about Python's most versatile data structure — the list.", "fruits = [\"apple\", \"banana\", \"cherry\"]\nfruits.append(\"date\")\nprint(fruits[0])", "easy"),
                lesson("py_05", languageId, "Conditionals", 5, "Control program flow with if/elif/else statements.", "x = 10\nif x > 5:\n    print(\"Big\")\nelif x == 5:\n    print(\"Equal\")\nelse:\n    print(\"Small\")", "easy"),
                lesson("py_06", languageId, "Loops", 6, "Iterate with for and while loops.", "for i in range(5):\n    print(i)\n\ncount = 0\nwhile count < 3:\n    count += 1", "medium"),
                lesson("py_07", languageId, "Functions", 7, "Define reusable blocks of code with functions.", "def greet(name):\n    return f\"Hello, {name}!\"\n\nprint(greet(\"World\"))", "medium"),
                lesson("py_08", languageId, "Dictionaries", 8, "Store key-value pairs with Python dictionaries.", "person = {\"name\": \"Alice\", \"age\": 30}\nprint(person[\"name\"])\nperson[\"city\"] = \"NYC\"", "medium"),
                lesson("py_09", languageId, "List Comprehensions", 9, "Write concise list transformations.", "squares = [x**2 for x in range(10)]\nevens = [x for x in range(20) if x % 2 == 0]", "medium"),
                lesson("py_10", languageId, "Classes & OOP", 10, "Object-oriented programming fundamentals.", "class Dog:\n    def __init__(self, name):\n        self.name = name\n    def bark(self):\n        return f\"{self.name} says Woof!\"", "hard")
            )
            "javascript" -> listOf(
                lesson("js_01", languageId, "Hello World", 1, "Your first JavaScript program.", "console.log(\"Hello, World!\");", "easy"),
                lesson("js_02", languageId, "Variables", 2, "const, let, and var.", "const PI = 3.14;\nlet count = 0;\ncount++;", "easy"),
                lesson("js_03", languageId, "Functions", 3, "Function declarations and arrow functions.", "const add = (a, b) => a + b;\nconsole.log(add(2, 3));", "easy"),
                lesson("js_04", languageId, "Arrays", 4, "Working with arrays and array methods.", "const nums = [1, 2, 3];\nconst doubled = nums.map(n => n * 2);", "medium"),
                lesson("js_05", languageId, "Objects", 5, "JavaScript object literals and destructuring.", "const user = { name: \"Alice\", age: 30 };\nconst { name, age } = user;", "medium")
            )
            "sql" -> listOf(
                lesson("sql_01", languageId, "SELECT Basics", 1, "Query data from a table.", "SELECT name, age FROM users\nWHERE age > 18\nORDER BY name;", "easy"),
                lesson("sql_02", languageId, "Filtering with WHERE", 2, "Use WHERE clauses to filter results.", "SELECT * FROM products\nWHERE price > 10.00\nAND category = 'Electronics';", "easy"),
                lesson("sql_03", languageId, "JOIN Operations", 3, "Combine data from multiple tables.", "SELECT u.name, o.total\nFROM users u\nINNER JOIN orders o ON u.id = o.user_id;", "medium"),
                lesson("sql_04", languageId, "Aggregation", 4, "GROUP BY and aggregate functions.", "SELECT category, COUNT(*), AVG(price)\nFROM products\nGROUP BY category\nHAVING COUNT(*) > 5;", "medium"),
                lesson("sql_05", languageId, "Subqueries", 5, "Nested queries and correlated subqueries.", "SELECT name FROM users\nWHERE id IN (\n  SELECT user_id FROM orders\n  WHERE total > 100\n);", "hard")
            )
            else -> listOf(
                lesson("${languageId}_01", languageId, "Introduction", 1, "Getting started with $languageId.", "// Hello from $languageId!", "easy"),
                lesson("${languageId}_02", languageId, "Basics", 2, "Core concepts of $languageId.", "// Core concepts", "easy"),
                lesson("${languageId}_03", languageId, "Intermediate", 3, "Building on the fundamentals.", "// Intermediate content", "medium")
            )
        }
        val array = JSONArray()
        lessons.forEach { array.put(it) }
        file.writeText(array.toString(2))
    }

    private fun lesson(id: String, langId: String, title: String, order: Int, content: String, code: String, difficulty: String): JSONObject {
        return JSONObject().apply {
            put("id", id)
            put("language_id", langId)
            put("title", title)
            put("order", order)
            put("content", content)
            put("code_example", code)
            put("difficulty", difficulty)
        }
    }

    private fun seedQuestions(languageId: String, file: File) {
        val questions = when (languageId) {
            "python" -> listOf(
                question("pyq_01", languageId, "py_01", "What is the output of print(\"Hello\")?", listOf("Hello", "print(Hello)", "\"Hello\"", "Error"), 0, "print() outputs the string content without quotes.", "easy", 10),
                question("pyq_02", languageId, "py_02", "What type is the value 3.14?", listOf("int", "str", "float", "bool"), 2, "Decimal numbers in Python are float type.", "easy", 10),
                question("pyq_03", languageId, "py_03", "What does 'hello'.upper() return?", listOf("Hello", "HELLO", "hello", "hELLO"), 1, ".upper() converts all characters to uppercase.", "easy", 10),
                question("pyq_04", languageId, "py_05", "What prints when x=3: if x>5: print('A') else: print('B')?", listOf("A", "B", "AB", "Error"), 1, "Since 3 is not greater than 5, the else branch executes.", "easy", 10),
                question("pyq_05", languageId, "py_07", "What does def mean in Python?", listOf("Define a variable", "Define a function", "Delete a function", "Defer execution"), 1, "def is the keyword to define (create) a function.", "easy", 10)
            )
            "sql" -> listOf(
                question("sqlq_01", languageId, "sql_01", "Which clause filters rows?", listOf("SELECT", "FROM", "WHERE", "ORDER BY"), 2, "WHERE filters rows based on conditions.", "easy", 10),
                question("sqlq_02", languageId, "sql_03", "Which JOIN returns only matching rows?", listOf("LEFT JOIN", "RIGHT JOIN", "INNER JOIN", "FULL JOIN"), 2, "INNER JOIN returns rows that have matching values in both tables.", "medium", 15),
                question("sqlq_03", languageId, "sql_04", "What does COUNT(*) return?", listOf("Sum of values", "Number of rows", "Average value", "Maximum value"), 1, "COUNT(*) returns the total number of rows.", "easy", 10)
            )
            else -> listOf(
                question("${languageId}q_01", languageId, "${languageId}_01", "What is $languageId used for?", listOf("Web development", "General programming", "Data science", "All of the above"), 3, "$languageId has many applications.", "easy", 10)
            )
        }
        val array = JSONArray()
        questions.forEach { array.put(it) }
        file.writeText(array.toString(2))
    }

    private fun question(id: String, langId: String, lessonId: String, text: String, options: List<String>, correct: Int, explanation: String, difficulty: String, xp: Int): JSONObject {
        return JSONObject().apply {
            put("id", id)
            put("language_id", langId)
            put("lesson_id", lessonId)
            put("text", text)
            put("options", JSONArray(options))
            put("correct_index", correct)
            put("explanation", explanation)
            put("difficulty", difficulty)
            put("xp_reward", xp)
        }
    }
}
