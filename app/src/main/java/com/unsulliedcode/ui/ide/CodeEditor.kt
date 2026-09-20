package com.unsulliedcode.ui.ide

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import com.unsulliedcode.engine.SyntaxHighlighter
import com.unsulliedcode.ui.theme.*

@Composable
fun CodeEditor(
    code: String,
    language: String,
    onCodeChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current
    val typography = LocalAppTypography.current
    val highlighter = remember(colors) { SyntaxHighlighter(colors) }

    var textFieldValue by remember(code) { mutableStateOf(TextFieldValue(code)) }

    Row(
        modifier = modifier
            .fillMaxSize()
            .background(colors.bg)
            .horizontalScroll(rememberScrollState())
    ) {
        // Line numbers gutter
        val lineCount = textFieldValue.text.count { it == '\n' } + 1
        Column(
            modifier = Modifier
                .background(colors.bgSubtle)
                .padding(horizontal = Space.sm)
                .verticalScroll(rememberScrollState())
        ) {
            for (i in 1..lineCount) {
                Text(
                    text = i.toString().padStart(4),
                    style = typography.code,
                    color = colors.textTertiary
                )
            }
        }

        Spacer(
            modifier = Modifier
                .width(Border.hairline)
                .fillMaxHeight()
                .background(colors.border)
        )

        // Code area
        BasicTextField(
            value = textFieldValue,
            onValueChange = { newValue ->
                textFieldValue = newValue
                onCodeChange(newValue.text)
            },
            textStyle = typography.code.copy(color = colors.textPrimary),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Space.sm)
                .verticalScroll(rememberScrollState()),
            decorationBox = { innerTextField ->
                innerTextField()
            }
        )
    }
}

fun buildHighlightedString(
    code: String,
    language: String,
    highlighter: SyntaxHighlighter
): AnnotatedString {
    val tokens = highlighter.tokenize(code, language)
    return buildAnnotatedString {
        tokens.forEach { token ->
            withStyle(SpanStyle(color = highlighter.colorForToken(token.type))) {
                append(token.text)
            }
        }
    }
}
