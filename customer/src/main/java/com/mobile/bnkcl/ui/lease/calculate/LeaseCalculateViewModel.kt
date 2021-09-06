package com.mobile.bnkcl.ui.lease.calculate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bnkc.library.data.type.Status
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.sourcemodule.base.BaseViewModel
import com.mobile.bnkcl.data.api.lease.LeaseApi
import com.mobile.bnkcl.data.repository.lease.LeaseRepo
import com.mobile.bnkcl.data.request.auth.LoginRequest
import com.mobile.bnkcl.data.request.auth.LoginRequestNoAuth
import com.mobile.bnkcl.data.request.lease.calcculate.LeaseCalculateReq
import com.mobile.bnkcl.data.response.auth.LoginResponse
import com.mobile.bnkcl.data.response.lease.calculate.LeaseCalResponse
import com.mobile.bnkcl.data.response.office.AreaDataResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaseCalculateViewModel @Inject constructor(private val leaseRepo: LeaseRepo) :BaseViewModel() {

    private val _leaseCalLiveData: MutableLiveData<LeaseCalResponse> = MutableLiveData()
    val leaseCalLiveData: LiveData<LeaseCalResponse> = _leaseCalLiveData
    var leaseCalculateReq : LeaseCalculateReq = LeaseCalculateReq()
    fun calculateLease(){
        viewModelScope.launch {
            leaseRepo.getLeaseCalculate(leaseCalculateReq).onEach { resource ->
                if (resource.status == Status.ERROR) {
                    val code = resource.errorCode
                    val title = resource.messageTitle
                    val message = resource.messageDes
                    RxJava.publish(RxEvent.ServerError(code!!, title!!, message!!))
                } else {
                    _leaseCalLiveData.value = resource.data
                }
            }.launchIn(viewModelScope)
        }
    }

    private var data: ArrayList<String>? = ArrayList()
    fun setUpData() : ArrayList<String> {
        return arrayListOf(
            "12 Months",
            "18 Months",
            "24 Months",
            "36 Months",
            "48 Months",
            "60 Months",
            "72 Months",
        )
    }

}