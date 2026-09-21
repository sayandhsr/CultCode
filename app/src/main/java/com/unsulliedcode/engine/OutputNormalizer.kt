package com.unsulliedcode.engine

import kotlin.math.abs

data class NormalizationConfig(
    val floatTolerance: Double = 0.001,
    val ignoreOrder: Boolean = false
)

object OutputNormalizer {
    
    fun compare(actual: String, expected: String, config: NormalizationConfig = NormalizationConfig()): Boolean {
        val aClean = actual.trim()
        val eClean = expected.trim()

        // 1. Numeric Equivalence (including float tolerance)
        val aDouble = aClean.toDoubleOrNull()
        val eDouble = eClean.toDoubleOrNull()
        if (aDouble != null && eDouble != null) {
            return abs(aDouble - eDouble) <= config.floatTolerance
        }

        // 2. Structural/Collection Comparison
        if (aClean.startsWith("[") && aClean.endsWith("]") && eClean.startsWith("[") && eClean.endsWith("]")) {
            val aList = parseList(aClean)
            val eList = parseList(eClean)
            
            if (aList.size != eList.size) return false
            
            if (config.ignoreOrder) {
                val aSorted = aList.sorted()
                val eSorted = eList.sorted()
                return aSorted == eSorted
            } else {
                return aList == eList
            }
        }

        // 3. Fallback: string matching ignoring case and whitespace
        val aStrip = aClean.replace("\\s".toRegex(), "").lowercase()
        val eStrip = eClean.replace("\\s".toRegex(), "").lowercase()
        return aStrip == eStrip
    }

    private fun parseList(listStr: String): List<String> {
        val content = listStr.substring(1, listStr.length - 1)
        if (content.isBlank()) return emptyList()
        return content.split(",").map { it.trim().replace("\"", "").replace("'", "") }
    }
}
