package com.hallert.voteforreddit.util

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager

class KeyboardUtil {
    companion object {
        // used for opening the keyboard in fragments/activities
        fun openKeyboard(context: Context, view: View) {
            view.requestFocus()
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }

        // used for opening the keyboard in dialogs
        fun openKeyboardDialog(dialog: Dialog, view: View) {
            view.requestFocus()
            dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }

        fun closeKeyboard(context: Context, view: View) {
            view.requestFocus()
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}