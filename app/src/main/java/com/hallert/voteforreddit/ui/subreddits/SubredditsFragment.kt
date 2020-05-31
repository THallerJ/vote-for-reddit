package com.hallert.voteforreddit.ui.subreddits

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.hallert.voteforreddit.R

class SubredditsFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_subreddits, container, false)

        val toolbarTitle = activity?.findViewById<TextView>(R.id.bottom_nav_title)

        if (toolbarTitle != null) {
            toolbarTitle.text = getString(R.string.subreddits)
        }

        return root
    }
}