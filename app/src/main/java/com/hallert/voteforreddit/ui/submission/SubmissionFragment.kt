package com.hallert.voteforreddit.ui.submission

import SwipeVoteCallBack
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.hallert.voteforreddit.R
import com.hallert.voteforreddit.ui.misc.RecyclerLoadListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_submissions.*
import net.dean.jraw.models.Submission

@AndroidEntryPoint
class SubmissionsFragment : Fragment(), SubmissionClickListener {
    private val submissionViewModel: SubmissionViewModel by viewModels()
    private lateinit var adapter: SubmissionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_submissions, container, false)

        val toolbarTitle = activity?.findViewById<TextView>(R.id.bottom_nav_title)

        if (toolbarTitle != null) {
            toolbarTitle.text = submissionViewModel.getSubredditName()
        }

        adapter = SubmissionAdapter(this)

        submissionViewModel.submissions.observe(viewLifecycleOwner, Observer { submissions ->
            adapter.data = submissions
        })

        submissionViewModel.isLoading.observe(viewLifecycleOwner, Observer { loading ->
            submission_swipe_refresh.isRefreshing = loading
        })

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        submission_recycler_view.layoutManager = LinearLayoutManager(context)
        submission_recycler_view.adapter = adapter
        submissionViewModel.getNextPage()

        submission_recycler_view.addItemDecoration(
            DividerItemDecoration(
                submission_recycler_view.context,
                DividerItemDecoration.VERTICAL
            )
        )

        val swipeCallback =
            object : SwipeVoteCallBack(this!!.context, adapter) {
                override fun onSwipeLeft() {
                    Toast.makeText(
                        context,
                        "TODO: Downvote submission",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onSwipeRight() {
                    Toast.makeText(
                        context,
                        "TODO: Upvote submission",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        val itemTouchHelper: ItemTouchHelper = ItemTouchHelper(swipeCallback)
        itemTouchHelper.attachToRecyclerView(submission_recycler_view)
        submission_recycler_view.itemAnimator!!.changeDuration = 0

        submission_recycler_view.addOnScrollListener(object : RecyclerLoadListener() {
            override fun atBottom() {
                submissionViewModel.getNextPage()
            }
        })

        submission_swipe_refresh.setOnRefreshListener {
            submissionViewModel.refresh()
        }
    }

    // These methods handle click events on items in the RecyclerView
    override fun onItemClick(submission: Submission, position: Int) {
        Toast.makeText(
            context,
            "TODO: Open submission " + submission.title, Toast.LENGTH_SHORT
        )
            .show()
    }

    override fun onItemThumbnailClick(submission: Submission, position: Int) {
        Toast.makeText(
            context,
            "TODO: Open thumbnail/link " + submission.title, Toast.LENGTH_SHORT
        )
            .show()
    }
}
