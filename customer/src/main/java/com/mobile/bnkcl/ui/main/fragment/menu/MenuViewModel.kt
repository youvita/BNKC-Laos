package com.mobile.bnkcl.ui.main.fragment.menu

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bnkc.library.prefer.CredentialSharedPrefer
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.base.BaseViewModel
import com.mobile.bnkcl.data.repository.user.UserRepo
import com.mobile.bnkcl.data.response.user.ProfileData
import com.mobile.bnkcl.ui.otp.OtpActivity
import com.mobile.bnkcl.ui.pinview.PinCodeActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(private val userRepo: UserRepo) : BaseViewModel() {

    @Inject
    lateinit var sharedPrefer: CredentialSharedPrefer
    private val _userProfile: MutableLiveData<ProfileData> = MutableLiveData()
    private val _logout: MutableLiveData<Unit> = MutableLiveData()
    val userProfileLiveData: LiveData<ProfileData> = _userProfile
    val logoutLiveData: LiveData<Unit> = _logout

    fun goToLogin() {
        val intent1 = Intent(context, PinCodeActivity::class.java)
        intent1.putExtra("pin_action", "login")
        context.startActivity(intent1)
    }

    fun goToSignUp() {
        val intent1 = Intent(context, OtpActivity::class.java)
        intent1.putExtra("ACTION_TAG", "SIGN_UP")
        context.startActivity(intent1)
    }

    fun getUserProfile() {
        if (!sharedPrefer.getPrefer(Constants.KEY_TOKEN).isNullOrEmpty()) {
            viewModelScope.launch {
                userRepo.getProfile().onEach { resource ->
                    _userProfile.value = resource.data
                }.launchIn(viewModelScope)
            }
        }
    }

    fun logout() {
        if (!sharedPrefer.getPrefer(Constants.KEY_TOKEN).isNullOrEmpty()) {
            viewModelScope.launch {
                userRepo.logout().onEach { resource ->
                    _logout.value = resource.data
                }.launchIn(viewModelScope)
            }
        }
    }

}