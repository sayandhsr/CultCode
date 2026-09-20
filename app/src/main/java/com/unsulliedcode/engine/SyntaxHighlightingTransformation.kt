package com.unsulliedcode.engine

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import java.util.regex.Pattern

class SyntaxHighlightingTransformation : VisualTransformation {
    
    private val keywords = listOf(
        "select", "from", "where", "join", "on", "as", "group by", "order by", "limit",
        "insert", "into", "values", "update", "set", "delete", "create", "table", "drop",
        "def", "class", "import", "from", "return", "if", "else", "elif", "for", "while", "in",
        "try", "except", "with", "as", "pass", "break", "continue", "True", "False", "None",
        "var", "let", "const", "function", "=>", "val", "fun"
    ).map { it.lowercase() }

    private var keywordColor = androidx.compose.ui.graphics.Color.White // Purple
    private var stringColor = androidx.compose.ui.graphics.Color.Gray  // Green
    private var numberColor = androidx.compose.ui.graphics.Color.Gray  // Orange
    private var defaultColor = androidx.compose.ui.graphics.Color.Gray // Light Grey
    private var operatorColor = androidx.compose.ui.graphics.Color.White // Cyan

    override fun filter(text: AnnotatedString): TransformedText {
        val inputText = text.text
        val annotatedString = buildAnnotatedString {
            append(inputText)
            addStyle(SpanStyle(color = defaultColor), 0, inputText.length)

            // Extremely basic regex tokenizer for MVP syntax highlighting
            val pattern = Pattern.compile("(\"[^\"]*\")|('[^']*')|(\\b\\d+\\b)|(\\b[a-zA-Z_]\\w*\\b)|([=+\\-*/<>!]+)")
            val matcher = pattern.matcher(inputText)
            
            while (matcher.find()) {
                val group = matcher.group()
                val start = matcher.start()
                val end = matcher.end()
                
                when {
                    group.startsWith("\"") || group.startsWith("'") -> {
                        addStyle(SpanStyle(color = stringColor), start, end)
                    }
                    group.matches("\\d+".toRegex()) -> {
                        addStyle(SpanStyle(color = numberColor), start, end)
                    }
                    group.matches("[=+\\-*/<>!]+".toRegex()) -> {
                        addStyle(SpanStyle(color = operatorColor), start, end)
                    }
                    keywords.contains(group.lowercase()) -> {
                        addStyle(SpanStyle(color = keywordColor), start, end)
                    }
                }
            }
        }
        return TransformedText(annotatedString, OffsetMapping.Identity)
    }
}

