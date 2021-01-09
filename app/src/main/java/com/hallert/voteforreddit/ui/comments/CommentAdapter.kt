package com.hallert.voteforreddit.ui.comments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hallert.voteforreddit.R
import net.dean.jraw.models.Comment
import net.dean.jraw.models.MoreChildren
import net.dean.jraw.models.NestedIdentifiable

class CommentAdapter constructor(private val listener: CommentClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val COMMENT = 0
    private val MORE_CHILDREN = 1

    var data: List<NestedIdentifiable> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            COMMENT -> {
                CommentViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.comment_item, parent, false)
                )
            }
            else -> {
                MoreChildrenViewHodler(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.more_comment_children_item, parent, false)
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (data[position]) {
            is Comment -> {
                (holder as CommentViewHolder).bind(data[position])
            }
            is MoreChildren -> {
                (holder as MoreChildrenViewHodler).bind(data[position])
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (data[position] is Comment) {
            COMMENT
        } else {
            MORE_CHILDREN
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class CommentViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val commentTextView: TextView = itemView.findViewById(R.id.comment)

        fun bind(identifiable: NestedIdentifiable) {
            val comment = identifiable as Comment
            commentTextView.text = comment.body
        }
    }

    class MoreChildrenViewHodler constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(identifiable: NestedIdentifiable) {
            // TODO: Implement function
        }
    }
}


interface CommentClickListener {
    fun onItemClick()
}