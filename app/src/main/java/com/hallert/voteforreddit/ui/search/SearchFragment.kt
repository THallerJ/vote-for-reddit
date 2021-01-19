package com.hallert.voteforreddit.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.viewModels
import com.hallert.voteforreddit.R
import com.hallert.voteforreddit.ui.misc.bottomsheet.FullscreenBottomSheet
import com.hallert.voteforreddit.util.KeyboardUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : FullscreenBottomSheet() {
    private val searchViewModel: SearchViewModel by viewModels()
    private lateinit var searchEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_search, container, false)
        searchEditText = root.findViewById<EditText>(R.id.search_edit_text)

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = searchEditText.text.toString()

                if (query.length >= 2) {
                    searchViewModel.searchReddit(query)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })

        dialog?.let { KeyboardUtil.openKeyboardDialog(it, searchEditText) }

        return root
    }
}