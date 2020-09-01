package com.hallert.voteforreddit.ui.subreddits

import android.content.Context
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
import net.dean.jraw.models.Subreddit
import javax.inject.Inject

@AndroidEntryPoint
class SubredditsFragment : BottomSheetDialogFragment(), SubredditClickListener {
    private val subredditsViewModel: SubredditsViewModel by viewModels()
    private lateinit var adapter: SubredditAdapter
    private lateinit var listener: SubredditFragmentListener

    @Inject
    lateinit var userManager: com.hallert.voteforreddit.user.UserManager

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

        adapter = SubredditAdapter(this)

        subredditsViewModel.subreddits.observe(viewLifecycleOwner, Observer { subreddits ->
            adapter.data = subreddits

            subreddit_frontpage.visibility = View.VISIBLE
            subreddit_popular.visibility = View.VISIBLE
            subreddit_all.visibility = View.VISIBLE
            subreddits_title.visibility = View.VISIBLE

            if (userManager.isUserless()) {
                subreddits_divider_line.visibility = View.GONE
            } else {
                subreddits_divider_line.visibility = View.VISIBLE
            }

            setListeners()
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

    // This method control what happens when clicking items in the RecyclerView
    override fun onItemClick(subreddit: Subreddit, position: Int) {
        listener.onSubredditSelected(subreddit.name)
        this.dismiss()
    }

    private fun setListeners() {
        subreddit_all.setOnClickListener {
            listener.onSubredditSelected("All")
            this.dismiss()
        }

        subreddit_popular.setOnClickListener {
            listener.onSubredditSelected("Popular")
            this.dismiss()
        }

        subreddit_frontpage.setOnClickListener {
            listener.onFrontPageSelected()
            this.dismiss()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is SubredditFragmentListener) {
            listener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement SubredditFragmentListener"
            )
        }
    }


    interface SubredditFragmentListener {
        fun onSubredditSelected(selection: String)
        fun onFrontPageSelected()
    }
}