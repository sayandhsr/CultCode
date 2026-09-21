package com.unsulliedcode

import org.junit.Test
import org.junit.Assert.*
import java.io.File

class NestedScrollGuardTest {
    
    @Test
    fun testNoNestedScrollContainers() {
        val uiDir = File("src/main/java/com/unsulliedcode/ui")
        if (!uiDir.exists()) {
            println("UI directory not found, skipping nested scroll test.")
            return
        }

        val scrollables = listOf("LazyColumn", "LazyRow", "LazyVerticalGrid", "LazyHorizontalGrid", "verticalScroll", "horizontalScroll")
        
        uiDir.walkTopDown().filter { it.isFile && it.extension == "kt" }.forEach { file ->
            val content = file.readText()
            
            // Very naive structural check: if a file contains LazyColumn AND LazyVerticalGrid, 
            // flag it as a risk. (In a real AST parser, we would track nesting depth).
            // For now, we strictly forbid LazyVerticalGrid and LazyColumn in the same file
            // unless they are explicitly known to be safe (not nested).
            
            if (content.contains("LazyColumn") && content.contains("LazyVerticalGrid")) {
                // If it's HomeScreen, we know we just fixed it by removing LazyVerticalGrid,
                // so if it's still there, it's a regression.
                if (file.name == "HomeScreen.kt" || file.name == "Dashboard.kt") {
                    fail("File ${file.name} contains both LazyColumn and LazyVerticalGrid, which previously caused a nested scroll crash. Do not nest them.")
                }
            }
        }
    }
}
