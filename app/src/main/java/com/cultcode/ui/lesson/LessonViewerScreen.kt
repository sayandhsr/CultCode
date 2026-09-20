package com.cultcode.ui.lesson

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.cultcode.Practice

data class LessonContent(val title: String, val objective: String, val explanation: String, val code: String, val mistakes: String, val practiceId: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonViewerScreen(courseId: String, lessonId: String, onNavigate: (NavKey) -> Unit) {
    
    val lesson = remember(courseId, lessonId) {
        when(courseId.lowercase()) {
            "sql" -> LessonContent(
                "SQL Joins", "Understand how to combine rows from multiple tables.",
                "A JOIN clause is used to combine rows from two or more tables, based on a related column between them.",
                "SELECT * FROM Orders\nJOIN Customers\nON Orders.CustomerID = Customers.CustomerID;",
                "Forgetting the ON clause which causes a cross join.", "SQL-MEDIUM-JOINS-001"
            )
            "javascript" -> LessonContent(
                "JS Variables", "Learn var, let, and const.",
                "let allows you to declare variables that are limited to the scope of a block statement. const is similar, but its value cannot be reassigned.",
                "let name = \"Dev\";\nconst pi = 3.14;",
                "Reassigning a const variable throws an error.", "JS-BASICS-001"
            )
            "docker" -> LessonContent(
                "Docker Containers", "Learn how to containerize apps.",
                "A container is a standard unit of software that packages up code and all its dependencies so the application runs quickly and reliably from one computing environment to another.",
                "docker build -t myapp .\ndocker run -d -p 8080:80 myapp",
                "Forgetting to expose the port when running the container.", "DOC-BASICS-001"
            )
            "kubernetes" -> LessonContent(
                "K8s Pods", "Understand the smallest deployable unit.",
                "Pods are the smallest deployable units of computing that you can create and manage in Kubernetes. A Pod contains one or more containers.",
                "kubectl get pods\nkubectl describe pod my-pod",
                "Assuming a Pod is a VM. Pods are ephemeral.", "K8S-BASICS-001"
            )
            "yaml" -> LessonContent(
                "YAML Configuration", "Understand YAML syntax.",
                "YAML is a human-readable data-serialization language. It is commonly used for configuration files.",
                "server:\n  port: 8080",
                "Using tabs instead of spaces.", "YAM-BASICS-001"
            )
            "java" -> LessonContent(
                "Java Basics", "Learn Java syntax.",
                "Java is a high-level, class-based, object-oriented programming language.",
                "class Main {\n  public static void main(String[] args) {\n    System.out.println(\"Hello World\");\n  }\n}",
                "Forgetting semicolons.", "JAV-BASICS-001"
            )
            "pandas/eda" -> LessonContent(
                "Data Science & EDA", "Learn Exploratory Data Analysis.",
                "Pandas is an essential Python library for loading, analyzing, and cleaning datasets. You'll often start by loading a CSV and inspecting the head.",
                "import pandas as pd\ndf = pd.read_csv('sales_data.csv')\nprint(df.head())",
                "Forgetting to import pandas.", "DAT-EDA-001"
            )
            "html/css" -> LessonContent(
                "Frontend Fundamentals", "Learn HTML tags.",
                "HTML builds the skeleton of the web.",
                "<h1>Hello</h1>",
                "Forgetting closing tags.", "HTM-BASICS-001"
            )
            "typescript" -> LessonContent(
                "Strict Typing", "Learn TS Interfaces.",
                "TS adds types to JS.",
                "interface User { name: string }",
                "Mixing up type syntax.", "TS-BASICS-001"
            )
            else -> LessonContent(
                "Python Variables", "Understand how to store data in variables.",
                "Variables are containers for storing data values. In Python, you do not need to declare a variable before using it.",
                "name = \"Developer\"\nscore = 10",
                "Using a double equals sign (==) for assignment instead of a single equals sign (=).", "PY-BASICS-001"
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = { IconButton(onClick = { onNavigate(com.cultcode.CourseList) }) { Text("<") } },
                title = { Text(courseId.replaceFirstChar { it.uppercase() }) },
                actions = { Text("UnsulliedCode ", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary) }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { onNavigate(Practice(lesson.practiceId)) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Text("Start Practice")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(lesson.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    IconButton(onClick = { /* TTS Placeholder */ }) {
                        Text("🔊")
                    }
                }
                Text("Objective: ${lesson.objective}", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            
            item {
                Text("Explanation", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(8.dp))
                Text(lesson.explanation)
            }
            
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        lesson.code, 
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            item {
                Text("Common Mistakes", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(8.dp))
                Text(lesson.mistakes)
            }
            
            item { Spacer(modifier = Modifier.height(64.dp)) }
        }
    }
}
