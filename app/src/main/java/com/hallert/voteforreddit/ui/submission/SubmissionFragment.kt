package com.hallert.voteforreddit.ui.submission

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.hallert.voteforreddit.R
import com.hallert.voteforreddit.RedditApp
import com.hallert.voteforreddit.ui.submission.SubmissionAdapter
import com.squareup.moshi.JsonAdapter
import kotlinx.android.synthetic.main.fragment_submissions.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import net.dean.jraw.JrawUtils
import net.dean.jraw.models.Listing
import net.dean.jraw.models.Submission

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
    }
}