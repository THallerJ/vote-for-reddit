package com.hallert.voteforreddit.ui.submission

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hallert.voteforreddit.R
import com.hallert.voteforreddit.ui.views.RecyclerLoadListener
import kotlinx.android.synthetic.main.fragment_submissions.*

class SubmissionsFragment : Fragment() {
    private lateinit var submissionViewModel: SubmissionViewModel
    private lateinit var adapter: SubmissionAdapter
    private var isLoading: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_submissions, container, false)

        submissionViewModel = ViewModelProvider(this).get(SubmissionViewModel::class.java)

        val toolbarTitle = activity?.findViewById<TextView>(R.id.bottom_nav_title)

        if (toolbarTitle != null) {
            toolbarTitle.text = submissionViewModel.getSubredditName()
        }

        adapter = SubmissionAdapter()

        submissionViewModel.submissions.observe(viewLifecycleOwner, Observer { subs ->
            adapter.data = subs
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
        submissionViewModel.getSubmissions()
        submission_recycler_view.addItemDecoration(
            DividerItemDecoration(
                submission_recycler_view.context,
                DividerItemDecoration.VERTICAL
            )
        )

        submission_recycler_view.addOnScrollListener(object : RecyclerLoadListener() {
            override fun atBottom() {
                submissionViewModel.getSubmissions()
            }
        })

        submission_swipe_refresh.setOnRefreshListener {
            submissionViewModel.refresh()
        }
    }
}