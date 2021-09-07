package com.mobile.bnkcl.ui.management.full_payment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bnkc.library.prefer.CredentialSharedPrefer
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.base.BaseViewModel
import com.mobile.bnkcl.data.repository.lease.LeaseRepo
import com.mobile.bnkcl.data.request.lease.full_payment.FullPaymentRequest
import com.mobile.bnkcl.data.response.lease.full_payment.FullPaymentResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FullPaymentViewModel @Inject constructor(private val leaseRepo: LeaseRepo) :
    BaseViewModel() {

    @Inject
    lateinit var sharedPrefer: CredentialSharedPrefer
    private var fullPaymentRequest: FullPaymentRequest? = null
    private val _fullPayment: MutableLiveData<FullPaymentResponse> = MutableLiveData()
    val fullPaymentLiveData: LiveData<FullPaymentResponse> = _fullPayment

    fun getFullPayment(contractNo: String, repaymentDate: String, sort: String) {
        fullPaymentRequest = FullPaymentRequest(contractNo, repaymentDate, sort)
        if (!sharedPrefer.getPrefer(Constants.KEY_TOKEN).isNullOrEmpty()) {
            viewModelScope.launch {
                leaseRepo.getFullPayment(fullPaymentRequest!!)
                    .onEach { resource ->
                        _fullPayment.value = resource.data

                    }.launchIn(viewModelScope)
            }
        }
    }
}