package com.mobile.bnkcl.ui.main.fragment.menu

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bnkc.library.data.type.Status
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.sourcemodule.base.BaseViewModel
import com.mobile.bnkcl.data.repository.user.UserRepo
import com.mobile.bnkcl.data.response.user.ProfileData
import com.mobile.bnkcl.data.response.user.SettingData
import com.mobile.bnkcl.ui.otp.OtpActivity
import com.mobile.bnkcl.ui.pinview.PinCodeActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(private val userRepo: UserRepo) : BaseViewModel(){

    fun goToLogin(){
        val intent1 = Intent(context, PinCodeActivity::class.java)
        intent1.putExtra("pin_action", "login")
        context.startActivity(intent1)
    }

    fun goToSignUp(){
        val intent1 = Intent(context, OtpActivity::class.java)
        intent1.putExtra("ACTION_TAG", "SIGN_UP")
        context.startActivity(intent1)
    }

    private val _userProfile: MutableLiveData<ProfileData> = MutableLiveData()
    val userProfileLiveData: LiveData<ProfileData> = _userProfile
    fun getUserProfile() {
        viewModelScope.launch {
            userRepo.getProfile().onEach { resource ->
                if (resource.status == Status.ERROR) {
                    val code = resource.errorCode
                    val title = resource.messageTitle
                    val message = resource.messageDes
                    RxJava.publish(RxEvent.ServerError(code!!, title!!, message!!))
                } else {
                    _userProfile.value = resource.data
                }
            }.launchIn(viewModelScope)
        }
    }

}