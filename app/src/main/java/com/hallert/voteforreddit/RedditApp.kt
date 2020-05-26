package com.hallert.voteforreddit

import android.app.Application
import android.util.Log
import com.hallert.voteforreddit.user.Authentication
import kotlinx.coroutines.runBlocking
import net.dean.jraw.android.SharedPreferencesTokenStore
import net.dean.jraw.oauth.AccountHelper


class RedditApp : Application() {
    override fun onCreate() {
        super.onCreate()


        val auth = Authentication()

        auth.authenticateOnStart(applicationContext)

        accountHelper = auth.accountHelper
        tokenStore = auth.tokenStore
    }

    // TODO: Remove after adding Dagger injection
    companion object {
        lateinit var accountHelper: AccountHelper
        lateinit var tokenStore: SharedPreferencesTokenStore
    }
}