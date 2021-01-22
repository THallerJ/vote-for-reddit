package com.hallert.voteforreddit.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import net.dean.jraw.models.Subreddit

@Entity
data class SubredditSearchEntity(
    @PrimaryKey
    val id: String,
    val searchQuery: String,
    val subreddit: Subreddit,
    val saveTimeMillis: Long
)