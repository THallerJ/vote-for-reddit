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
import android.widget.*
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
import net.dean.jraw.models.SearchSort
import net.dean.jraw.models.Subreddit
import net.dean.jraw.models.TimePeriod

private const val SEARCH_TITLE_BUNDLE = "search_title_bundle"
private const val SEARCH_FLAG_BUNDLE = "search_flag_bundle"

@AndroidEntryPoint
class SearchFragment : FullscreenBottomSheet(), SubredditClickListener {
    private val searchViewModel: SearchViewModel by viewModels()

    private lateinit var searchEditText: EditText
    private lateinit var subCheckbox: CheckBox
    private lateinit var timePeriodSpinner: Spinner
    private lateinit var sortSpinner: Spinner

    private lateinit var subredditAdapter: SubredditAdapter
    private lateinit var observer: SearchFragmentObserver

    private lateinit var subredditTitle: String
    private var searchFlag: Boolean = false

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_search, container, false)

        subredditTitle = arguments?.getString(SEARCH_TITLE_BUNDLE).toString()
        searchFlag = arguments?.getBoolean(SEARCH_FLAG_BUNDLE) == true

        searchEditText = root.findViewById(R.id.search_edit_text)
        timePeriodSpinner = root.findViewById(R.id.time_period_spinner)
        sortSpinner = root.findViewById(R.id.sort_spinner)
        subCheckbox = root.findViewById(R.id.search_checkbox)

        subredditAdapter = SubredditAdapter(this, true)

        if (isInSubreddit()) {
            subCheckbox.text =
                resources.getString(R.string.search_check_text).let {
                    String.format(
                        it, subredditTitle
                    )
                }

            setSearchHint(true)

            subCheckbox.isChecked = true
            subCheckbox.visibility = View.VISIBLE
        } else {
            subCheckbox.visibility = View.GONE
        }

        subCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            setSearchHint(isChecked)
        }

        searchViewModel.subreddits.observe(
            viewLifecycleOwner,
            Observer { subreddits ->
                subredditAdapter.data = subreddits
                search_divider_line.visibility = View.VISIBLE
            })

        setupSpinners()
        setupEditText()

        dialog?.let { KeyboardUtil.openKeyboardDialog(it, searchEditText) }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSubredditRecyclerView()
    }

    private fun isInSubreddit(): Boolean {
        return subredditTitle != resources.getString(R.string.frontpage) && subredditTitle != resources.getString(
            R.string.all
        ) && subredditTitle != resources.getString(R.string.popular)
    }

    private fun setSearchHint(isSubreddit: Boolean) {
        if (isSubreddit) {
            searchEditText.hint =
                resources.getString(R.string.search_hint_subreddit).let {
                    String.format(
                        it, subredditTitle
                    )
                }
        } else {
            searchEditText.hint =
                resources.getString(R.string.search_hint)
        }
    }


    private fun initSubredditRecyclerView() {
        subreddit_search_recycler_view.layoutManager = LinearLayoutManager(context)
        subreddit_search_recycler_view.adapter = subredditAdapter
    }

    @ExperimentalCoroutinesApi
    private fun setupEditText() {
        if (isInSubreddit() && searchFlag) {
            searchEditText.setText(subredditTitle)
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (searchEditText.text.isNotBlank()) {
                    val query = searchEditText.text.toString()
                    searchViewModel.searchReddit(query)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val timePeriod = when (timePeriodSpinner.selectedItem) {
                    resources.getString(R.string.all_time) -> TimePeriod.ALL
                    resources.getString(R.string.year) -> TimePeriod.YEAR
                    resources.getString(R.string.month) -> TimePeriod.MONTH
                    resources.getString(R.string.week) -> TimePeriod.WEEK
                    resources.getString(R.string.day) -> TimePeriod.DAY
                    else -> TimePeriod.ALL
                }

                val sort = when (sortSpinner.selectedItem) {
                    resources.getString(R.string.relevance) -> SearchSort.RELEVANCE
                    resources.getString(R.string.hot) -> SearchSort.HOT
                    resources.getString(R.string.top) -> SearchSort.TOP
                    resources.getString(R.string.newSort) -> SearchSort.NEW
                    resources.getString(R.string.comments) -> SearchSort.COMMENTS
                    else -> SearchSort.RELEVANCE
                }

                val subreddit = if (subCheckbox.isChecked) subredditTitle else null

                observer.onSearch(searchEditText.text.toString(), timePeriod, sort, subreddit)

                searchViewModel.clearNonQuery()
                context?.let { KeyboardUtil.closeKeyboard(it, searchEditText) }
                this.dismiss()

                return@setOnEditorActionListener true
            }

            false
        }
    }

    private fun setupSpinners() {
        context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.search_time_period,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(R.layout.modified_spinner_item)
                timePeriodSpinner.adapter = adapter
            }
        }

        context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.search_sort,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(R.layout.modified_spinner_item)
                sortSpinner.adapter = adapter
            }
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
        fun onSearch(query: String, timePeriod: TimePeriod, sort: SearchSort, subreddit: String?)
    }
}

