package com.hallert.voteforreddit.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import net.dean.jraw.JrawUtils
import net.dean.jraw.models.Submission

@Entity
data class SubmissionEntity constructor(
    @PrimaryKey
    val id: String,
    val submission: Submission,
    val saveTimeMillis: Long
)

class SubmissionTypeConverter {
    private val adapter: JsonAdapter<Submission> =
        JrawUtils.moshi.adapter(Submission::class.java).serializeNulls()

    @TypeConverter
    fun submissionToJson(submission: Submission): String {
        return adapter.toJson(submission)
    }

    @TypeConverter
    fun jsonToSubmission(json: String): Submission? {
        return adapter.fromJson(json)
    }
}