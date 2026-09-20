package com.unsulliedcode.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import com.unsulliedcode.ui.theme.*

@Composable
fun DashboardGrid(
    items: List<String>,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current
    val typography = LocalAppTypography.current

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(Space.md),
        horizontalArrangement = Arrangement.spacedBy(Space.md),
        verticalArrangement = Arrangement.spacedBy(Space.md),
        modifier = modifier.fillMaxSize().background(colors.bg)
    ) {
        items(items.size) { index ->
            val item = items[index]
            
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .themeDepth(isLight = colors.isLight, accentColor = colors.accentPrimary)
                    .background(colors.surfaceElevated, shape = androidx.compose.foundation.shape.RoundedCornerShape(Radius.xl))
                    .interactive { /* Handle click */ }
                    // Simple tilt based on index for demo purposes; real would use sensors
                    .pseudo3DTilt(
                        rotationX = if (index % 2 == 0) 5f else -5f,
                        rotationY = if (index % 3 == 0) -5f else 5f
                    )
                    .padding(Space.md)
            ) {
                Text(
                    text = item,
                    style = typography.h3,
                    color = colors.textPrimary,
                    modifier = Modifier.align(Alignment.BottomStart)
                )
                
                // Top right accent icon mock
                Box(
                    modifier = Modifier
                        .size(Space.lg)
                        .background(colors.accentMuted, shape = androidx.compose.foundation.shape.CircleShape)
                        .align(Alignment.TopEnd)
                )
            }
        }
    }
}
