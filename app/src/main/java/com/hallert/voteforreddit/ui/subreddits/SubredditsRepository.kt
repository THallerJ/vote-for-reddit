package com.hallert.voteforreddit.ui.subreddits

import com.hallert.voteforreddit.database.SubredditDao
import com.hallert.voteforreddit.database.SubredditEntity
import com.hallert.voteforreddit.user.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import net.dean.jraw.models.Subreddit
import net.dean.jraw.oauth.AccountHelper
import net.dean.jraw.pagination.Paginator

class SubredditsRepository(
    private val subredditDao: SubredditDao,
    private val accountHelper: AccountHelper,
    private val userManager: UserManager
) {
    val subreddits: Flow<List<Subreddit>>
        get() = subredditDao.getSubreddits(userManager.currentUser())


    fun addSubreddits() {
        CoroutineScope(IO).launch {
            if (!userManager.isUserless()) {
                val pages = accountHelper.reddit.me().subreddits("subscriber")
                    .limit(Paginator.RECOMMENDED_MAX_LIMIT).build()
                val subredditList = mutableListOf<SubredditEntity>()

                for (page in pages) {
                    for (subreddit in page) {
                        val entity = SubredditEntity(
                            subreddit.id,
                            accountHelper.reddit.me().username,
                            subreddit.name,
                            subreddit
                        )

                        subredditList.add(entity)
                    }
                }

                subredditDao.insertSubreddits(subredditList.toList())
            }
        }
    }
}