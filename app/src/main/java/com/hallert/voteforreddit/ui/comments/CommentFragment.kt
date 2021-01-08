package com.hallert.voteforreddit.ui.comments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.hallert.voteforreddit.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@AndroidEntryPoint
class CommentFragment : Fragment(), CommentClickListener {
    private val commentsViewModel: CommentViewModel by viewModels()

    lateinit var textView: TextView

    private lateinit var adapter: CommentAdapter

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_comments, container, false)
        val id = arguments?.getString("submission_id")
        commentsViewModel.submissionId = id.toString()

        textView = root.findViewById(R.id.comments_text_view)
        textView.text = "COMMENTS\nSubmission ID: " + id

        adapter = CommentAdapter(this)

        commentsViewModel.comments.observe(viewLifecycleOwner, Observer {
            val data = commentsViewModel.comments.value

            if (data != null && data.isNotEmpty()) {
                adapter.data = data
            }
        })

        //TODO: re-enable caching once we can clear the cache when appropriate
        //commentsViewModel.initComments(id!!)
        return root
    }

    override fun onItemClick() {
        TODO("Not yet implemented")
    }
}