package com.hallert.voteforreddit

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.submission.view.*
import net.dean.jraw.models.Submission
import org.w3c.dom.Text

class SubmissionsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var data: List<Submission> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SubmissionViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.submission, parent, false)
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

    fun setData(submissions: List<Submission>) {
        data = submissions
        notifyDataSetChanged()
    }

    class SubmissionViewHolder constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        val thing = itemView.test_text


        fun bind(submission: Submission) {
            thing.text = submission.title
        }
    }
}