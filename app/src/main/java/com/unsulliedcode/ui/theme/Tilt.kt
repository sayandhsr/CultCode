package com.unsulliedcode.ui.theme

import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer

fun Modifier.pseudo3DTilt(
    rotationX: Float = 0f,
    rotationY: Float = 0f,
    maxTilt: Float = 15f
) = composed {
    this.graphicsLayer {
        this.cameraDistance = 12f * density
        this.rotationX = rotationX.coerceIn(-maxTilt, maxTilt)
        this.rotationY = rotationY.coerceIn(-maxTilt, maxTilt)
    }
}
