package com.hallert.voteforreddit.user

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.dean.jraw.RedditClient
import net.dean.jraw.android.SharedPreferencesTokenStore
import net.dean.jraw.android.SimpleAndroidLogAdapter
import net.dean.jraw.http.SimpleHttpLogger
import net.dean.jraw.oauth.AccountHelper
import net.dean.jraw.oauth.DeferredPersistentTokenStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Authentication @Inject constructor(
    private val accountHelper: AccountHelper,
    private val tokenStore: SharedPreferencesTokenStore
) {
    lateinit var client: RedditClient

    fun authenticateOnStart(context: Context) {
        accountHelper.onSwitch { reddit: RedditClient ->
            val logAdapter = SimpleAndroidLogAdapter(Log.INFO)

            reddit.logger = SimpleHttpLogger(SimpleHttpLogger.DEFAULT_LINE_LENGTH, logAdapter)
            reddit.autoRenew = true
        }

        val deferredToken: DeferredPersistentTokenStore = tokenStore

        // TODO: This crashes on first startup if not connected to the internet
        if (deferredToken.usernames.isNotEmpty() && deferredToken.usernames[0] != "<userless>") {
            accountHelper.trySwitchToUser(deferredToken.usernames[0])
            client = accountHelper.reddit
        } else {
            runBlocking {
                CoroutineScope(Dispatchers.IO).launch {
                    accountHelper.switchToUserless()
                    client = accountHelper.reddit
                }.join()
            }
        }
    }

    fun isUserless(): Boolean {
        return accountHelper.reddit.authMethod.isUserless
    }

    fun logout() {
        accountHelper.reddit.authManager.revokeRefreshToken()
        accountHelper.reddit.authManager.revokeAccessToken()

        // TODO: Switch to other logged in accounts if possible
        accountHelper.switchToUserless()
    }
}

