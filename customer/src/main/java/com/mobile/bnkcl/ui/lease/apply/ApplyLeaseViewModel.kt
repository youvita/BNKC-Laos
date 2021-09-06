package com.mobile.bnkcl.ui.lease.apply

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bnkc.library.data.type.Status
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.sourcemodule.base.BaseViewModel
import com.mobile.bnkcl.data.repository.lease.LeaseRepo
import com.mobile.bnkcl.data.request.lease.apply.ApplyLeaseRequest
import com.mobile.bnkcl.data.request.lease.calcculate.LeaseCalculateReq
import com.mobile.bnkcl.data.response.lease.apply.ApplyLeaseResponse
import com.mobile.bnkcl.data.response.lease.calculate.LeaseCalResponse
import com.mobile.bnkcl.data.response.office.AreaDataResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApplyLeaseViewModel @Inject constructor(private val leaseRepo: LeaseRepo) : BaseViewModel() {

    private val _actionMuLiveData = MutableLiveData<String>()
    val actionLiveData = _actionMuLiveData

    private val _applyLeaseLiveData: MutableLiveData<ApplyLeaseResponse> = MutableLiveData()
    val applyLeaseLiveData: LiveData<ApplyLeaseResponse> = _applyLeaseLiveData
    var applyLeaseRequest : ApplyLeaseRequest? = null
    fun applyLease(){
        viewModelScope.launch {
            leaseRepo.applyLease(applyLeaseRequest!!).onEach { resource ->
                if (resource.status == Status.ERROR) {
                    val code = resource.errorCode
                    val title = resource.messageTitle
                    val message = resource.messageDes
                    RxJava.publish(RxEvent.ServerError(code!!, title!!, message!!))
                } else {
                    _applyLeaseLiveData.value = resource.data
                }
            }.launchIn(viewModelScope)
        }
    }

    fun applyLeaseClicked(){
        _actionMuLiveData.value = "apply_lease"
    }

    fun selectProductType(){
        _actionMuLiveData.value = "product_type"
    }
    private var areaNames: ArrayList<String>? = ArrayList()
    fun setUpProductTypeData() : ArrayList<String> {
        areaNames?.add("-")
        areaNames?.add("-")
        areaNames?.add("-")
        areaNames?.add("-")
        areaNames?.add("-")
        areaNames?.add("-")
        areaNames?.add("-")
        return areaNames!!
    }

    fun repaymentTerm(){
        _actionMuLiveData.value = "repayment_term"
    }
    private var repaymentNames: ArrayList<String>? = ArrayList()
    fun setUpRepaymentTermData() : ArrayList<String> {
        repaymentNames?.add("-")
        repaymentNames?.add("-")
        repaymentNames?.add("-")
        repaymentNames?.add("-")
        repaymentNames?.add("-")
        repaymentNames?.add("-")
        repaymentNames?.add("-")
        return repaymentNames!!
    }

}