package com.hallert.voteforreddit.injection

import android.content.Context
import com.hallert.voteforreddit.user.UserManager
import com.hallert.voteforreddit.user.UserPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import net.dean.jraw.RedditClient
import net.dean.jraw.android.AndroidHelper
import net.dean.jraw.android.ManifestAppInfoProvider
import net.dean.jraw.android.SharedPreferencesTokenStore
import net.dean.jraw.oauth.AccountHelper
import java.util.*
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object AccountModule {

    @Singleton
    @Provides
    fun provideManifestAppInfoProvider(
        @ApplicationContext context: Context
    ): ManifestAppInfoProvider {
        return ManifestAppInfoProvider(context)
    }

    @Singleton
    @Provides
    fun provideTokenStore(
        @ApplicationContext context: Context
    ): SharedPreferencesTokenStore {
        val tokenStore = SharedPreferencesTokenStore(context)
        tokenStore.load()
        tokenStore.autoPersist = true
        return tokenStore
    }

    @Singleton
    @Provides
    fun provideAccountHelper(
        tokenStore: SharedPreferencesTokenStore,
        provider: ManifestAppInfoProvider
    ): AccountHelper {
        return AndroidHelper.accountHelper(provider, UUID.randomUUID(), tokenStore)
    }

    @Singleton
    @Provides
    fun provideRedditClient(accountHelper: AccountHelper): RedditClient {
        return accountHelper.reddit
    }

    @Singleton
    @Provides
    fun provideUserManager(accountHelper: AccountHelper): UserManager {
        return UserManager(accountHelper)
    }

    @Singleton
    @Provides
    fun provideUserPref(): UserPreferences {
        return UserPreferences()
    }
}