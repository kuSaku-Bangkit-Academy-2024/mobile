package com.capstone.kusaku.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DateHelper {
    companion object {
        fun getDateInYearAndMonthFormat(month: Int = 0): String {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.MONTH, month)
            val dateFormat = SimpleDateFormat("yyyy-MM", Locale.getDefault())
            return dateFormat.format(calendar.time)
        }

        fun getPreviousMonthAndYear(): String {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.MONTH, -1)
            val dateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
            return dateFormat.format(calendar.time)
        }

        fun convertTimestampToDate(timestamp: Long): String {
            val timestampInMillis = timestamp * 1000
            val date = Date(timestampInMillis)
            val format = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            return format.format(date)
        }
    }
}