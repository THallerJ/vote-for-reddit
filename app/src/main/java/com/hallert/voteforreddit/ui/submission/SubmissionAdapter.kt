package com.hallert.voteforreddit.ui.submission

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hallert.voteforreddit.R
import com.hallert.voteforreddit.util.NumberFormatUtil
import kotlinx.android.synthetic.main.submission.view.*
import net.dean.jraw.models.Submission
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
        val sub = itemView.submission_sub_text
        val comments = itemView.submission_comment_count_text
        val karma = itemView.submission_karma_text
        val date = itemView.submission_date_text
        val thumbnail = itemView.submission_thumbnail_image

        fun bind(submission: Submission) {
            // TODO: Make  image icons smaller, perhaps 12dp
            title.text = submission.title
            sub.text = submission.subreddit
            comments.text = NumberFormatUtil.truncate(submission.commentCount)
            karma.text = NumberFormatUtil.truncate((submission.score))
            //date.text = DateFormatUtil.timespan(submission.created)
            date.text = "2h"

            thumbnail.layout(0, 0, 0, 0)

            val requestOptions = RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)

            Glide.with(itemView.context)
                .load(submission.thumbnail)
                .into(thumbnail)
        }
    }
}