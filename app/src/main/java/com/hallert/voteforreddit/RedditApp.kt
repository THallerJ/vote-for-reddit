package com.hallert.voteforreddit

import android.app.Application
import com.hallert.voteforreddit.database.RedditDatabase
import com.hallert.voteforreddit.user.Authentication
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltAndroidApp
class RedditApp : Application() {

    @Inject
    lateinit var auth: Authentication
    @Inject
    lateinit var db: RedditDatabase

    override fun onCreate() {
        super.onCreate()

        auth.authenticateOnStart(applicationContext)
        runBlocking { CoroutineScope(IO).launch { db.submissionDao.clearDatabase() } }
    }
}