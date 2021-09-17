package com.bnkc.sourcemodule.dialog

import android.os.Bundle
import android.view.View
import com.bnkc.sourcemodule.R
import com.bnkc.sourcemodule.base.BaseBottomSheetDialog
import com.bnkc.sourcemodule.data.SettingMenu
import com.bnkc.sourcemodule.databinding.ActivityPhotoSettingBinding

class PhotoSettingMenu: BaseBottomSheetDialog<ActivityPhotoSettingBinding>() {

    private var menuListener: ((settingMenu: SettingMenu?) -> Unit)? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_photo_setting
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.gallery.setOnClickListener {
            menuListener?.invoke(SettingMenu.Gallery)
            dismiss()
        }

        binding.camera.setOnClickListener {
            menuListener?.invoke(SettingMenu.Camera)
            dismiss()
        }
    }

    fun onMenuSelected(menuListener: ((settingMenu: SettingMenu?) -> Unit)) = apply { this.menuListener = menuListener }
}

