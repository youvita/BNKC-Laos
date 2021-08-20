package com.mobile.bnkcl.ui.management

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bnkc.library.data.type.Status
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.sourcemodule.base.BaseViewModel
import com.mobile.bnkcl.data.repository.lease.LeaseRepo
import com.mobile.bnkcl.data.response.lease.LeaseInfoResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaseManagementViewModel @Inject constructor(private val leaseRepo: LeaseRepo) : BaseViewModel(){

    private val _lease: MutableLiveData<LeaseInfoResponse> = MutableLiveData()
    val leaseLiveData: LiveData<LeaseInfoResponse> = _lease
    fun getLeaseInfo(contractNo: String){
        viewModelScope.launch {
            leaseRepo.getLeaseInfo(contractNo).onEach { resource ->
                if (resource.status == Status.ERROR) {
                    val code = resource.errorCode
                    val title = resource.messageTitle
                    val message = resource.messageDes
                    RxJava.publish(RxEvent.ServerError(code!!, title!!, message!!))
                } else {
                    _lease.value = resource.data
                }
            }.launchIn(viewModelScope)
        }
    }
}