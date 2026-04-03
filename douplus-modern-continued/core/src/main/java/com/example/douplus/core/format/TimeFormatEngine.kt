package com.example.douplus.core.format

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TimeFormatEngine {
    fun format(timestamp: Long, pattern: String, locale: Locale = Locale.getDefault()): String {
        return try {
            SimpleDateFormat(pattern, locale).format(Date(timestamp))
        } catch (_: Throwable) {
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale).format(Date(timestamp))
        }
    }

    fun formatOrDefault(
        timestamp: Long,
        pattern: String?,
        defaultPattern: String = "yyyy-MM-dd HH:mm:ss",
        locale: Locale = Locale.getDefault(),
    ): String {
        val usePattern = pattern?.takeIf { it.isNotBlank() } ?: defaultPattern
        return format(timestamp, usePattern, locale)
    }
}
