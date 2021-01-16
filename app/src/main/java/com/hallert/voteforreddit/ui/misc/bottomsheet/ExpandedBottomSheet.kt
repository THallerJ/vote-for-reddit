package com.hallert.voteforreddit.ui.misc.bottomsheet

import android.content.DialogInterface
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

open class ExpandedBottomSheet : BaseBottomSheet() {
    override fun setHeight(dialog: DialogInterface) {
        val bottomSheetDialog = dialog as BottomSheetDialog
        bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }
}