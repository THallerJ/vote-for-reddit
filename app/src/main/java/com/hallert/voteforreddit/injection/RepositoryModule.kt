package com.hallert.voteforreddit.injection

import com.hallert.voteforreddit.database.CommentDao
import com.hallert.voteforreddit.database.SearchDao
import com.hallert.voteforreddit.database.SubmissionDao
import com.hallert.voteforreddit.database.SubredditDao
import com.hallert.voteforreddit.ui.comments.CommentRepository
import com.hallert.voteforreddit.ui.search.SearchRepository
import com.hallert.voteforreddit.ui.submission.SubmissionRepository
import com.hallert.voteforreddit.ui.subreddits.SubredditRepository
import com.hallert.voteforreddit.user.UserManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import net.dean.jraw.oauth.AccountHelper
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
    fun provideSubmissionRepository(
        submissionDao: SubmissionDao, accountHelper: AccountHelper
    ): SubmissionRepository {
        return SubmissionRepository(submissionDao, accountHelper)
    }

    @Singleton
    @Provides
    fun provideSubredditsRepopsitory(
        subredditDao: SubredditDao,
        accountHelper: AccountHelper,
        userManager: UserManager
    ): SubredditRepository {
        return SubredditRepository(subredditDao, accountHelper, userManager)
    }

    @Singleton
    @Provides
    fun provideCommentRepository(
        commentDao: CommentDao,
        accountHelper: AccountHelper
    ): CommentRepository {
        return CommentRepository(commentDao, accountHelper)
    }

    @Singleton
    @Provides
    fun provideSearchRepository(
        searchDao: SearchDao,
        accountHelper: AccountHelper
    ): SearchRepository {
        return SearchRepository(searchDao, accountHelper)
    }
}