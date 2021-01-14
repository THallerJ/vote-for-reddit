package com.hallert.voteforreddit.ui.submission

import SwipeVoteCallBack
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.hallert.voteforreddit.R
import com.hallert.voteforreddit.RedditApp
import com.hallert.voteforreddit.ui.misc.RecyclerDecoration
import com.hallert.voteforreddit.ui.misc.RecyclerLoadListener
import com.hallert.voteforreddit.user.UserManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_submissions.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import net.dean.jraw.models.Submission
import net.dean.jraw.models.SubredditSort
import net.dean.jraw.models.TimePeriod
import net.dean.jraw.models.VoteDirection
import javax.inject.Inject

@AndroidEntryPoint
class SubmissionsFragment : Fragment(), SubmissionClickListener {
    @ExperimentalCoroutinesApi
    private val submissionViewModel: SubmissionViewModel by viewModels()
    private lateinit var adapter: SubmissionAdapter
    lateinit var observer: SubmissionFragmentObserver

    @Inject
    lateinit var userManager: UserManager

    @ExperimentalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_submissions, container, false)

        adapter = SubmissionAdapter(this)

        submissionViewModel.submissions.observe(viewLifecycleOwner, Observer { submissions ->
            adapter.data = submissions
        })

        submissionViewModel.isLoading.observe(viewLifecycleOwner, Observer { loading ->
            submission_swipe_refresh.isRefreshing = loading
        })

        return root
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    @ExperimentalCoroutinesApi
    private fun initRecyclerView() {
        submission_recycler_view.layoutManager = LinearLayoutManager(context)
        submission_recycler_view.adapter = adapter
        submissionViewModel.getNextPage()

        val decoration = RecyclerDecoration()
        decoration.addDividerLine(context, submission_recycler_view)

        val swipeCallback =
            object : SwipeVoteCallBack(this.context, adapter) {
                override fun onSwipeLeft(position: Int) {
                    if (!userManager.isUserless()) {
                        adapter.itemSwipedLeft(position)

                        submissionViewModel.voteSubmission(
                            adapter.data[position],
                            VoteDirection.DOWN
                        )
                    } else {
                        observer.loginUser()
                    }

                }

                override fun onSwipeRight(position: Int) {
                    if (!userManager.isUserless()) {
                        adapter.itemSwipedRight(position)

                        submissionViewModel.voteSubmission(
                            adapter.data[position],
                            VoteDirection.UP
                        )
                    } else {
                        observer.loginUser()
                    }
                }
            }

        val itemTouchHelper: ItemTouchHelper = ItemTouchHelper(swipeCallback)
        itemTouchHelper.attachToRecyclerView(submission_recycler_view)

        swipeCallback.swipeAnimation(submission_recycler_view)

        submission_recycler_view.addOnScrollListener(object : RecyclerLoadListener() {
            override fun atBottom() {
                submissionViewModel.getNextPage()
                submission_recycler_view
            }
        })

        submission_swipe_refresh.setOnRefreshListener {
            submissionViewModel.refresh()
        }
    }

    @ExperimentalCoroutinesApi
    fun openSubreddit(name: String) {
        adapter.isMultireddit = false
        submissionViewModel.switchSubreddits(name)
    }

    @ExperimentalCoroutinesApi
    fun openMultireddit(name: String) {
        adapter.isMultireddit = true

        if (name == RedditApp.appContext.getString(R.string.frontpage)) {
            submissionViewModel.switchFrontpage()
        } else {
            submissionViewModel.switchSubreddits(name)
        }

    }

    @ExperimentalCoroutinesApi
    fun openFrontpage() {
        submissionViewModel.switchFrontpage()
    }

    @ExperimentalCoroutinesApi
    fun changeSort(sort: SubredditSort, timePeriod: TimePeriod?) {
        submissionViewModel.changeSort(sort, timePeriod)

    }

    // These methods handle click events on items in the RecyclerView
    override fun onItemClick(submission: Submission, position: Int) {
        observer.openComments(submission.id)
    }

    override fun onItemThumbnailClick(submission: Submission, position: Int) {
        Toast.makeText(
            context,
            "TODO: Open thumbnail/link " + submission.title, Toast.LENGTH_SHORT
        )
            .show()
    }

    override fun onItemLongClick() {
        observer.sort()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is SubmissionFragmentObserver) {
            observer = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement SubmissionFragmentListener"
            )
        }
    }


    interface SubmissionFragmentObserver {
        fun loginUser()
        fun sort()
        fun openComments(id: String)
    }
}
