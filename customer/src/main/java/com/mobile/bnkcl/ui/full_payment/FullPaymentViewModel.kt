package com.mobile.bnkcl.ui.full_payment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bnkc.library.data.type.Status
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.sourcemodule.base.BaseViewModel
import com.mobile.bnkcl.data.repository.lease.full_payment.FullPaymentRepo
import com.mobile.bnkcl.data.request.lease.full_payment.FullPaymentRequest
import com.mobile.bnkcl.data.response.lease.full_payment.FullPaymentResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FullPaymentViewModel @Inject constructor(private val fullPaymentRepo: FullPaymentRepo) :
    BaseViewModel() {

    private var fullPaymentRequest: FullPaymentRequest? = null
    private val _fullPayment: MutableLiveData<FullPaymentResponse> = MutableLiveData()
    val fullPaymentLiveData: LiveData<FullPaymentResponse> = _fullPayment

    fun getFullPayment(contractNo: String, repaymentDate: String, sort: String) {
        fullPaymentRequest = FullPaymentRequest(contractNo, repaymentDate, sort)
        viewModelScope.launch {
            fullPaymentRepo.getFullPayment(fullPaymentRequest!!)
                .onEach { resource ->
                    if (resource.status == Status.ERROR) {
                        val code = resource.errorCode
                        val title = resource.messageTitle
                        val message = resource.messageDes
                        RxJava.publish(RxEvent.ServerError(code!!, title!!, message!!))
                    } else {
                        _fullPayment.value = resource.data
                    }
                }.launchIn(viewModelScope)
        }
    }
}