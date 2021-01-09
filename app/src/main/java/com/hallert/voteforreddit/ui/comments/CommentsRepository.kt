package com.hallert.voteforreddit.ui.comments

import com.hallert.voteforreddit.database.CommentDao
import com.hallert.voteforreddit.database.CommentEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import net.dean.jraw.oauth.AccountHelper
import java.util.*


class CommentsRepository(
    private val commentDao: CommentDao,
    private val accountHelper: AccountHelper
) {
    var id: String = null.toString()

    fun setupComments(id: String) {
        this.id = id

        CoroutineScope(IO).launch {
            val root = accountHelper.reddit.submission(id).comments()
            val replies = root.children

            val entity =
                CommentEntity(UUID.randomUUID().toString(), id, replies, System.currentTimeMillis())

            commentDao.insertComments(entity)
        }
    }

    fun getComments(id: String): Flow<List<CommentEntity>> {
        return commentDao.getComments(id)
    }
}