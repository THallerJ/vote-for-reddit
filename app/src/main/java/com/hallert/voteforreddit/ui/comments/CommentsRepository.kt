package com.hallert.voteforreddit.ui.comments

import android.util.Log
import com.hallert.voteforreddit.database.CommentDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.dean.jraw.oauth.AccountHelper

class CommentsRepository(
    private val commentDao: CommentDao,
    private val accountHelper: AccountHelper
) {
    fun test(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            //var commentRequest = CommentsRequest(null, null, null, null, CommentSort.TOP)
            val root = accountHelper.reddit.submission(id).comments()

            // root.loadFully(accountHelper.reddit)

            val it = root.walkTree().forEach { Log.i("TESTING", "COMMENT: " + it.subject.body) }
        }
    }
}