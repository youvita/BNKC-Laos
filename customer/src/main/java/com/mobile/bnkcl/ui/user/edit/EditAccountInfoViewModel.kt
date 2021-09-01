package com.mobile.bnkcl.ui.user.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bnkc.library.data.type.Status
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.sourcemodule.base.BaseViewModel
import com.mobile.bnkcl.data.repository.user.EditAccountInfoRepo
import com.mobile.bnkcl.data.response.user.EditAccountInfoData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditAccountInfoViewModel @Inject constructor(private val editAccountInfoRepo: EditAccountInfoRepo) :
    BaseViewModel() {


    private val _editAccountInfo: MutableLiveData<Boolean> = MutableLiveData()
    val editAccountInfoLiveData: LiveData<Boolean> = _editAccountInfo
    fun isUpdate(editAccountInfoData: EditAccountInfoData) {
        viewModelScope.launch {
            editAccountInfoRepo.isUpdate(editAccountInfoData).onEach { resource ->
                if (resource.status == Status.ERROR) {
                    val code = resource.errorCode
                    val title = resource.messageTitle
                    val message = resource.messageDes
                    RxJava.publish(RxEvent.ServerError(code!!, title!!, message!!))
                } else {
                    _editAccountInfo.value = resource.data
                }
            }.launchIn(viewModelScope)
        }
    }
}