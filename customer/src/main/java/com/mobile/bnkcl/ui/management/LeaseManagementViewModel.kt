package com.mobile.bnkcl.ui.management

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bnkc.library.prefer.CredentialSharedPrefer
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.base.BaseViewModel
import com.mobile.bnkcl.data.repository.lease.LeaseRepo
import com.mobile.bnkcl.data.response.lease.LeaseInfoResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaseManagementViewModel @Inject constructor(private val leaseRepo: LeaseRepo) :
    BaseViewModel() {

    @Inject
    lateinit var sharedPrefer: CredentialSharedPrefer
    private val _lease: MutableLiveData<LeaseInfoResponse> = MutableLiveData()
    val leaseLiveData: LiveData<LeaseInfoResponse> = _lease

    fun getLeaseInfo(contractNo: String) {
        if (!sharedPrefer.getPrefer(Constants.KEY_TOKEN).isNullOrEmpty()) {
            viewModelScope.launch {
                leaseRepo.getLeaseInfo(contractNo).onEach { resource ->
                    _lease.value = resource.data
                }.launchIn(viewModelScope)
            }
        }
    }
}