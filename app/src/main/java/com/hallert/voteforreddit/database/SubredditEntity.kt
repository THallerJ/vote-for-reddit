package com.hallert.voteforreddit.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import net.dean.jraw.JrawUtils
import net.dean.jraw.models.Subreddit

@Entity
data class SubredditEntity constructor(
    @PrimaryKey
    val id: String,
    val username: String,
    val subredditName: String,
    val subreddit: Subreddit
)

class SubredditTypeConverter {
    private val adapter: JsonAdapter<Subreddit> =
        JrawUtils.moshi.adapter(Subreddit::class.java).serializeNulls()

    @TypeConverter
    fun subredditToJson(subreddit: Subreddit): String {
        return adapter.toJson(subreddit)
    }

    @TypeConverter
    fun jsonToSubreddit(json: String): Subreddit? {
        return adapter.fromJson(json)
    }
}