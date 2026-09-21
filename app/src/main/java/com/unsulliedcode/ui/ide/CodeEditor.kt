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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
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

    // Shared scroll state so line numbers stay in sync with code
    val verticalScrollState = rememberScrollState()

    val visualTransformation = remember(highlighter, language) {
        SyntaxHighlightVisualTransformation(highlighter, language)
    }

    Row(
        modifier = modifier
            .fillMaxSize()
            .background(colors.bg)
            .horizontalScroll(rememberScrollState())
    ) {
        // Line numbers gutter — uses the SAME scroll state as code
        val lineCount = textFieldValue.text.count { it == '\n' } + 1
        Column(
            modifier = Modifier
                .background(colors.bgSubtle)
                .padding(horizontal = Space.sm)
                .verticalScroll(verticalScrollState)
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

        // Code area with syntax highlighting
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
                .verticalScroll(verticalScrollState),
            visualTransformation = visualTransformation,
            decorationBox = { innerTextField ->
                innerTextField()
            }
        )
    }
}

/**
 * VisualTransformation that applies syntax highlighting via the SyntaxHighlighter engine.
 */
private class SyntaxHighlightVisualTransformation(
    private val highlighter: SyntaxHighlighter,
    private val language: String
) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val highlighted = buildHighlightedString(text.text, language, highlighter)
        return TransformedText(highlighted, OffsetMapping.Identity)
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
