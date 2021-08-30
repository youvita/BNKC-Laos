package com.mobile.bnkcl.ui.cscenter.viewmodel

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bnkc.library.data.type.Status
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.sourcemodule.base.BaseViewModel
import com.mobile.bnkcl.data.repository.cscenter.ClaimRepo
import com.mobile.bnkcl.data.request.cscenter.ClaimDataRequest
import com.mobile.bnkcl.data.request.cscenter.ClaimReq
import com.mobile.bnkcl.data.response.cscenter.ClaimDataResponse
import com.mobile.bnkcl.data.response.cscenter.ClaimRes
import com.mobile.bnkcl.ui.pinview.PinCodeActivity
import com.mobile.bnkcl.ui.success.AskBNKCRegisterSuccessActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AskBNKCViewModel @Inject constructor(private val claimRepo: ClaimRepo) : BaseViewModel() {

    private val _claimData: MutableLiveData<ClaimRes> = MutableLiveData()
    val claimLiveData: LiveData<ClaimRes> get() = _claimData
    private var claimReq = ClaimReq()

    fun getClaim(category: String, subject : String, description : String){

        claimReq = ClaimReq(category,subject,description)

        viewModelScope.launch {
            claimRepo.getClaim(claimReq).onEach { resource ->
                if (resource.status == Status.ERROR) {
                    val code = resource.errorCode
                    val title = resource.messageTitle
                    val message = resource.messageDes
                    RxJava.publish(RxEvent.ServerError(code!!, title!!, message!!))
                } else {
                    _claimData.value = resource.data
                }
            }.launchIn(viewModelScope)

        }
    }
    }
