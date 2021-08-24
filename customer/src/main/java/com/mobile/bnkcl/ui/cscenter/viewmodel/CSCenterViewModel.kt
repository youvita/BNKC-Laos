package com.mobile.bnkcl.ui.cscenter.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bnkc.library.data.type.Status
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.sourcemodule.base.BaseViewModel
import com.mobile.bnkcl.data.repository.cscenter.ClaimRepo
import com.mobile.bnkcl.data.request.cscenter.ClaimDataRequest
import com.mobile.bnkcl.data.response.cscenter.ClaimDataResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CSCenterViewModel @Inject constructor(private val claimRepo: ClaimRepo) :BaseViewModel() {

    //Notice Data
    private val _claimData: MutableLiveData<ClaimDataResponse> = MutableLiveData()
    val claimLiveData: LiveData<ClaimDataResponse> get() = _claimData
    private val lastPage = false

    fun getClaimData(request: ClaimDataRequest){
        viewModelScope.launch {
            claimRepo.getClaimData(request).onEach { resource ->
                // catch error
                if (resource.status == Status.ERROR) {
                    val code    = resource.errorCode
                    val title   = resource.messageTitle
                    val message = resource.messageDes
                    RxJava.publish(RxEvent.ServerError(code!!, title!!, message!!))
                } else {
                    _claimData.value = resource.data
                }
            }.launchIn(viewModelScope)
        }
    }
    fun isLastPage(): Boolean {
        return lastPage
    }



}