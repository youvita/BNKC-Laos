package com.mobile.bnkcl.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bnkc.library.data.type.Status
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.sourcemodule.base.BaseViewModel
import com.mobile.bnkcl.data.repository.user.UserRepo
import com.mobile.bnkcl.data.response.user.ProfileData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountInformationViewModel @Inject constructor(private val userRepo: UserRepo) :
    BaseViewModel() {
    private val _accountInformation: MutableLiveData<ProfileData> = MutableLiveData()
    val accountInformationLiveData: LiveData<ProfileData> = _accountInformation
    fun getAccountInformation() {
        viewModelScope.launch {
            userRepo.getProfile().onEach { resource ->
                if (resource.status == Status.ERROR) {
                    val code = resource.errorCode
                    val title = resource.messageTitle
                    val message = resource.messageDes
                    RxJava.publish(RxEvent.ServerError(code!!, title!!, message!!))
                } else {
                    _accountInformation.value = resource.data
                }
            }.launchIn(viewModelScope)
        }
    }
}