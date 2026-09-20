package com.unsulliedcode.ui.theme

import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.softShadow(
    radius: Dp = Space.md,
    alpha: Float = 0.05f
) = composed {
    this.drawBehind {
        val shadowPaint = Paint().apply {
            this.color = Color.Black.copy(alpha = alpha)
            this.asFrameworkPaint().let { frameworkPaint ->
                frameworkPaint.maskFilter = android.graphics.BlurMaskFilter(
                    radius.toPx(),
                    android.graphics.BlurMaskFilter.Blur.NORMAL
                )
            }
        }
        drawIntoCanvas { canvas ->
            canvas.save()
            // Offset for light source
            canvas.translate(0f, (radius / 2).toPx())
            canvas.drawRoundRect(
                left = 0f,
                top = 0f,
                right = size.width,
                bottom = size.height,
                radiusX = Space.md.toPx(), // Simplified corner radius
                radiusY = Space.md.toPx(),
                paint = shadowPaint
            )
            canvas.restore()
        }
    }
}

fun Modifier.glow(
    color: Color,
    radius: Dp = Space.lg,
    alpha: Float = 0.15f
) = composed {
    this.drawBehind {
        val glowPaint = Paint().apply {
            this.color = color.copy(alpha = alpha)
            this.asFrameworkPaint().let { frameworkPaint ->
                frameworkPaint.maskFilter = android.graphics.BlurMaskFilter(
                    radius.toPx(),
                    android.graphics.BlurMaskFilter.Blur.NORMAL
                )
            }
        }
        drawIntoCanvas { canvas ->
            canvas.drawRoundRect(
                left = 0f,
                top = 0f,
                right = size.width,
                bottom = size.height,
                radiusX = Space.md.toPx(), // Simplified corner radius
                radiusY = Space.md.toPx(),
                paint = glowPaint
            )
        }
    }
}

fun Modifier.themeDepth(isLight: Boolean, accentColor: Color) = composed {
    if (isLight) {
        this.softShadow()
    } else {
        this.glow(color = accentColor)
    }
}

