package com.hallert.voteforreddit.util

import android.text.format.DateUtils.*

class DateFormatUtil {
    companion object {
        fun timeSince(time: Long): String {
            val timeBetween = System.currentTimeMillis() - time

            when {
                timeBetween < MINUTE_IN_MILLIS -> return "now"
                timeBetween < HOUR_IN_MILLIS -> {
                    val minutes = timeBetween / 1000 / 60
                    return minutes.toString() + "m"
                }
                timeBetween < DAY_IN_MILLIS -> {
                    val hours = timeBetween / 1000 / 60 / 60
                    return hours.toString() + "h"
                }
                timeBetween <= YEAR_IN_MILLIS  -> { // YEAR_IN_MILLIS is the number of millis in 364 days
                    val days = timeBetween / 1000 / 60 / 60 / 24
                    return days.toString() + "d"
                }
                else -> {
                    val years = timeBetween / 1000 / 60 / 60 / 24 / 364
                    return years.toString() + "y"
                }
            }
        }
    }
}
