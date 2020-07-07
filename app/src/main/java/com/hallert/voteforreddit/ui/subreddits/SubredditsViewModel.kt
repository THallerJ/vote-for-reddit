package com.hallert.voteforreddit.ui.subreddits

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hallert.voteforreddit.RedditApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.dean.jraw.models.Subreddit
import net.dean.jraw.pagination.Paginator

class SubredditsViewModel : ViewModel() {
    val subreddits = MutableLiveData<List<Subreddit>>()

    fun getSubs() {
        CoroutineScope(IO).launch {
            if (!RedditApp.accountHelper.reddit.authMethod.isUserless) {
                val pages = RedditApp.accountHelper.reddit.me().subreddits("subscriber")
                    .limit(Paginator.RECOMMENDED_MAX_LIMIT).build()
                val subredditList = mutableListOf<Subreddit>()

                for (page in pages) {
                    subredditList.addAll(page)
                }

                withContext(Main) {
                    subreddits.value = subredditList
                }
            }
        }
    }
}