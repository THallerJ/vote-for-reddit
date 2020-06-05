package com.hallert.voteforreddit.ui.subreddits

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hallert.voteforreddit.R
import kotlinx.android.synthetic.main.fragment_subreddits.*


class SubredditsFragment: BottomSheetDialogFragment() {
    private lateinit var subredditsViewModel: SubredditsViewModel
    private lateinit var adapter: SubredditAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (dialog as? BottomSheetDialog)?.let {
            val width = Resources.getSystem().displayMetrics.heightPixels
            it.behavior.peekHeight = width / 2
        }
        val root = inflater.inflate(R.layout.fragment_subreddits, container, false)


        subredditsViewModel = ViewModelProvider(this).get(SubredditsViewModel::class.java)

        adapter = SubredditAdapter()

        subredditsViewModel.subreddits.observe(viewLifecycleOwner, Observer { subreddits ->
            adapter.data = subreddits
        })

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    fun initRecyclerView() {
        subreddit_recycler_view.layoutManager = LinearLayoutManager(context)
        subreddit_recycler_view.adapter = adapter
        subredditsViewModel.getSubs()
    }
}