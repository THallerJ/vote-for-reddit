package com.hallert.voteforreddit.ui.search

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.hallert.voteforreddit.R
import com.hallert.voteforreddit.ui.misc.bottomsheet.FullscreenBottomSheet
import com.hallert.voteforreddit.ui.subreddits.SubredditAdapter
import com.hallert.voteforreddit.ui.subreddits.SubredditClickListener
import com.hallert.voteforreddit.util.KeyboardUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_subreddits.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import net.dean.jraw.models.Subreddit

@AndroidEntryPoint
class SearchFragment : FullscreenBottomSheet(), SubredditClickListener {
    private val searchViewModel: SearchViewModel by viewModels()
    private lateinit var searchEditText: EditText
    private lateinit var subredditAdapter: SubredditAdapter
    private lateinit var observer: SearchFragmentObserver

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_search, container, false)
        searchEditText = root.findViewById<EditText>(R.id.search_edit_text)

        subredditAdapter = SubredditAdapter(this, true)

        searchViewModel.subreddits.observe(
            viewLifecycleOwner,
            Observer { subreddits ->
                subredditAdapter.data = subreddits
            })

        setupEditText()

        dialog?.let { KeyboardUtil.openKeyboardDialog(it, searchEditText) }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSubredditRecyclerView()
    }

    private fun initSubredditRecyclerView() {
        subreddit_search_recycler_view.layoutManager = LinearLayoutManager(context)
        subreddit_search_recycler_view.adapter = subredditAdapter
    }

    @ExperimentalCoroutinesApi
    private fun setupEditText() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = searchEditText.text.toString()
                searchViewModel.searchReddit(query)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })

        searchEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchViewModel.clearNonQuery()
                context?.let { KeyboardUtil.closeKeyboard(it, searchEditText) }
                this.dismiss()

                return@setOnEditorActionListener true
            }

            false
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        searchViewModel.clearNonQuery()
        super.onDismiss(dialog)
    }

    override fun onItemClick(subreddit: Subreddit, position: Int) {
        observer.onSubredditSearchSelected(subreddit.name)
        searchViewModel.clearNonQuery()
        this.dismiss()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is SearchFragmentObserver) {
            observer = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement SubredditSearchObserver"
            )
        }
    }


    interface SearchFragmentObserver {
        fun onSubredditSearchSelected(selection: String)
    }
}

