package com.hallert.voteforreddit.ui.misc

import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
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
        (dialog as? BottomSheetDialog)?.let {
            val width = Resources.getSystem().displayMetrics.heightPixels
            it.behavior.peekHeight = width / 2
        }
        val root = inflater.inflate(R.layout.fragment_sorting, container, false)
        return root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setOnShowListener {
                (this@SortingFragment.dialog as BottomSheetDialog).behavior.setState(
                    BottomSheetBehavior.STATE_EXPANDED
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hot_text_view.setOnClickListener(this)
        new_text_view.setOnClickListener(this)
        rising_text_view.setOnClickListener(this)
        top_text_view.setOnClickListener(this)
        controversial_text_view.setOnClickListener(this)
        hour_text_view.setOnClickListener(this)
        day_text_view.setOnClickListener(this)
        week_text_view.setOnClickListener(this)
        month_text_view.setOnClickListener(this)
        year_text_view.setOnClickListener(this)
        all_text_view.setOnClickListener(this)
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
                sort = SubredditSort.TOP
                sorting_linear_layout.visibility = View.GONE
                time_linear_layout.visibility = View.VISIBLE
            }
            controversial_text_view -> {
                sort = SubredditSort.CONTROVERSIAL
                sorting_linear_layout.visibility = View.GONE
                time_linear_layout.visibility = View.VISIBLE
            }
            hour_text_view -> {
                observer.sortSelected(sort, TimePeriod.HOUR)
                this.dismiss()
            }
            day_text_view -> {
                observer.sortSelected(sort, TimePeriod.WEEK)
                this.dismiss()
            }
            week_text_view -> {
                observer.sortSelected(sort, TimePeriod.WEEK)
                this.dismiss()
            }
            month_text_view -> {
                observer.sortSelected(sort, TimePeriod.MONTH)
                this.dismiss()
            }
            year_text_view -> {
                observer.sortSelected(sort, TimePeriod.YEAR)
                this.dismiss()
            }
            all_text_view -> {
                observer.sortSelected(sort, TimePeriod.ALL)
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
        fun sortSelected(sort: SubredditSort, timePeriod: TimePeriod)
    }
}