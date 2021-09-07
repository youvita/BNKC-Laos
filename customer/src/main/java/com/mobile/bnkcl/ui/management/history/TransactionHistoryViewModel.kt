package com.mobile.bnkcl.ui.management.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bnkc.library.prefer.CredentialSharedPrefer
import com.bnkc.sourcemodule.app.Constants
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

    @Inject
    lateinit var sharedPrefer: CredentialSharedPrefer
    private val _transactionHistory: MutableLiveData<TransactionHistoryResponse> = MutableLiveData()
    val transactionHistoryLiveData: LiveData<TransactionHistoryResponse> = _transactionHistory

    fun getTransactionHistory(transactionHistoryRequest: TransactionHistoryRequest) {
        if (!sharedPrefer.getPrefer(Constants.KEY_TOKEN).isNullOrEmpty()) {
            viewModelScope.launch {
                leaseRepo.getTransactionHistory(transactionHistoryRequest).onEach { resource ->
                    _transactionHistory.value = resource.data
                }.launchIn(viewModelScope)
            }
        }
    }
}