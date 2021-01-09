package com.hallert.voteforreddit.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.squareup.moshi.Types
import net.dean.jraw.JrawUtils
import net.dean.jraw.databind.Enveloped
import net.dean.jraw.models.Listing
import net.dean.jraw.models.NestedIdentifiable

@Entity
data class CommentEntity constructor(
    @PrimaryKey
    val id: String,
    val submissionId: String,
    val children: Listing<NestedIdentifiable>,
    val saveTimeMillis: Long
)

class CommentTypeConverter {
    private val type =
        Types.newParameterizedType(Listing::class.java, NestedIdentifiable::class.java)
    private val adapter =
        JrawUtils.moshi.adapter<Listing<NestedIdentifiable>>(type, Enveloped::class.java)
            .serializeNulls()

    @TypeConverter
    fun commentToJson(children: Listing<NestedIdentifiable>): String {
        return adapter.toJson(children)
    }

    @TypeConverter
    fun jsonToComment(json: String): Listing<NestedIdentifiable>? {
        return adapter.fromJson(json)
    }
}