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

    @Query("UPDATE SubmissionEntity SET submission = :submission WHERE id = :id")
    fun updateSubmission(submission: Submission, id: String)
}

@Dao
interface SubredditDao {
    @Query("SELECT subreddit FROM SubredditEntity WHERE username = :currentUser ORDER BY LOWER(subredditName) ASC")
    fun getSubreddits(currentUser: String): Flow<List<Subreddit>>

    @Query("DELETE FROM SubredditEntity WHERE username = :currentUser")
    fun clearDatabase(currentUser: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSubreddits(subreddits: List<SubredditEntity>)
}

@Dao
interface CommentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertComments(comments: CommentEntity)

    @Query("SELECT * FROM CommentEntity WHERE submissionId = :id ORDER BY saveTimeMillis ASC")
    fun getComments(id: String): Flow<List<CommentEntity>>

    @Query("DELETE FROM CommentEntity")
    fun clearDatabase()
}

@Database(
    entities = [SubmissionEntity::class, SubredditEntity::class, CommentEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    SubmissionTypeConverter::class,
    SubredditTypeConverter::class,
    CommentTypeConverter::class
)
abstract class RedditDatabase : RoomDatabase() {
    abstract val submissionDao: SubmissionDao
    abstract val subredditDao: SubredditDao
    abstract val commentDao: CommentDao

    companion object {
        val DATABASE_NAME: String = "reddit_database"
    }
}

