package com.unsulliedcode.ui.theme

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.runtime.staticCompositionLocalOf

object Motion {
    val snappy = spring<Float>(
        dampingRatio = 0.8f,
        stiffness = 400f
    )
    val fluid = spring<Float>(
        dampingRatio = 0.7f,
        stiffness = 300f
    )
    val expressive = spring<Float>(
        dampingRatio = 0.6f,
        stiffness = 200f
    )
}

val LocalAppMotion = staticCompositionLocalOf { Motion }
