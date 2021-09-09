package com.mobile.bnkcl.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bnkc.library.prefer.CredentialSharedPrefer
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.base.BaseViewModel
import com.mobile.bnkcl.data.repository.user.UserRepo
import com.mobile.bnkcl.data.response.user.ProfileData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val userRepo: UserRepo) : BaseViewModel() {

    var userRole = 1
    var isLogin = false

    @Inject
    lateinit var sharedPrefer: CredentialSharedPrefer
    private val _userProfile: MutableLiveData<ProfileData> = MutableLiveData()
    val userProfileLiveData: LiveData<ProfileData> = _userProfile

    fun getUserProfile() {
        if (!sharedPrefer.getPrefer(Constants.KEY_TOKEN).isNullOrEmpty()) {
            viewModelScope.launch {
                userRepo.getProfile().onEach { resource ->
                    _userProfile.value = resource.data
                }.launchIn(viewModelScope)
            }
        }
    }

}
