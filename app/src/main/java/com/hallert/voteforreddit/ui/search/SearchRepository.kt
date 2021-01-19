package com.hallert.voteforreddit.ui.search

import android.util.Log
import com.hallert.voteforreddit.database.SearchDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.dean.jraw.oauth.AccountHelper

class SearchRepository constructor(
    private val searchDao: SearchDao, private val accountHelper: AccountHelper
) {
    fun searchSubreddits(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val sub = accountHelper.reddit.searchSubreddits().query(query).limit(5).build()
            val thing = sub.next()
            thing.forEach { sub ->
                //Log.i("TESTING", sub.name)
            }
        }

        fun searchPosts() {

        }
    }
}