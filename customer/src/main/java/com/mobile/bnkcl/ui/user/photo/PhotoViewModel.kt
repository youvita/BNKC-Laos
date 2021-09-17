package com.mobile.bnkcl.ui.user.photo

import androidx.lifecycle.MutableLiveData
import com.bnkc.sourcemodule.base.BaseViewModel
import com.bnkc.sourcemodule.data.SettingMenu
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Useful management photo view
 */
@HiltViewModel
class PhotoViewModel @Inject constructor() : BaseViewModel() {

    /**
     * observe photo setting, camera & gallery
     */
    val photoSettingLiveData: MutableLiveData<SettingMenu> = MutableLiveData()

    fun setSettingMenu(settingMenu: SettingMenu?) {
        photoSettingLiveData.value = settingMenu
    }

}