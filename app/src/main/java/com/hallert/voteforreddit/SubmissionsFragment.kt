package com.hallert.voteforreddit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_submissions.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import net.dean.jraw.models.Submission

private var adapter = SubmissionsAdapter()

class SubmissionsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_submissions, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData()
        initRecyclerView()
    }

    private fun setData() {
        val result = CoroutineScope(IO).async { fetchData() }
        runBlocking {
            adapter.setData(result.await())
        }
    }

    private suspend fun fetchData(): List<Submission> {
        return RedditApp.accountHelper.reddit.frontPage().build().next()
    }

    private fun initRecyclerView() {
        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.adapter = adapter

    }

}