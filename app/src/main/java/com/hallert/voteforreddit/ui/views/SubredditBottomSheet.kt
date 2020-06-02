package com.hallert.voteforreddit.ui.views

import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.hardware.display.DisplayManager
import android.os.Bundle
import android.view.ContextMenu
import android.view.Display
import android.view.View
import android.view.WindowManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hallert.voteforreddit.R


class SubredditBottomSheet : BottomSheetDialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog =
            super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        val view =
            View.inflate(context, R.layout.fragment_subreddits, null)

        dialog.setContentView(view)
        val bottomSheetBehavior =
            BottomSheetBehavior.from(view.parent as View)

        val width = Resources.getSystem().displayMetrics.heightPixels
        bottomSheetBehavior.peekHeight = width / 2

        return dialog
    }
}