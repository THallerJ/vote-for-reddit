package com.hallert.voteforreddit.ui.views

import androidx.recyclerview.widget.RecyclerView

open class RecyclerLoadListener : RecyclerView.OnScrollListener() {
    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)

        if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
            atBottom()
        }
    }

    open fun atBottom() {}
}