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
import com.hallert.voteforreddit.RedditApp
import com.hallert.voteforreddit.util.DateFormatUtil
import com.hallert.voteforreddit.util.NumberFormatUtil
import kotlinx.android.synthetic.main.submission.view.*
import net.dean.jraw.models.Submission
import net.dean.jraw.models.VoteDirection

class SubmissionAdapter constructor(
    private val listener: SubmissionClickListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val NO_THUMBNAIL = 0
    private val THUMBNAIL = 1

    private var upvote: Boolean = false
    private var downvote: Boolean = false

    var data: List<Submission> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            THUMBNAIL -> {
                ThumbnailViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.submission, parent, false)
                )
            }
            else -> {
                NoThumbnailViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.submission_no_thumbnail, parent, false)
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (data[position].thumbnail != "self") {
            (holder as ThumbnailViewHolder).bind(data[position], listener)
        } else {
            (holder as NoThumbnailViewHolder).bind(data[position], listener)
        }

        setViewHolderColor(holder, position)
    }

    private fun setViewHolderColor(holder: RecyclerView.ViewHolder, position: Int) {
        val currentVoteDirection = data[position].vote

        if (upvote) {
            if (currentVoteDirection == VoteDirection.UP) {
                holder.itemView.setBackgroundColor(
                    RedditApp.appContext.resources.getColor(
                        R.color.colorPrimary,
                        null
                    )
                )
            } else {
                holder.itemView.setBackgroundColor(
                    RedditApp.appContext.resources.getColor(
                        R.color.upvoteTintColor,
                        null
                    )
                )
            }

            upvote = false
        } else if (downvote) {
            if (currentVoteDirection == VoteDirection.DOWN) {
                holder.itemView.setBackgroundColor(
                    RedditApp.appContext.resources.getColor(
                        R.color.colorPrimary,
                        null
                    )
                )
            } else {
                holder.itemView.setBackgroundColor(
                    RedditApp.appContext.resources.getColor(
                        R.color.downvoteTintColor,
                        null
                    )
                )
            }

            downvote = false
        } else {
            if (currentVoteDirection == VoteDirection.UP) {
                holder.itemView.setBackgroundColor(
                    RedditApp.appContext.resources.getColor(
                        R.color.upvoteTintColor,
                        null
                    )
                )
            } else if (currentVoteDirection == VoteDirection.DOWN) {
                holder.itemView.setBackgroundColor(
                    RedditApp.appContext.resources.getColor(
                        R.color.downvoteTintColor,
                        null
                    )
                )
            } else {
                holder.itemView.setBackgroundColor(
                    RedditApp.appContext.resources.getColor(
                        R.color.colorPrimary,
                        null
                    )
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (data[position].thumbnail != "self") {
            THUMBNAIL
        } else {
            NO_THUMBNAIL
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ThumbnailViewHolder constructor(
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
        private val submissionLink: ImageView = itemView.submission_link

        fun bind(submission: Submission, listener: SubmissionClickListener) {
            thumbnail.layout(0, 0, 0, 0)
            val requestOptions = RequestOptions()
                .error(R.drawable.ic_submission_link)
                .transform(CenterCrop(), RoundedCorners(20))

            Glide.with(itemView.context)
                .applyDefaultRequestOptions(requestOptions)
                .load(submission.thumbnail)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(thumbnail)

            itemView.setOnClickListener {
                listener.onItemClick(submission, adapterPosition)
            }

            itemView.setOnLongClickListener(View.OnLongClickListener {
                listener.onItemLongClick()
                true
            })

            thumbnail.setOnClickListener {
                listener.onItemThumbnailClick(
                    submission,
                    adapterPosition
                )
            }

            submissionLink.setOnClickListener {
                listener.onItemThumbnailClick(
                    submission,
                    adapterPosition
                )
            }

            linkFlair.text = submission.linkFlairText

            if (submission.linkFlairText.isNullOrBlank()) {
                linkFlair.visibility = View.GONE
            } else {
                linkFlair.visibility = View.VISIBLE
            }

            title.text = submission.title
            sub.text = submission.subreddit
            comments.text = NumberFormatUtil.truncate(submission.commentCount)
            karma.text = NumberFormatUtil.truncate((submission.score))
            date.text = DateFormatUtil.timeSince(submission.created.time)
            domain.text = submission.domain
        }
    }

    class NoThumbnailViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.submission_title_text
        private val sub: TextView = itemView.submission_sub_text
        private val comments: TextView = itemView.submission_comment_count_text
        private val karma: TextView = itemView.submission_karma_text
        private val date: TextView = itemView.submission_date_text
        private val linkFlair: TextView = itemView.submission_link_flair
        private val domain: TextView = itemView.submission_domain

        fun bind(submission: Submission, listener: SubmissionClickListener) {
            linkFlair.text = submission.linkFlairText

            if (submission.linkFlairText.isNullOrBlank()) {
                linkFlair.visibility = View.GONE
            } else {
                linkFlair.visibility = View.VISIBLE
            }

            itemView.setOnClickListener {
                listener.onItemClick(submission, adapterPosition)
            }

            itemView.setOnLongClickListener(View.OnLongClickListener {
                listener.onItemLongClick()
                true
            })

            title.text = submission.title
            sub.text = submission.subreddit
            comments.text = NumberFormatUtil.truncate(submission.commentCount)
            karma.text = NumberFormatUtil.truncate((submission.score))
            date.text = DateFormatUtil.timeSince(submission.created.time)
            domain.text = submission.domain
        }
    }

    fun itemSwipedRight(position: Int) {
        upvote = true
        notifyItemChanged(position)
    }

    fun itemSwipedLeft(position: Int) {
        downvote = true
        notifyItemChanged(position)
    }
}


interface SubmissionClickListener {
    fun onItemClick(submission: Submission, position: Int)
    fun onItemThumbnailClick(submission: Submission, position: Int)
    fun onItemLongClick()
}