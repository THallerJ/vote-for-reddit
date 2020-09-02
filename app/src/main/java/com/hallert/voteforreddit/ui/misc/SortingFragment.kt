package com.hallert.voteforreddit.ui.misc

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hallert.voteforreddit.R
import kotlinx.android.synthetic.main.fragment_sorting.*
import net.dean.jraw.models.SubredditSort
import net.dean.jraw.models.TimePeriod

class SortingFragment : BottomSheetDialogFragment(), View.OnClickListener {
    private lateinit var observer: SortingFragmentObserver
    private lateinit var sort: SubredditSort

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_sorting, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hot_text_view.setOnClickListener(this)
        new_text_view.setOnClickListener(this)
        rising_text_view.setOnClickListener(this)
        top_text_view.setOnClickListener(this)
        controversial_text_view.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            hot_text_view -> {
                observer.sortSelected(SubredditSort.HOT)
                this.dismiss()
            }
            new_text_view -> {
                observer.sortSelected(SubredditSort.NEW)
                this.dismiss()
            }
            rising_text_view -> {
                observer.sortSelected(SubredditSort.RISING)
                this.dismiss()
            }
            top_text_view -> {
                // sort = SubredditSort.TOP
                observer.sortSelected(SubredditSort.TOP)
                this.dismiss()
            }
            controversial_text_view -> {
                // sort = SubredditSort.CONTROVERSIAL
                observer.sortSelected(SubredditSort.CONTROVERSIAL)
                this.dismiss()
            }
        }
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
        fun sortSelected(sort: SubredditSort)
        fun sortTimeSelected(sort: SubredditSort, timePeriod: TimePeriod)
    }
}