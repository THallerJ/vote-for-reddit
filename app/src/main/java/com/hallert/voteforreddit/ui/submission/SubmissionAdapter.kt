package com.hallert.voteforreddit.ui.submission

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hallert.voteforreddit.R
import kotlinx.android.synthetic.main.submission.view.*
import net.dean.jraw.models.Submission

class SubmissionAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var data: List<Submission> = ArrayList()
    set(value) {
        field = value
        notifyDataSetChanged()}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SubmissionViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.submission,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is SubmissionViewHolder -> {
                holder.bind(data[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class SubmissionViewHolder constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.title_text
        val score = itemView.post_point_text

        fun bind(submission: Submission) {
            title.text = submission.title
            score.text = submission.score.toString()
        }
    }
}