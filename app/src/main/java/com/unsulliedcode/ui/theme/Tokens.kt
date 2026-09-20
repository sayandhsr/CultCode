package com.unsulliedcode.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp

data class AppTypography(
    val h1: TextStyle,
    val h2: TextStyle,
    val h3: TextStyle,
    val body: TextStyle,
    val label: TextStyle,
    val caption: TextStyle,
    val overline: TextStyle,
    val code: TextStyle,
    val numericHero: TextStyle,
    val numericStat: TextStyle
)

fun buildTypography(isLight: Boolean): AppTypography {
    val weightOffset = if (isLight) 0 else -100 // Reduce weight by 100 in dark mode
    
    fun adjust(weight: Int) = FontWeight(Math.max(100, weight + weightOffset))

    val tabularFeatures = "tnum"
    
    return AppTypography(
        h1 = TextStyle(fontWeight = adjust(700), fontSize = 34.sp, letterSpacing = (-0.5).sp, lineHeight = 42.sp),
        h2 = TextStyle(fontWeight = adjust(700), fontSize = 24.sp, letterSpacing = 0.sp, lineHeight = 32.sp),
        h3 = TextStyle(fontWeight = adjust(700), fontSize = 20.sp, letterSpacing = 0.15.sp, lineHeight = 28.sp),
        body = TextStyle(fontWeight = adjust(400), fontSize = 16.sp, letterSpacing = 0.sp, lineHeight = 24.sp),
        label = TextStyle(fontWeight = adjust(500), fontSize = 14.sp, letterSpacing = 0.1.sp, lineHeight = 20.sp),
        caption = TextStyle(fontWeight = adjust(400), fontSize = 12.sp, letterSpacing = 0.4.sp, lineHeight = 16.sp),
        overline = TextStyle(fontWeight = adjust(600), fontSize = 10.sp, letterSpacing = 1.5.sp, lineHeight = 16.sp, fontFeatureSettings = tabularFeatures),
        code = TextStyle(fontWeight = adjust(400), fontSize = 14.sp, letterSpacing = 0.sp, lineHeight = (14 * 1.55).sp, fontFeatureSettings = tabularFeatures),
        numericHero = TextStyle(fontWeight = adjust(600), fontSize = 48.sp, letterSpacing = (-0.5).sp, lineHeight = 56.sp, fontFeatureSettings = tabularFeatures),
        numericStat = TextStyle(fontWeight = adjust(600), fontSize = 24.sp, letterSpacing = 0.sp, lineHeight = 32.sp, fontFeatureSettings = tabularFeatures)
    )
}

val LocalAppTypography = staticCompositionLocalOf<AppTypography> { error("No Typography provided") }

object Space {
    val xxs = 2.dp
    val xs = 4.dp
    val sm = 8.dp
    val md = 16.dp
    val lg = 24.dp
    val xl = 32.dp
    val xxl = 48.dp
    val xxxl = 64.dp
}
val LocalAppSpacing = staticCompositionLocalOf { Space }

object Radius {
    val none = 0.dp
    val sm = 8.dp
    val md = 12.dp
    val lg = 16.dp
    val xl = 24.dp
    val full = 999.dp
}
val LocalAppRadius = staticCompositionLocalOf { Radius }

object Border {
    val hairline = 1.dp
    val thick = 2.dp
    val focus = 3.dp
}
val LocalAppBorder = staticCompositionLocalOf { Border }
