package com.hallert.voteforreddit.ui.submission

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.hallert.voteforreddit.R
import com.hallert.voteforreddit.util.DateFormatUtil
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
        private val linkFlair: TextView = itemView.submission_link_flair
        private val domain: TextView = itemView.submission_domain

        fun bind(submission: Submission) {
            thumbnail.layout(0, 0, 0, 0)
            val requestOptions = RequestOptions()
                .transform(CenterCrop(), RoundedCorners(20))

            Glide.with(itemView.context)
                .applyDefaultRequestOptions(requestOptions)
                .load(submission.thumbnail)
                .transition(DrawableTransitionOptions.withCrossFade())
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        thumbnail.visibility = View.GONE
                        return true
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }
                })
                .into(thumbnail)

            if (submission.linkFlairText.isNullOrBlank()) {
                linkFlair.visibility = View.GONE
            } else {
                linkFlair.text = submission.linkFlairText
            }

            title.text = submission.title
            sub.text = submission.subreddit
            comments.text = NumberFormatUtil.truncate(submission.commentCount)
            karma.text = NumberFormatUtil.truncate((submission.score))
            date.text = DateFormatUtil.timeSince(submission.created.time)
            domain.text = submission.domain
        }
    }
}