package com.unsulliedcode.ui.dashboard

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import com.unsulliedcode.ui.theme.*

@Composable
fun StatCard(
    label: String,
    value: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current
    val typography = LocalAppTypography.current

    Box(
        modifier = modifier
            .themeDepth(isLight = colors.isLight, accentColor = colors.accentPrimary)
            .background(colors.surfaceElevated, RoundedCornerShape(Radius.lg))
            .padding(Space.lg)
    ) {
        Column {
            Text(text = label, style = typography.overline, color = colors.textTertiary)
            Spacer(modifier = Modifier.height(Space.xs))
            Text(text = value, style = typography.numericHero, color = colors.textPrimary)
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(Space.xxs))
                Text(text = subtitle, style = typography.caption, color = colors.textSecondary)
            }
        }
    }
}

@Composable
fun ProgressRing(
    progress: Float,
    label: String,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current
    val typography = LocalAppTypography.current
    val gradients = LocalAppGradients.current

    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = Motion.expressive,
        label = "RingProgress"
    )

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(Space.xxxl)) {
            val strokeWidth = Border.focus.toPx() * 2
            val radius = (size.minDimension - strokeWidth) / 2
            val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)
            val arcSize = Size(radius * 2, radius * 2)

            // Track
            drawArc(
                color = colors.surfaceInset,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Progress
            drawArc(
                color = colors.accentPrimary,
                startAngle = -90f,
                sweepAngle = animatedProgress * 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${(animatedProgress * 100).toInt()}%",
                style = typography.numericStat,
                color = colors.textPrimary
            )
            Text(
                text = label,
                style = typography.caption,
                color = colors.textTertiary
            )
        }
    }
}

@Composable
fun WeeklyActivityChart(
    data: List<Float>,
    labels: List<String> = listOf("M", "T", "W", "T", "F", "S", "S"),
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current
    val typography = LocalAppTypography.current
    val maxVal = data.maxOrNull() ?: 1f

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(Space.xxxl * 2)
            .background(colors.surface, RoundedCornerShape(Radius.md))
            .padding(Space.md),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        data.forEachIndexed { index, value ->
            val height by animateFloatAsState(
                targetValue = (value / maxVal).coerceIn(0.05f, 1f),
                animationSpec = Motion.fluid,
                label = "BarHeight$index"
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight(height)
                        .width(Space.md)
                        .background(
                            color = if (value > 0) colors.accentPrimary else colors.surfaceInset,
                            shape = RoundedCornerShape(topStart = Radius.sm, topEnd = Radius.sm)
                        )
                )
                Spacer(modifier = Modifier.height(Space.xs))
                Text(
                    text = if (index < labels.size) labels[index] else "",
                    style = typography.caption,
                    color = colors.textTertiary
                )
            }
        }
    }
}
