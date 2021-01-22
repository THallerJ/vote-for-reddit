package com.hallert.voteforreddit.ui.subreddits

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.hallert.voteforreddit.R
import com.hallert.voteforreddit.RedditApp
import com.hallert.voteforreddit.ui.misc.bottomsheet.HalfScreenBottomSheet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_subreddits.*
import net.dean.jraw.models.Subreddit
import javax.inject.Inject

@AndroidEntryPoint
class SubredditFragment : HalfScreenBottomSheet(), SubredditClickListener {
    private val subredditViewModel: SubredditViewModel by viewModels()
    private lateinit var adapter: SubredditAdapter
    private lateinit var observer: SubredditFragmentObserver

    @Inject
    lateinit var userManager: com.hallert.voteforreddit.user.UserManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_subreddits, container, false)

        adapter = SubredditAdapter(this)

        subredditViewModel.subreddits.observe(viewLifecycleOwner, Observer { subreddits ->
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

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    fun initRecyclerView() {
        subreddit_recycler_view.layoutManager = LinearLayoutManager(context)
        subreddit_recycler_view.adapter = adapter
        subredditViewModel.updateSubreddits()
    }

    // This method control what happens when clicking items in the RecyclerView
    override fun onItemClick(subreddit: Subreddit, position: Int) {
        observer.onSubredditSelected(subreddit.name)
        this.dismiss()
    }

    private fun setListeners() {
        subreddit_all.setOnClickListener {
            observer.onMultiredditSelected(RedditApp.appContext.getString(R.string.all))
            this.dismiss()
        }

        subreddit_popular.setOnClickListener {
            observer.onMultiredditSelected(RedditApp.appContext.getString(R.string.popular))
            this.dismiss()
        }

        subreddit_frontpage.setOnClickListener {
            observer.onMultiredditSelected(RedditApp.appContext.getString(R.string.frontpage))
            this.dismiss()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is SubredditFragmentObserver) {
            observer = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement SubredditFragmentObserver"
            )
        }
    }


    interface SubredditFragmentObserver {
        fun onSubredditSelected(selection: String)
        fun onMultiredditSelected(selection: String)
    }
}