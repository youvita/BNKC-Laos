package com.mobile.bnkcl.ui.user.photo

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityPhotoSettingBinding

class PhotoSettingMenu(context: Context?, viewModel: PhotoViewModel?) {
    private var mDialog: Dialog? = null

    /**
     * dismiss dialog
     */
    fun onDismiss() {
        mDialog!!.dismiss()
    }

    init {
        try {
            val mSettingBinding: ActivityPhotoSettingBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.activity_photo_setting,
                null,
                false
            )
            mSettingBinding.photoSetting = viewModel
            mDialog = Dialog(context!!, R.style.DialogTheme)
            mDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            mDialog!!.setContentView(mSettingBinding.getRoot())
            mDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            mDialog!!.window!!.setGravity(Gravity.BOTTOM)
            mDialog!!.window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            mDialog!!.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}