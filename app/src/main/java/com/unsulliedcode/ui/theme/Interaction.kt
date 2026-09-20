package com.unsulliedcode.ui.theme

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer

/**
 * Interactive modifier that applies scale/alpha micro-animations on press/hover.
 * Uses the new Indication API (no deprecated RippleTheme).
 */
fun Modifier.interactive(onClick: () -> Unit) = composed {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()

    val targetScale = when {
        isPressed -> 0.95f
        isHovered -> 1.02f
        else -> 1f
    }

    val scale by animateFloatAsState(
        targetValue = targetScale,
        animationSpec = if (isPressed) Motion.snappy else Motion.fluid,
        label = "InteractiveScale"
    )

    val alpha by animateFloatAsState(
        targetValue = if (isPressed) 0.8f else 1f,
        animationSpec = if (isPressed) Motion.snappy else Motion.fluid,
        label = "InteractiveAlpha"
    )

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
            this.alpha = alpha
        }
        .clickable(
            interactionSource = interactionSource,
            indication = androidx.compose.material3.ripple(),
            onClick = onClick
        )
}
