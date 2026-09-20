package com.unsulliedcode.ui.gamification

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import com.unsulliedcode.ui.theme.*

@Composable
fun XpBar(
    currentXp: Int,
    targetXp: Int,
    level: Int,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current
    val typography = LocalAppTypography.current
    val gradients = LocalAppGradients.current
    val progress = (currentXp.toFloat() / targetXp).coerceIn(0f, 1f)

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = Motion.expressive,
        label = "XpProgress"
    )

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Level $level",
                style = typography.label,
                color = colors.xpGold
            )
            Text(
                text = "$currentXp / $targetXp XP",
                style = typography.numericStat,
                color = colors.textSecondary
            )
        }
        Spacer(modifier = Modifier.height(Space.xs))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(Space.sm)
                .clip(RoundedCornerShape(Radius.full))
                .background(colors.surfaceInset)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(animatedProgress)
                    .clip(RoundedCornerShape(Radius.full))
                    .background(brush = gradients.G06_Solar)
            )
        }
    }
}

@Composable
fun StreakCounter(
    days: Int,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current
    val typography = LocalAppTypography.current
    val gradients = LocalAppGradients.current

    val infiniteTransition = rememberInfiniteTransition(label = "StreakPulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "PulseScale"
    )

    Box(
        modifier = modifier
            .themeDepth(isLight = colors.isLight, accentColor = colors.warning)
            .background(
                brush = gradients.G11_Ember,
                shape = RoundedCornerShape(Radius.lg)
            )
            .padding(Space.lg)
            .graphicsLayer {
                scaleX = if (days > 0) pulseScale else 1f
                scaleY = if (days > 0) pulseScale else 1f
            },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "🔥",
                style = typography.numericHero
            )
            Text(
                text = "$days",
                style = typography.numericHero,
                color = colors.textOnAccent
            )
            Text(
                text = if (days == 1) "Day Streak" else "Day Streak",
                style = typography.caption,
                color = colors.textOnAccent.copy(alpha = 0.8f)
            )
        }
    }
}
