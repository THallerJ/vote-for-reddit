package com.hallert.voteforreddit.ui.comments

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.dean.jraw.models.CommentSort
import net.dean.jraw.oauth.AccountHelper
import net.dean.jraw.references.CommentsRequest
import net.dean.jraw.tree.RootCommentNode

class CommentsViewModel @ViewModelInject constructor(
    //private val commentsRepository: CommentsRepository,
    private val accountHelper: AccountHelper,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    lateinit var submissionId: String

    fun test() {
        CoroutineScope(IO).launch {
            //var commentRequest = CommentsRequest(null, null, null, null, CommentSort.TOP)
            val root = accountHelper.reddit.submission(submissionId).comments()


           // root.loadFully(accountHelper.reddit)

            val it = root.walkTree().iterator()
        }
    }
}