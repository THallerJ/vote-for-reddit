package com.hallert.voteforreddit.ui.submission

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hallert.voteforreddit.R
import com.hallert.voteforreddit.Utils
import kotlinx.android.synthetic.main.submission.view.*
import net.dean.jraw.models.Submission
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*
import kotlin.collections.ArrayList

class SubmissionAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var data: List<Submission> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

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
        when (holder) {
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
        val title = itemView.submission_title_text
        val score = itemView.submission_score_text
        val sub = itemView.submission_sub_text
        val comments = itemView.submission_comment_count_text
        val karma = itemView.submission_karma_text
        val date = itemView.submission_date_text

        fun bind(submission: Submission) {
            // TODO: Make  image icons smaller, perhaps 12dp
            title.text = submission.title
            score.text = submission.score.toString()
            sub.text = submission.subreddit
            comments.text = Utils.formatThousand(submission.commentCount)
            karma.text = Utils.formatThousand((submission.score))
            //date.text = submission.created.toString()
            date.text = "2h"
        }
    }
}