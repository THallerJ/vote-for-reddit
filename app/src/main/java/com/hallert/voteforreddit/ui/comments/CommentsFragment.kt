package com.hallert.voteforreddit.ui.comments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.hallert.voteforreddit.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


lateinit var textView: TextView

@AndroidEntryPoint
class CommentsFragment: Fragment() {

    private val commentsViewModel: CommentsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_comments, container, false)
        val id = arguments?.getString("submission_id")
        commentsViewModel.submissionId = id.toString()

        textView = root.findViewById(R.id.comments_text_view)
        textView.text = "COMMENTS\nSubmission ID: " + id // use inspect to get Submission from SubmissionReference

        commentsViewModel.getComments()
        return root
    }
}