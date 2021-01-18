package com.hallert.voteforreddit.ui.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import com.hallert.voteforreddit.R
import com.hallert.voteforreddit.ui.misc.bottomsheet.FullscreenBottomSheet
import com.hallert.voteforreddit.util.KeyboardUtil
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : FullscreenBottomSheet() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_search, container, false)
        val text = root.findViewById<EditText>(R.id.search_edit_text)

        dialog?.let { KeyboardUtil.openKeyboardDialog(it, text) }

        return root
    }
}