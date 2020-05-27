package com.hallert.voteforreddit.ui.submission

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hallert.voteforreddit.R
import kotlinx.android.synthetic.main.fragment_submissions.*

private var adapter = SubmissionAdapter()

// TODO: Create my own submission class that can be created from retrieved cache data
class SubmissionsFragment : Fragment() {

    private lateinit var submissionViewModel: SubmissionViewModel
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
        submissionViewModel.submissions.observe(viewLifecycleOwner, Observer { subs ->
            adapter.data = subs
        })

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.adapter = adapter
        submissionViewModel.getSubmissions()
        recycler_view.addItemDecoration(DividerItemDecoration(recycler_view.context, DividerItemDecoration.VERTICAL))

        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    submissionViewModel.getSubmissions()
                }
            }
        })
    }
}