package com.unsulliedcode.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import com.unsulliedcode.ui.theme.*

@Composable
fun DashboardGrid(
    items: List<String>,
    modifier: Modifier = Modifier,
    onItemClick: (String) -> Unit
) {
    val colors = LocalAppColors.current
    val typography = LocalAppTypography.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.bg),
        verticalArrangement = Arrangement.spacedBy(Space.md)
    ) {
        val chunkedItems = items.chunked(2)
        chunkedItems.forEachIndexed { rowIndex, rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Space.md)
            ) {
                rowItems.forEachIndexed { colIndex, item ->
                    val index = rowIndex * 2 + colIndex
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .themeDepth(isLight = colors.isLight, accentColor = colors.accentPrimary)
                            .background(colors.surfaceElevated, shape = androidx.compose.foundation.shape.RoundedCornerShape(Radius.xl))
                            .interactive { onItemClick(item) }
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
                        
                        Box(
                            modifier = Modifier
                                .size(Space.lg)
                                .background(colors.accentMuted, shape = androidx.compose.foundation.shape.CircleShape)
                                .align(Alignment.TopEnd)
                        )
                    }
                }
                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
