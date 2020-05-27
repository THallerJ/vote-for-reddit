package com.hallert.voteforreddit.ui.submission

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.hallert.voteforreddit.R
import com.hallert.voteforreddit.util.NumberFormatUtil
import kotlinx.android.synthetic.main.submission.view.*
import net.dean.jraw.models.Submission

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
        private val title: TextView = itemView.submission_title_text
        private val sub: TextView = itemView.submission_sub_text
        private val comments: TextView = itemView.submission_comment_count_text
        private val karma: TextView = itemView.submission_karma_text
        private val date: TextView = itemView.submission_date_text
        private val thumbnail: ImageView = itemView.submission_thumbnail_image

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
                .error(R.drawable.ic_launcher_background)
                .transform(CenterCrop(), RoundedCorners(20))

            Glide.with(itemView.context)
                .applyDefaultRequestOptions(requestOptions)
                .load(submission.thumbnail)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(thumbnail)
        }
    }
}