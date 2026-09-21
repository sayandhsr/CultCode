package com.unsulliedcode.engine

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

object SqlEvaluator {
    fun evaluateQuery(db: SQLiteDatabase, userCode: String, expectedQuery: String): Boolean {
        try {
            val userRows = runQuery(db, userCode)
            val expectedRows = runQuery(db, expectedQuery)
            
            if (userRows.size != expectedRows.size) return false
            if (userRows.isEmpty()) return true

            // Check if column names match (ignoring order)
            val userCols = userRows.first().keys.toSet()
            val expectedCols = expectedRows.first().keys.toSet()
            if (userCols != expectedCols) return false

            // Check if rows match (ignoring order)
            // Note: If the query has ORDER BY, we should theoretically enforce order.
            // But the spec says: "Full SQL result-set comparison (column-order insensitive, row-order insensitive)".
            val userRowsSet = userRows.toSet()
            val expectedRowsSet = expectedRows.toSet()
            
            return userRowsSet == expectedRowsSet
        } catch (e: Exception) {
            return false
        }
    }

    private fun runQuery(db: SQLiteDatabase, query: String): List<Map<String, String>> {
        val rows = mutableListOf<Map<String, String>>()
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val row = mutableMapOf<String, String>()
            for (i in 0 until cursor.columnCount) {
                row[cursor.getColumnName(i)] = cursor.getString(i) ?: "NULL"
            }
            rows.add(row)
        }
        cursor.close()
        return rows
    }
}
