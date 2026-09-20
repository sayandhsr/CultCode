package com.unsulliedcode.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import com.unsulliedcode.ui.theme.LocalAppColors
import com.unsulliedcode.ui.theme.LocalAppGradients
import com.unsulliedcode.ui.theme.Border
import com.unsulliedcode.ui.theme.Space
import kotlin.math.cos
import kotlin.math.sin

enum class BadgeRarity {
    COMMON, RARE, EPIC, LEGENDARY, MYTHIC
}

@Composable
fun Badge(
    rarity: BadgeRarity,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current
    val gradients = LocalAppGradients.current
    
    val baseColor = when (rarity) {
        BadgeRarity.COMMON -> colors.rarityCommon
        BadgeRarity.RARE -> colors.rarityRare
        BadgeRarity.EPIC -> colors.rarityEpic
        BadgeRarity.LEGENDARY -> colors.rarityLegendary
        BadgeRarity.MYTHIC -> colors.rarityMythic
    }
    
    val fillGradient = when (rarity) {
        BadgeRarity.COMMON -> gradients.G12_Void
        BadgeRarity.RARE -> gradients.G09_Glacier
        BadgeRarity.EPIC -> gradients.G05_Amethyst
        BadgeRarity.LEGENDARY -> gradients.G06_Solar
        BadgeRarity.MYTHIC -> gradients.G01_Nebula
    }

    Canvas(modifier = modifier.size(Space.xxxl)) {
        val size = this.size.width
        val center = Offset(size / 2, size / 2)
        val radius = size / 2

        val path = Path()

        when (rarity) {
            BadgeRarity.COMMON -> {
                path.addOval(Rect(0f, 0f, size, size))
            }
            BadgeRarity.RARE -> {
                for (i in 0..5) {
                    val angle = i * (Math.PI / 3) - Math.PI / 2
                    val x = center.x + radius * cos(angle).toFloat()
                    val y = center.y + radius * sin(angle).toFloat()
                    if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                }
                path.close()
            }
            BadgeRarity.EPIC -> {
                for (i in 0..7) {
                    val angle = i * (Math.PI / 4) - Math.PI / 2
                    val x = center.x + radius * cos(angle).toFloat()
                    val y = center.y + radius * sin(angle).toFloat()
                    if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                }
                path.close()
            }
            BadgeRarity.LEGENDARY -> {
                path.moveTo(center.x, 0f)
                path.lineTo(size, size * 0.2f)
                path.lineTo(size * 0.9f, size * 0.8f)
                path.lineTo(center.x, size)
                path.lineTo(size * 0.1f, size * 0.8f)
                path.lineTo(0f, size * 0.2f)
                path.close()
            }
            BadgeRarity.MYTHIC -> {
                val innerRadius = radius * 0.6f
                for (i in 0 until 24) {
                    val angle = i * (Math.PI / 12) - Math.PI / 2
                    val currentRadius = if (i % 2 == 0) radius else innerRadius
                    val x = center.x + currentRadius * cos(angle).toFloat()
                    val y = center.y + currentRadius * sin(angle).toFloat()
                    if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                }
                path.close()
            }
        }
        
        // §13.2 Layer 1: Inner Gradient
        drawPath(path = path, brush = fillGradient, style = Fill)
        
        // §13.2 Layer 2: Outer Border
        drawPath(path = path, color = baseColor.copy(alpha = 0.4f), style = Stroke(width = Border.focus.toPx()))
    }
}
