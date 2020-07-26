package com.hallert.voteforreddit.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import net.dean.jraw.models.Submission
import net.dean.jraw.models.Subreddit

@Dao
interface SubmissionDao {
    @Query("SELECT submission FROM SubmissionEntity ORDER BY saveTimeMillis ASC")
    fun getAllSubmissions(): Flow<List<Submission>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSubmissions(submissions: List<SubmissionEntity>)

    @Query("DELETE FROM SubmissionEntity")
    fun clearDatabase()
}

@Dao
interface SubredditDao {
    @Query("SELECT subreddit FROM SubredditEntity WHERE username = :currentUser ORDER BY subredditName ASC")
    fun getSubreddits(currentUser: String): Flow<List<Subreddit>>

    @Query("DELETE FROM SubredditEntity")
    fun clearDatabase()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSubreddits(subreddits: List<SubredditEntity>)
}

@Database(entities = [SubmissionEntity::class, SubredditEntity::class], version = 1)
@TypeConverters(SubmissionTypeConverter::class, SubredditTypeConverter::class)
abstract class RedditDatabase : RoomDatabase() {
    abstract val submissionDao: SubmissionDao
    abstract val subredditDao: SubredditDao

    companion object {
        val DATABASE_NAME: String = "reddit_database"
    }
}

