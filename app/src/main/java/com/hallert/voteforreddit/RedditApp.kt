package com.hallert.voteforreddit

import android.app.Application
import com.hallert.voteforreddit.user.Authentication
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class RedditApp : Application() {

    @Inject lateinit var auth : Authentication

    override fun onCreate() {
        super.onCreate()

        auth.authenticateOnStart(applicationContext)
    }
}