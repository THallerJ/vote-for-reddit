package com.hallert.voteforreddit.ui.subreddits

import com.hallert.voteforreddit.RedditApp
import com.hallert.voteforreddit.database.RedditDatabase
import com.hallert.voteforreddit.database.SubredditEntity
import com.hallert.voteforreddit.user.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import net.dean.jraw.models.Subreddit
import net.dean.jraw.pagination.Paginator

class SubredditsRepository(private val database: RedditDatabase) {
    val subreddits: Flow<List<Subreddit>>
        get() = database.subredditDao.getSubreddits(UserManager.currentUser())


    fun addSubreddits() {
        CoroutineScope(IO).launch {
            if (!UserManager.isUserless()) {
                val pages = RedditApp.accountHelper.reddit.me().subreddits("subscriber")
                    .limit(Paginator.RECOMMENDED_MAX_LIMIT).build()
                val subredditList = mutableListOf<SubredditEntity>()

                for (page in pages) {
                    for (subreddit in page) {
                        val entity = SubredditEntity(
                            subreddit.id,
                            RedditApp.accountHelper.reddit.me().username,
                            subreddit.name,
                            subreddit
                        )

                        subredditList.add(entity)
                    }
                }

                database.subredditDao.insertSubreddits(subredditList.toList())
            }
        }
    }

}