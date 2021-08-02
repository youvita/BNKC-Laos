package com.mobile.bnkcl.com.alert

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.mobile.bnkcl.R

class DlgWindow(context: Context?, layout: View?) :
    Dialog(context!!, R.style.Theme_AppCompat_Dialog_Alert) {
    init {
        try {
            if (super.getWindow() != null) {
                super.getWindow()!!.requestFeature(Window.FEATURE_NO_TITLE)
                super.getWindow()!!.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
                )
                super.setContentView(layout!!)
                super.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}