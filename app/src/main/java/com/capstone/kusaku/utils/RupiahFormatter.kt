package com.capstone.kusaku.utils

import java.text.NumberFormat
import java.util.Locale

class RupiahFormatter {
    companion object {
        fun format(number: Long): String {
            val localeID = Locale("in", "ID")
            val numberFormat = NumberFormat.getCurrencyInstance(localeID)
            return numberFormat.format(number)
        }
    }
}