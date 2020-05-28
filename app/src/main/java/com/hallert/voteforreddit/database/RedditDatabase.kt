package com.hallert.voteforreddit.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import net.dean.jraw.models.Submission

@Dao
interface SubmissionDao {
    @Query("SELECT submission FROM SubmissionEntity ORDER BY saveTimeMillis ASC")
    fun getAllSubmissions(): Flow<List<Submission>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSubmissions(submissions: List<SubmissionEntity>)

    @Query("DELETE FROM SubmissionEntity")
    fun clearDatabase()
}

@Database(entities = [SubmissionEntity::class], version = 1)
@TypeConverters(SubmissionTypeConverter::class)
abstract class RedditDatabase : RoomDatabase() {
    abstract val submissionDao: SubmissionDao
}

