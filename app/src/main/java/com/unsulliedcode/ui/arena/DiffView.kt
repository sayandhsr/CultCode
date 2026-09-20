package com.unsulliedcode.ui.arena

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.unsulliedcode.ui.theme.*

data class DiffLine(
    val lineNumber: Int,
    val content: String,
    val type: DiffLineType
)

enum class DiffLineType {
    CONTEXT, ADD, REMOVE
}

@Composable
fun DiffView(
    originalLines: List<DiffLine>,
    modifiedLines: List<DiffLine>,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current
    val typography = LocalAppTypography.current

    Row(modifier = modifier.fillMaxWidth().horizontalScroll(rememberScrollState())) {
        // Left pane: original
        Column(
            modifier = Modifier
                .weight(1f)
                .background(colors.diffContextBg)
                .verticalScroll(rememberScrollState())
        ) {
            originalLines.forEach { line ->
                val (bgColor, textColor, gutterColor) = when (line.type) {
                    DiffLineType.CONTEXT -> Triple(colors.diffContextBg, colors.diffContext, colors.diffContextBg)
                    DiffLineType.REMOVE -> Triple(colors.diffRemoveBg, colors.diffRemove, colors.diffRemoveGutter)
                    DiffLineType.ADD -> Triple(colors.diffAddBg, colors.diffAdd, colors.diffAddGutter)
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(bgColor)
                        .padding(horizontal = Space.xs, vertical = Space.xxs)
                ) {
                    // Gutter
                    Text(
                        text = line.lineNumber.toString().padStart(4),
                        style = typography.code,
                        color = colors.textTertiary,
                        modifier = Modifier
                            .background(gutterColor)
                            .padding(end = Space.sm)
                    )
                    // Content
                    Text(
                        text = line.content,
                        style = typography.code,
                        color = textColor
                    )
                }
            }
        }

        // Divider
        Spacer(
            modifier = Modifier
                .width(Border.hairline)
                .fillMaxHeight()
                .background(colors.border)
        )

        // Right pane: modified
        Column(
            modifier = Modifier
                .weight(1f)
                .background(colors.diffContextBg)
                .verticalScroll(rememberScrollState())
        ) {
            modifiedLines.forEach { line ->
                val (bgColor, textColor, gutterColor) = when (line.type) {
                    DiffLineType.CONTEXT -> Triple(colors.diffContextBg, colors.diffContext, colors.diffContextBg)
                    DiffLineType.REMOVE -> Triple(colors.diffRemoveBg, colors.diffRemove, colors.diffRemoveGutter)
                    DiffLineType.ADD -> Triple(colors.diffAddBg, colors.diffAdd, colors.diffAddGutter)
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(bgColor)
                        .padding(horizontal = Space.xs, vertical = Space.xxs)
                ) {
                    Text(
                        text = line.lineNumber.toString().padStart(4),
                        style = typography.code,
                        color = colors.textTertiary,
                        modifier = Modifier
                            .background(gutterColor)
                            .padding(end = Space.sm)
                    )
                    Text(
                        text = line.content,
                        style = typography.code,
                        color = textColor
                    )
                }
            }
        }
    }
}
