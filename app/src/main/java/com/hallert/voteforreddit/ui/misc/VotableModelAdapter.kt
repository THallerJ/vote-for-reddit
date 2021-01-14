package com.hallert.voteforreddit.ui.misc

import androidx.recyclerview.widget.RecyclerView
import com.hallert.voteforreddit.R
import com.hallert.voteforreddit.RedditApp
import net.dean.jraw.models.Votable
import net.dean.jraw.models.VoteDirection

abstract class VotableModelAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    protected var userVoteDirection = VoteDirection.NONE

    protected fun setViewHolderColor(holder: RecyclerView.ViewHolder, votable: Votable) {
        val currentVoteDirection = votable.vote

        if (userVoteDirection == VoteDirection.UP) {
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

            userVoteDirection = VoteDirection.NONE
        } else if (userVoteDirection == VoteDirection.DOWN) {
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
            userVoteDirection = VoteDirection.NONE
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
}
