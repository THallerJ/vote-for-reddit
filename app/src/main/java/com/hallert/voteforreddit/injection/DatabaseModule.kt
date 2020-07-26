package com.hallert.voteforreddit.injection

import android.content.Context
import androidx.room.Room
import com.hallert.voteforreddit.database.RedditDatabase
import com.hallert.voteforreddit.database.SubmissionDao
import com.hallert.voteforreddit.database.SubredditDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): RedditDatabase {
        return Room.databaseBuilder(
            context,
            RedditDatabase::class.java,
            RedditDatabase.DATABASE_NAME
        )
            .build()
    }

    @Singleton
    @Provides
    fun provideSubmissionDao(redditDatabase: RedditDatabase): SubmissionDao {
        return redditDatabase.submissionDao
    }

    @Singleton
    @Provides
    fun provideSubredditDbo(redditDatabase: RedditDatabase): SubredditDao {
        return redditDatabase.subredditDao
    }
}