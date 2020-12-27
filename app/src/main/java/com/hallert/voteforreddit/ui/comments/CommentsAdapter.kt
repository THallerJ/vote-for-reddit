package com.hallert.voteforreddit.ui.comments

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CommentsAdapter constructor(private val listener: CommentClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

}

interface CommentClickListener {
    fun onItemClick()
}