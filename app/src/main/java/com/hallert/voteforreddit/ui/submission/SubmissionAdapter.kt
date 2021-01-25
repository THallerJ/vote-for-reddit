package com.hallert.voteforreddit.ui.submission

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.hallert.voteforreddit.R
import com.hallert.voteforreddit.RedditApp
import com.hallert.voteforreddit.ui.misc.VotableModelAdapter
import com.hallert.voteforreddit.util.DateFormatUtil
import com.hallert.voteforreddit.util.NumberFormatUtil
import kotlinx.android.synthetic.main.submission.view.*
import net.dean.jraw.models.Submission
import net.dean.jraw.models.VoteDirection

class SubmissionAdapter constructor(
    private val listener: SubmissionClickListener
) :
    VotableModelAdapter() {

    private val NO_THUMBNAIL = 0
    private val THUMBNAIL = 1

    var isMultireddit = true

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
        setViewHolderColor(holder, data[position])

        if (!data[position].isSelfPost) {
            (holder as ThumbnailViewHolder).bind(data[position], isMultireddit, listener)
        } else {
            (holder as NoThumbnailViewHolder).bind(data[position], isMultireddit, listener)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (!data[position].isSelfPost) {
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
        private val submissionLink: ImageView = itemView.submission_link
        private val author: TextView = itemView.submission_author
        private val nsfw: TextView = itemView.submission_nsfw

        fun bind(
            submission: Submission,
            isMultireddit: Boolean,
            listener: SubmissionClickListener
        ) {
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

            if (submission.isNsfw) {
                nsfw.visibility = View.VISIBLE
            } else {
                nsfw.visibility = View.GONE
            }

            title.text = submission.title

            if (submission.isStickied) {
                title.setTextColor(RedditApp.appContext.getColor(R.color.stickyColor))
            } else {
                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
                    title.setTextColor(RedditApp.appContext.getColor(R.color.primaryTextLight))
                } else {
                    title.setTextColor(RedditApp.appContext.getColor(R.color.primaryTextDark))
                }
            }

            val domainParser = SubmissionDomainParser()

            when (domainParser.parse(submission.domain)) {
                SubmissionDomainType.INTERNET -> {
                    submissionLink.setImageResource(R.drawable.ic_link)
                }
                SubmissionDomainType.IMAGE -> {
                    submissionLink.setImageResource(R.drawable.ic_image)
                }
                SubmissionDomainType.VIDEO -> {
                    submissionLink.setImageResource(R.drawable.ic_video)
                }
                SubmissionDomainType.TWITTER -> {
                    submissionLink.setImageResource(R.drawable.ic_twitter)
                }
                SubmissionDomainType.YOUTUBE -> {
                    submissionLink.setImageResource(R.drawable.ic_youtube)
                }
            }

            comments.text = NumberFormatUtil.truncate(submission.commentCount)
            karma.text = NumberFormatUtil.truncate((submission.score))
            date.text = DateFormatUtil.timeSince(submission.created.time)
            author.text = submission.author

            if (isMultireddit) {
                sub.visibility = View.VISIBLE
                sub.text = submission.subreddit
            } else {
                sub.visibility = View.GONE
            }
        }
    }

    class NoThumbnailViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.submission_title_text
        private val sub: TextView = itemView.submission_sub_text
        private val comments: TextView = itemView.submission_comment_count_text
        private val karma: TextView = itemView.submission_karma_text
        private val date: TextView = itemView.submission_date_text
        private val linkFlair: TextView = itemView.submission_link_flair
        private val author: TextView = itemView.submission_author
        private val nsfw: TextView = itemView.submission_nsfw

        fun bind(
            submission: Submission,
            isMultireddit: Boolean,
            listener: SubmissionClickListener
        ) {
            linkFlair.text = submission.linkFlairText

            if (submission.linkFlairText.isNullOrBlank()) {
                linkFlair.visibility = View.GONE
            } else {
                linkFlair.visibility = View.VISIBLE
            }

            if (submission.isNsfw) {
                nsfw.visibility = View.VISIBLE
            } else {
                nsfw.visibility = View.GONE
            }

            itemView.setOnClickListener {
                listener.onItemClick(submission, adapterPosition)
            }

            itemView.setOnLongClickListener(View.OnLongClickListener {
                listener.onItemLongClick()
                true
            })

            title.text = submission.title

            title.text = submission.title

            if (submission.isStickied) {
                title.setTextColor(RedditApp.appContext.getColor(R.color.stickyColor))
            } else {
                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
                    Log.i("TESTING", "LIGHT MODE")
                    title.setTextColor(RedditApp.appContext.getColor(R.color.primaryTextLight))
                } else {
                    Log.i("TESTING", "DARK MODE")
                    title.setTextColor(RedditApp.appContext.getColor(R.color.primaryTextDark))
                }
            }

            comments.text = NumberFormatUtil.truncate(submission.commentCount)
            karma.text = NumberFormatUtil.truncate((submission.score))
            date.text = DateFormatUtil.timeSince(submission.created.time)
            author.text = submission.author

            if (isMultireddit) {
                sub.visibility = View.VISIBLE
                sub.text = submission.subreddit
            } else {
                sub.visibility = View.GONE
            }
        }
    }

    fun itemSwipedRight(position: Int) {
        userVoteDirection = VoteDirection.UP
        notifyItemChanged(position)
    }

    fun itemSwipedLeft(position: Int) {
        userVoteDirection = VoteDirection.DOWN
        notifyItemChanged(position)
    }
}


interface SubmissionClickListener {
    fun onItemClick(submission: Submission, position: Int)
    fun onItemThumbnailClick(submission: Submission, position: Int)
    fun onItemLongClick()
}