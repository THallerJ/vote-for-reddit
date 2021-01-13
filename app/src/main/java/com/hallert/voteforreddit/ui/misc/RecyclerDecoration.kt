package com.hallert.voteforreddit.ui.misc

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.hallert.voteforreddit.R

class RecyclerDecoration {
    fun addDividerLine(context: Context?, recyclerView: RecyclerView) {
        val divider = DividerItemDecoration(
            context,
            DividerItemDecoration.VERTICAL
        )

        divider.setDrawable(context?.let {
            ContextCompat.getDrawable(
                it,
                R.drawable.divider_line
            )
        }!!)
        recyclerView.addItemDecoration(divider)
    }
}