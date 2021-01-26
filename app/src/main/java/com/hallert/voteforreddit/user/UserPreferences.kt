package com.hallert.voteforreddit.user

import android.content.Context.MODE_PRIVATE
import androidx.appcompat.app.AppCompatDelegate
import com.hallert.voteforreddit.RedditApp

private const val SHARED_PREF = "shared_pref"
private const val NIGHT_MODE_TOGGLED = "night_mode_toggled"

class UserPreferences {
    fun toggleDarkMode() {
        val editor = RedditApp.appContext.getSharedPreferences(SHARED_PREF, MODE_PRIVATE).edit()

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            editor.putBoolean(NIGHT_MODE_TOGGLED, true)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            editor.putBoolean(NIGHT_MODE_TOGGLED, false)
        }

        editor.apply()
    }

    fun setTheme() {
        if (RedditApp.appContext.getSharedPreferences(SHARED_PREF, MODE_PRIVATE)
                .getBoolean(NIGHT_MODE_TOGGLED, false)
        ) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}