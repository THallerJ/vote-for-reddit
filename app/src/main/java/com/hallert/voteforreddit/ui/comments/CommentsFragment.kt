package com.hallert.voteforreddit.ui.comments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.hallert.voteforreddit.R
import dagger.hilt.android.AndroidEntryPoint
import net.dean.jraw.oauth.AccountHelper
import javax.inject.Inject

lateinit var textView: TextView


class CommentsFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_comments, container, false)
        val id = this.arguments?.getString("submission_id")

        textView = root.findViewById(R.id.comments_text_view)
        textView.text = "COMMENTS\nSubmission ID: " + id // use inspect to get Submission from SubmissionReference

        return root
    }
}