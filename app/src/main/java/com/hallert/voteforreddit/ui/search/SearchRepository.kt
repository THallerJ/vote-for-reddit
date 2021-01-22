package com.hallert.voteforreddit.ui.search

import com.hallert.voteforreddit.database.SearchDao
import com.hallert.voteforreddit.database.SubredditSearchEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import net.dean.jraw.models.Subreddit
import net.dean.jraw.oauth.AccountHelper

class SearchRepository constructor(
    private val searchDao: SearchDao, private val accountHelper: AccountHelper
) {

    fun searchSubreddits(query: String) {
        CoroutineScope(IO).launch {
            val builder = accountHelper.reddit.searchSubreddits().query(query).limit(5).build()
            val subreddits = builder.next()

            val subredditList = mutableListOf<SubredditSearchEntity>()

            subreddits.forEach { sub ->
                val entity = SubredditSearchEntity(sub.id, query, sub, System.currentTimeMillis())
                subredditList.add(entity)
            }

            searchDao.insertSearchSubreddits(subredditList.toList())
        }
    }

    fun getSearchSubreddits(query: String): Flow<List<Subreddit>> {
        return searchDao.getSearchSubreddits(query)
    }

    fun clearNonQuery(query: String) {
        CoroutineScope(IO).launch {
            searchDao.clearNonQuery(query)
        }
    }
}

