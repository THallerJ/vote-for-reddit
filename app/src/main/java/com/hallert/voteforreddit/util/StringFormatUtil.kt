package com.hallert.voteforreddit.util

import android.annotation.SuppressLint

class StringFormatUtil {
    companion object {
        @SuppressLint("DefaultLocale")
        fun capitalizeWords(string: String): String {
            val splitString = string.split(" ").map { it.trim() }

            val sb = StringBuilder()

            for (i in splitString.indices) {
                val newString =
                    splitString[i].substring(0, 1).toUpperCase() +
                            splitString[i].substring(1).toLowerCase();

                sb.append(newString)

                if (i != splitString.size) {
                    sb.append(" ")
                }
            }

            return sb.toString()
        }
    }
}