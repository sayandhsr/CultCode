package com.unsulliedcode.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.runtime.staticCompositionLocalOf

data class AppGradients(
    val G01_Nebula: Brush,
    val G02_Aurora: Brush,
    val G03_Inferno: Brush,
    val G04_Cyber: Brush,
    val G05_Amethyst: Brush,
    val G06_Solar: Brush,
    val G07_Abyss: Brush,
    val G08_Bloom: Brush,
    val G09_Glacier: Brush,
    val G10_Toxic: Brush,
    val G11_Ember: Brush,
    val G12_Void: Brush
)

val ObsidianGradients = AppGradients(
    G01_Nebula = Brush.linearGradient(listOf(Color(0xFF7C5CFF), Color(0xFFFF6B9D))),
    G02_Aurora = Brush.linearGradient(listOf(Color(0xFF00E5A0), Color(0xFF4D9FFF))),
    G03_Inferno = Brush.linearGradient(listOf(Color(0xFFFF6B35), Color(0xFFFF2E63))),
    G04_Cyber = Brush.linearGradient(listOf(Color(0xFF00F0A0), Color(0xFF0575E6))),
    G05_Amethyst = Brush.linearGradient(listOf(Color(0xFF8E2DE2), Color(0xFF4A00E0))),
    G06_Solar = Brush.linearGradient(listOf(Color(0xFFFFC93C), Color(0xFFFF6B35))),
    G07_Abyss = Brush.linearGradient(listOf(Color(0xFF0F2027), Color(0xFF2C5364))),
    G08_Bloom = Brush.linearGradient(listOf(Color(0xFFFF4D8D), Color(0xFFC792EA))),
    G09_Glacier = Brush.linearGradient(listOf(Color(0xFF4D9FFF), Color(0xFFB8E6FF))),
    G10_Toxic = Brush.linearGradient(listOf(Color(0xFFA5E075), Color(0xFF00E5A0))),
    G11_Ember = Brush.linearGradient(listOf(Color(0xFFFF5A5F), Color(0xFFFFB020))),
    G12_Void = Brush.linearGradient(listOf(Color(0xFF1A1A22), Color(0xFF07070A)))
)

val PorcelainGradients = AppGradients(
    G01_Nebula = Brush.linearGradient(listOf(Color(0xFF6645EC), Color(0xFFEE5387))),
    G02_Aurora = Brush.linearGradient(listOf(Color(0xFF10AB7C), Color(0xFF378AEB))),
    G03_Inferno = Brush.linearGradient(listOf(Color(0xFFE95722), Color(0xFFE81B4F))),
    G04_Cyber = Brush.linearGradient(listOf(Color(0xFF11B57E), Color(0xFF1560AC))),
    G05_Amethyst = Brush.linearGradient(listOf(Color(0xFF7730B5), Color(0xFF4210A6))),
    G06_Solar = Brush.linearGradient(listOf(Color(0xFFE9B428), Color(0xFFE95722))),
    G07_Abyss = Brush.linearGradient(listOf(Color(0xFF040708), Color(0xFF28373E))),
    G08_Bloom = Brush.linearGradient(listOf(Color(0xFFEB3778), Color(0xFFB27FD4))),
    G09_Glacier = Brush.linearGradient(listOf(Color(0xFF378AEB), Color(0xFF99D4F4))),
    G10_Toxic = Brush.linearGradient(listOf(Color(0xFF91C566), Color(0xFF10AB7C))),
    G11_Ember = Brush.linearGradient(listOf(Color(0xFFEC4348), Color(0xFFE09816))),
    G12_Void = Brush.linearGradient(listOf(Color(0xFFFFFFFF), Color(0xFFFAFAFC)))
)

val LocalAppGradients = staticCompositionLocalOf { ObsidianGradients }
