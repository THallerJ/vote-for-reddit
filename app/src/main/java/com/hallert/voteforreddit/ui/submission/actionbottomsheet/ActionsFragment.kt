package com.hallert.voteforreddit.ui.submission.actionbottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.hallert.voteforreddit.R
import com.hallert.voteforreddit.ui.misc.bottomsheet.ExpandedBottomSheet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_actions_menu.*

@AndroidEntryPoint
class ActionsFragment : ExpandedBottomSheet() {
    private val actionsViewModel: ActionsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_actions_menu, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        toggle_theme_button.setOnClickListener {
            actionsViewModel.toggleDarkMode()
            this.dismiss()
        }
    }
}