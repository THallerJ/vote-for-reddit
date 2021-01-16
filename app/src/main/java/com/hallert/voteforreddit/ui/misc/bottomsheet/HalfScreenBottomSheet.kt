package com.hallert.voteforreddit.ui.misc.bottomsheet

import android.content.DialogInterface
import android.content.res.Resources
import com.google.android.material.bottomsheet.BottomSheetDialog

open class HalfScreenBottomSheet : BaseBottomSheet() {
    override fun setHeight(dialog: DialogInterface) {
        val bottomSheetDialog = dialog as BottomSheetDialog
        val height = Resources.getSystem().displayMetrics.heightPixels
        bottomSheetDialog.behavior.peekHeight = height / 2
    }
}
