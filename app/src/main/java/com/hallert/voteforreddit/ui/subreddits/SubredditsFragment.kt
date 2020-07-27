package com.hallert.voteforreddit.ui.subreddits

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hallert.voteforreddit.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_subreddits.*

@AndroidEntryPoint
class SubredditsFragment : BottomSheetDialogFragment() {
    private val subredditsViewModel: SubredditsViewModel by viewModels()
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

        adapter = SubredditAdapter()

        subredditsViewModel.subreddits.observe(viewLifecycleOwner, Observer { subreddits ->
            adapter.data = subreddits

            subreddit_frontpage.visibility = View.VISIBLE
            subreddit_popular.visibility = View.VISIBLE
            subreddit_all.visibility = View.VISIBLE
            subreddits_title.visibility = View.VISIBLE
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
        subredditsViewModel.updateSubreddits()
    }
}