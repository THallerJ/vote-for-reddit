package com.hallert.voteforreddit.ui.comments

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import net.dean.jraw.oauth.AccountHelper

class CommentsViewModel @ViewModelInject constructor(
    private val commentsRepository: CommentsRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    lateinit var submissionId: String

    fun getComments() {
        commentsRepository.test(submissionId)
    }
}