package com.hallert.voteforreddit.user

import android.content.Context
import android.util.Log
import com.hallert.voteforreddit.RedditApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.dean.jraw.RedditClient
import net.dean.jraw.android.AndroidHelper
import net.dean.jraw.android.ManifestAppInfoProvider
import net.dean.jraw.android.SharedPreferencesTokenStore
import net.dean.jraw.android.SimpleAndroidLogAdapter
import net.dean.jraw.http.SimpleHttpLogger
import net.dean.jraw.oauth.AccountHelper
import net.dean.jraw.oauth.DeferredPersistentTokenStore
import java.util.*

class Authentication {
    // TODO: Make a seperate class for retrieiving RedditClient, perhaps using observer patter with RxJava
    // TODO: Make another userManger class for managing different users. This class should only be used for initial authentication.
    lateinit var tokenStore: SharedPreferencesTokenStore
    lateinit var accountHelper: AccountHelper
    lateinit var client: RedditClient

    fun authenticateOnStart(context: Context) {
        val provider = ManifestAppInfoProvider(context)
        val deviceUuid = UUID.randomUUID()

        tokenStore = SharedPreferencesTokenStore(context)
        tokenStore.load()
        tokenStore.autoPersist = true

        accountHelper = AndroidHelper.accountHelper(provider, deviceUuid, tokenStore)

        accountHelper.onSwitch { reddit: RedditClient ->
            val logAdapter = SimpleAndroidLogAdapter(Log.INFO)

            reddit.logger = SimpleHttpLogger(SimpleHttpLogger.DEFAULT_LINE_LENGTH, logAdapter)
            reddit.autoRenew = true
        }

        val deferredToken: DeferredPersistentTokenStore = tokenStore


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
        RedditApp.accountHelper.reddit.authManager.revokeRefreshToken()
        RedditApp.accountHelper.reddit.authManager.revokeAccessToken()

        // TODO: Switch to other logged in accounts if possible
        RedditApp.accountHelper.switchToUserless()
    }
}

