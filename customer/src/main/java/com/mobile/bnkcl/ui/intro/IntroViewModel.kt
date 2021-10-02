package com.mobile.bnkcl.ui.intro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bnkc.library.data.type.Status
import com.bnkc.sourcemodule.base.BaseViewModel
import com.bnkc.sourcemodule.data.error.ErrorItem
import com.mobile.bnkcl.data.repository.intro.MGRepo
import com.mobile.bnkcl.data.repository.user.UserRepo
import com.mobile.bnkcl.data.response.common.MGDataResponse
import com.mobile.bnkcl.data.response.user.ProfileData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(
    private val mgRepo: MGRepo,
    private val userRepo: UserRepo
) : BaseViewModel() {

    private val _mgDataResponse: MutableLiveData<MGDataResponse> = MutableLiveData()
    val mgDataResponse: LiveData<MGDataResponse> get() = _mgDataResponse

    fun getMGData() {
        viewModelScope.launch {
            mgRepo.getMGData().onEach { resource ->
                if (resource.status == Status.ERROR) {
                    setError(ErrorItem(null, resource.code, resource.message, null))
                } else {
                    _mgDataResponse.value = resource.data
                }
            }.launchIn(viewModelScope)
        }
    }

    private val _profile: MutableLiveData<ProfileData> = MutableLiveData()
    val profile: LiveData<ProfileData> get() = _profile

    fun getProfile() {
        viewModelScope.launch {
            userRepo.getProfile().onEach { resource ->
                _profile.value = resource.data
            }.launchIn(viewModelScope)
        }
    }

}