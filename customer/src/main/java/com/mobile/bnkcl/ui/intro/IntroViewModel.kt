package com.mobile.bnkcl.ui.intro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bnkc.library.data.type.Status
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.mobile.bnkcl.data.MGData
import com.mobile.bnkcl.data.repository.intro.MGRepo
import com.bnkc.sourcemodule.base.BaseViewModel
import com.mobile.bnkcl.data.repository.user.UserRepo
import com.mobile.bnkcl.data.response.user.ProfileData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(private val mgRepo: MGRepo, private val userRepo: UserRepo) : BaseViewModel() {

    private val _mgData: MutableLiveData<MGData> = MutableLiveData()
    val mgData: LiveData<MGData> get() = _mgData

    fun getMGData() {
        viewModelScope.launch {
            mgRepo.getMGData().onEach { resource ->
                // catch error
                if (resource.status == Status.ERROR) {
                    val code    = resource.errorCode
                    val title   = resource.messageTitle
                    val message = resource.messageDes
                    RxJava.publish(RxEvent.ServerError(code!!, title!!, message!!))
                } else {
                    _mgData.value = resource.data
                }
            }.launchIn(viewModelScope)
        }
    }

    private val _profile: MutableLiveData<ProfileData> = MutableLiveData()
    val profile: LiveData<ProfileData> get() = _profile

    fun getProfile() {
        viewModelScope.launch {
            userRepo.getProfile().onEach { resource ->
                // catch error
                if (resource.status == Status.ERROR) {
                    val code    = resource.errorCode
                    val title   = resource.messageTitle
                    val message = resource.messageDes
                    RxJava.publish(RxEvent.ServerError(code!!, title!!, message!!))
                } else {
                    _profile.value = resource.data
                }
            }.launchIn(viewModelScope)
        }
    }

}