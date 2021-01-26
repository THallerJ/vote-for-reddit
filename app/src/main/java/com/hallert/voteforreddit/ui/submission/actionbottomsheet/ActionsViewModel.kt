package com.hallert.voteforreddit.ui.submission.actionbottomsheet

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.hallert.voteforreddit.user.UserPreferences

class ActionsViewModel @ViewModelInject constructor(private val userPreferences: UserPreferences) :
    ViewModel() {

    fun toggleDarkMode() {
        userPreferences.toggleDarkMode()
    }
}