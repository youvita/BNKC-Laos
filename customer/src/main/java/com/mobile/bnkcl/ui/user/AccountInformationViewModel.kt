package com.mobile.bnkcl.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bnkc.library.prefer.CredentialSharedPrefer
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.base.BaseViewModel
import com.mobile.bnkcl.data.repository.code.CodesRepo
import com.mobile.bnkcl.data.repository.user.UserRepo
import com.mobile.bnkcl.data.response.code.CodesResponse
import com.mobile.bnkcl.data.response.user.ProfileData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountInformationViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val codesRepo: CodesRepo
) :
    BaseViewModel() {

    @Inject
    lateinit var sharedPrefer: CredentialSharedPrefer
    private val _accountInformation: MutableLiveData<ProfileData> = MutableLiveData()
    private val _jobCodesLiveData: MutableLiveData<CodesResponse> = MutableLiveData()
    private val _logout: MutableLiveData<Unit> = MutableLiveData()
    val accountInformationLiveData: LiveData<ProfileData> = _accountInformation
    val logoutLiveData: LiveData<Unit> = _logout
    val jobTypesLiveData: LiveData<CodesResponse> = _jobCodesLiveData

    fun getAccountInformation() {
        if (!sharedPrefer.getPrefer(Constants.KEY_TOKEN).isNullOrEmpty()) {
            viewModelScope.launch {
                userRepo.getProfile().onEach { resource ->
                    _accountInformation.value = resource.data
                }.launchIn(viewModelScope)
            }
        }
    }

    fun getJobTypeCodes() {
        if (!sharedPrefer.getPrefer(Constants.KEY_TOKEN).isNullOrEmpty()) {
            viewModelScope.launch {
                codesRepo.getCodes("JOB_TYPE").onEach { resource ->
                    _jobCodesLiveData.value = resource.data
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