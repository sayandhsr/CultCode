package com.unsulliedcode.theme

import androidx.compose.ui.graphics.Color
import com.unsulliedcode.ui.theme.*
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File
import kotlin.math.max
import kotlin.math.min

class ContrastTest {

    private fun getLuminance(color: Color): Double {
        fun linearize(channel: Float): Double {
            val c = channel.toDouble()
            return if (c <= 0.03928) c / 12.92 else Math.pow((c + 0.055) / 1.055, 2.4)
        }
        val r = linearize(color.red)
        val g = linearize(color.green)
        val b = linearize(color.blue)
        return 0.2126 * r + 0.7152 * g + 0.0722 * b
    }

    private fun getContrastRatio(c1: Color, c2: Color): Double {
        val l1 = getLuminance(c1)
        val l2 = getLuminance(c2)
        val lightest = max(l1, l2)
        val darkest = min(l1, l2)
        return (lightest + 0.05) / (darkest + 0.05)
    }

    @Test
    fun testContrastRatios() {
        val themes = mapOf(
            "Obsidian" to Obsidian,
            "Porcelain" to Porcelain
        )

        val report = StringBuilder()
        report.append("# Contrast Test Report\n\n")

        for ((name, theme) in themes) {
            val primaryContrast = getContrastRatio(theme.textPrimary, theme.bg)
            val secondaryContrast = getContrastRatio(theme.textSecondary, theme.bg)
            val primaryElevatedContrast = getContrastRatio(theme.textPrimary, theme.bgElevated)
            
            report.append("## Theme: $name\n")
            report.append("- textPrimary on bg: %.2f:1 (WCAG Pass: %b)\n".format(primaryContrast, primaryContrast >= 4.5))
            report.append("- textSecondary on bg: %.2f:1 (WCAG Pass: %b)\n".format(secondaryContrast, secondaryContrast >= 4.5))
            report.append("- textPrimary on bgElevated: %.2f:1 (WCAG Pass: %b)\n\n".format(primaryElevatedContrast, primaryElevatedContrast >= 4.5))

            assertTrue("Theme $name: textPrimary on bg fails WCAG 4.5:1 (was $primaryContrast)", primaryContrast >= 4.5)
            assertTrue("Theme $name: textSecondary on bg fails WCAG 4.5:1 (was $secondaryContrast)", secondaryContrast >= 4.5)
            assertTrue("Theme $name: textPrimary on bgElevated fails WCAG 4.5:1 (was $primaryElevatedContrast)", primaryElevatedContrast >= 4.5)
        }
        
        File("contrast_report.md").writeText(report.toString())
    }
}
