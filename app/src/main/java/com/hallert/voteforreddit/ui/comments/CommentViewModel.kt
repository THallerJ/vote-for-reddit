package com.hallert.voteforreddit.ui.comments

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.hallert.voteforreddit.database.CommentEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class CommentViewModel @ViewModelInject constructor(
    private val commentsRepository: CommentsRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    @ExperimentalCoroutinesApi
    private val commentChannel = ConflatedBroadcastChannel<String>()

    @ExperimentalCoroutinesApi
    @FlowPreview
    val comments: LiveData<List<CommentEntity>> = commentChannel.asFlow().flatMapLatest { id ->
        commentsRepository.getComments(id)
    }.asLiveData()

    var submissionId: String = null.toString()

    @ExperimentalCoroutinesApi
    fun initComments(id: String) {
        commentsRepository.id = id
        commentChannel.offer(id)
        CoroutineScope(Main).launch { commentsRepository.setupComments(id) }
    }
}