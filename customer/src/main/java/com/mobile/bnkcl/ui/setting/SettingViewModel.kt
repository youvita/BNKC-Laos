package com.mobile.bnkcl.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bnkc.library.data.type.Status
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.sourcemodule.base.BaseViewModel
import com.mobile.bnkcl.data.repository.user.UserRepo
import com.mobile.bnkcl.data.response.user.SettingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(private val userRepo: UserRepo) : BaseViewModel() {

    private val _updateUserSetting: MutableLiveData<SettingData> = MutableLiveData()
    val userSettingLiveData: LiveData<SettingData> = _updateUserSetting
    private var settingData: SettingData = SettingData("Y")
    fun updateUserSetting() {
        viewModelScope.launch {
            userRepo.updateUserSetting(settingData).onEach { resource ->
                if (resource.status == Status.ERROR) {
                    val code = resource.errorCode
                    val title = resource.messageTitle
                    val message = resource.messageDes
                    RxJava.publish(RxEvent.ServerError(code!!, title!!, message!!))
                } else {
                    _updateUserSetting.value = resource.data
                }
            }.launchIn(viewModelScope)
        }
    }
}