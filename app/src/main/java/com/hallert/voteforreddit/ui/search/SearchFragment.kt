package com.hallert.voteforreddit.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hallert.voteforreddit.R
import com.hallert.voteforreddit.ui.misc.bottomsheet.FullscreenBottomSheet

class SearchFragment : FullscreenBottomSheet() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_search, container, false)
        return root
    }
}