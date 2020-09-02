package com.hallert.voteforreddit.ui.misc

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hallert.voteforreddit.R

class SortingFragment : BottomSheetDialogFragment() {
    private lateinit var observer: SortingFragmentObserver

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_sorting, container, false)

        return root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is SortingFragmentObserver) {
            observer = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement SortingFragmentObserver"
            )
        }
    }

    interface SortingFragmentObserver {

    }
}