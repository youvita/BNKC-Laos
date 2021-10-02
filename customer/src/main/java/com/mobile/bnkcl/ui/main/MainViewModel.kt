package com.mobile.bnkcl.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bnkc.library.data.type.Status
import com.bnkc.library.prefer.CredentialSharedPrefer
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.base.BaseViewModel
import com.bnkc.sourcemodule.data.error.ErrorItem
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
    private val _logout: MutableLiveData<Unit> = MutableLiveData()
    val userProfileLiveData: LiveData<ProfileData> = _userProfile
    val logoutLiveData: LiveData<Unit> = _logout

    fun getUserProfile() {
        viewModelScope.launch {
            userRepo.getProfile().onEach { resource ->
                if (resource.status == Status.ERROR) {
                    setError(ErrorItem(null, resource.code, resource.message, null))
                } else {
                    _userProfile.value = resource.data
                }
            }.launchIn(viewModelScope)
        }
    }

    fun logout() {
        viewModelScope.launch {
            userRepo.logout().onEach { resource ->
                if (resource.status == Status.ERROR) {
                    setError(ErrorItem(null, resource.code, resource.message, null))
                } else {
                    _logout.value = resource.data
                }
            }.launchIn(viewModelScope)
        }
    }
}
