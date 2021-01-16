package com.hallert.voteforreddit.ui.misc.bottomsheet

import android.content.DialogInterface
import android.view.View
import android.view.WindowManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

open class FullscreenBottomSheet : BaseBottomSheet() {
    override fun setHeight(dialog: DialogInterface) {
        val bottomSheetDialog = dialog as BottomSheetDialog
        val layout =
            bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)

        layout?.let { it ->
            val behaviour = BottomSheetBehavior.from(it)
            val layoutParams = it.layoutParams
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
            it.layoutParams = layoutParams
            behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            behaviour.skipCollapsed = true
        }
    }
}