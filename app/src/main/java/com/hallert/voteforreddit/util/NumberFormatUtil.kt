package com.hallert.voteforreddit.util

import kotlin.math.ln
import kotlin.math.pow

class NumberFormatUtil {
    companion object {
        fun truncate(num: Int): String {
            if (num < 1000) return "" + num
            val exp = (ln(num.toDouble()) / ln(1000.0)).toInt()
            return String.format("%.1f%c", num / 1000.0.pow(exp.toDouble()), "KMGTPE"[exp - 1])
        }
    }
}
