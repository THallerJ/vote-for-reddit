package com.hallert.voteforreddit

import kotlin.math.ln
import kotlin.math.pow

class Utils {
    companion object {
        fun formatThousand(num: Int): String {
            if (num < 1000) return "" + num
            val exp = (ln(num.toDouble()) / ln(1000.0)).toInt()
            return String.format("%.1f %c", num / 1000.0.pow(exp.toDouble()), "KMGTPE"[exp - 1])
        }
    }

    //TODO: Create method for getting the difference between two times
}
