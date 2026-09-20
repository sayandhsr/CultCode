package com.unsulliedcode.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.unsulliedcode.ui.theme.LocalAppColors
import com.unsulliedcode.ui.theme.Space
import com.unsulliedcode.ui.theme.Border
import com.unsulliedcode.ui.theme.Radius
import androidx.compose.ui.Alignment
import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun BrutalistButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier, isPrimary: Boolean = true) {
    val colors = LocalAppColors.current
    Button(
        onClick = onClick,
        modifier = modifier.border(Border.hairline, colors.border, RoundedCornerShape(Radius.none)).height(Space.xxl),
        colors = ButtonDefaults.buttonColors(containerColor = if (isPrimary) colors.accentPrimary else colors.bg, contentColor = if (isPrimary) colors.textInverse else colors.textPrimary),
        shape = RoundedCornerShape(Radius.none)
    ) { Text(text, fontWeight = FontWeight.Bold) }
}

@Composable
fun BrutalistCard(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val colors = LocalAppColors.current
    Box(modifier = modifier.border(Border.hairline, colors.border, RoundedCornerShape(Radius.none)).background(colors.surface).padding(Space.md)) { content() }
}

@Composable
fun BrutalistChip(text: String, selected: Boolean = false, onClick: () -> Unit) {
    val colors = LocalAppColors.current
    Surface(
        onClick = onClick,
        modifier = Modifier.border(Border.hairline, colors.border, RoundedCornerShape(Radius.none)),
        color = if (selected) colors.accentPrimary else colors.bg,
        contentColor = if (selected) colors.textInverse else colors.textPrimary
    ) { Text(text, modifier = Modifier.padding(horizontal = Space.md, vertical = Space.sm)) }
}

@Composable
fun DiffViewer(oldCode: String, newCode: String) {
    val colors = LocalAppColors.current
    Column(modifier = Modifier.fillMaxWidth().background(colors.bg).border(Border.hairline, colors.border)) {
        Row(modifier = Modifier.fillMaxWidth().background(colors.diffRemoveBg)) { Text("- $oldCode", color = colors.diffRemove, modifier = Modifier.padding(Space.xs)) }
        Row(modifier = Modifier.fillMaxWidth().background(colors.diffAddBg)) { Text("+ $newCode", color = colors.diffAdd, modifier = Modifier.padding(Space.xs)) }
    }
}

@Composable
fun TerminalView(logs: List<String>) {
    val colors = LocalAppColors.current
    Column(modifier = Modifier.fillMaxWidth().background(colors.bgElevated).border(Border.hairline, colors.borderStrong).padding(Space.sm)) {
        logs.forEach { Text("> $it", color = colors.syntaxString) }
    }
}

@Composable
fun CodeBlockView(code: String) {
    val colors = LocalAppColors.current
    Box(modifier = Modifier.fillMaxWidth().background(colors.bg).border(Border.hairline, colors.borderStrong).padding(Space.sm)) {
        Text(code, color = colors.syntaxKeyword)
    }
}

@Composable
fun RadarChartStub() {
    val colors = LocalAppColors.current
    Canvas(modifier = Modifier.size(Space.xxxl)) {
        drawCircle(colors.borderStrong, radius = size.minDimension / 2, style = Stroke(Border.hairline.toPx()))
    }
}

@Composable
fun BarChartStub() {
    val colors = LocalAppColors.current
    Row(modifier = Modifier.height(Space.xxxl), verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(Space.xs)) {
        Box(modifier = Modifier.width(Space.lg).height(Space.xxl).background(colors.accentPrimary))
        Box(modifier = Modifier.width(Space.lg).height(Space.xxxl).background(colors.accentSecondary))
    }
}

@Composable
fun DonutChartStub() {
    val colors = LocalAppColors.current
    Canvas(modifier = Modifier.size(Space.xxxl)) {
        drawCircle(colors.accentPrimary, radius = size.minDimension / 2, style = Stroke(Space.md.toPx()))
    }
}

@Composable
fun SparklineStub() {
    val colors = LocalAppColors.current
    Canvas(modifier = Modifier.size(Space.xxxl, Space.xl)) {
        val path = Path().apply { moveTo(0f, 30f); lineTo(50f, 10f); lineTo(100f, 20f) }
        drawPath(path, color = colors.info, style = Stroke(Border.hairline.toPx()))
    }
}

@Composable
fun HeatmapCell(intensity: Float) {
    val colors = LocalAppColors.current
    Box(modifier = Modifier.size(Space.md).background(colors.accentPrimary.copy(alpha = intensity)).border(Border.hairline, colors.border))
}

@Composable
fun ProgressRingStub(progress: Float) {
    val colors = LocalAppColors.current
    CircularProgressIndicator(progress = { progress }, color = colors.success, trackColor = colors.surfaceHover)
}

@Composable
fun BottomSheetStub() {
    val colors = LocalAppColors.current
    Box(modifier = Modifier.fillMaxWidth().height(Space.xxxl).background(colors.surface).border(Border.hairline, colors.border)) {
        Box(modifier = Modifier.width(Space.xxl).height(Space.sm).background(colors.textTertiary).align(Alignment.TopCenter).padding(Space.sm))
    }
}

@Composable
fun SkeletonStub() {
    val colors = LocalAppColors.current
    Box(modifier = Modifier.fillMaxWidth().height(Space.lg).background(colors.surfaceHover))
}

@Composable
fun EmptyStateStub(message: String) {
    val colors = LocalAppColors.current
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(Space.lg)) {
        Text(message, color = colors.textSecondary)
    }
}

@Composable
fun ErrorStateStub(error: String) {
    val colors = LocalAppColors.current
    Box(modifier = Modifier.background(colors.errorBg).border(Border.hairline, colors.error).padding(Space.md)) {
        Text(error, color = colors.error)
    }
}

@Composable
fun TabsStub(tabs: List<String>) {
    val colors = LocalAppColors.current
    Row(modifier = Modifier.fillMaxWidth().border(Border.hairline, colors.border)) {
        tabs.forEach { Text(it, color = colors.textPrimary, modifier = Modifier.padding(Space.md)) }
    }
}

@Composable
fun AccordionStub(title: String) {
    val colors = LocalAppColors.current
    Row(modifier = Modifier.fillMaxWidth().border(Border.hairline, colors.border).padding(Space.md), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(title, color = colors.textPrimary)
        Text("+", color = colors.textPrimary)
    }
}

@Composable
fun StepperStub(step: Int) {
    val colors = LocalAppColors.current
    Row(horizontalArrangement = Arrangement.spacedBy(Space.sm)) {
        (1..3).forEach { Text("Step $it", color = if (it == step) colors.accentPrimary else colors.textDisabled) }
    }
}

@Composable
fun SearchFieldStub() {
    val colors = LocalAppColors.current
    OutlinedTextField(
        value = "", onValueChange = {},
        placeholder = { Text("Search...", color = colors.textSecondary) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colors.borderStrong,
            unfocusedBorderColor = colors.border
        )
    )
}

@Composable
fun ToastStub(message: String) {
    val colors = LocalAppColors.current
    Box(modifier = Modifier.background(colors.infoBg).border(Border.hairline, colors.info).padding(Space.sm)) {
        Text(message, color = colors.info)
    }
}



