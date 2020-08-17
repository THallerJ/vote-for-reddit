package com.hallert.voteforreddit.injection

import android.content.Context
import com.hallert.voteforreddit.database.SubmissionDao
import com.hallert.voteforreddit.database.SubredditDao
import com.hallert.voteforreddit.ui.submission.SubmissionRepository
import com.hallert.voteforreddit.ui.subreddits.SubredditsRepository
import com.hallert.voteforreddit.user.UserManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import net.dean.jraw.RedditClient
import net.dean.jraw.oauth.AccountHelper
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
    fun provideSubmissionRepository(
        submissionDao: SubmissionDao, accountHelper: AccountHelper, @ApplicationContext context: Context
    ): SubmissionRepository {
        return SubmissionRepository(submissionDao, accountHelper, context)
    }

    @Singleton
    @Provides
    fun provideSubredditsRepopsitory(
        subredditDao: SubredditDao,
        accountHelper: AccountHelper,
        userManager: UserManager
    ): SubredditsRepository {
        return SubredditsRepository(subredditDao, accountHelper, userManager)
    }
}