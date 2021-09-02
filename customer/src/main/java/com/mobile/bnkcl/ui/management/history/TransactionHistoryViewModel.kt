package com.mobile.bnkcl.ui.management.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bnkc.library.data.type.Status
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.sourcemodule.base.BaseViewModel
import com.mobile.bnkcl.data.repository.lease.LeaseRepo
import com.mobile.bnkcl.data.request.lease.transaction.TransactionHistoryRequest
import com.mobile.bnkcl.data.response.lease.transaction_history.TransactionHistoryResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionHistoryViewModel @Inject constructor(private val leaseRepo: LeaseRepo) :
    BaseViewModel() {

    private val _transactionHistory: MutableLiveData<TransactionHistoryResponse> = MutableLiveData()
    val transactionHistoryLiveData: LiveData<TransactionHistoryResponse> = _transactionHistory

    fun getTransactionHistory(transactionHistoryRequest: TransactionHistoryRequest) {
        viewModelScope.launch {
            leaseRepo.getTransactionHistory(transactionHistoryRequest).onEach { resource ->
                if (resource.status == Status.ERROR) {
                    val code = resource.errorCode
                    val title = resource.messageTitle
                    val message = resource.messageDes
                    RxJava.publish(RxEvent.ServerError(code!!, title!!, message!!))
                } else {
                    _transactionHistory.value = resource.data
                }
            }.launchIn(viewModelScope)
        }
    }
}