package com.mobile.bnkcl.ui.cscenter.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bnkc.library.data.type.Status
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.sourcemodule.base.BaseViewModel
import com.mobile.bnkcl.data.repository.faq.FaqsRepo
import com.mobile.bnkcl.data.request.cscenter.ClaimDataRequest
import com.mobile.bnkcl.data.request.faq.FaqReq
import com.mobile.bnkcl.data.response.cscenter.ClaimDataResponse
import com.mobile.bnkcl.data.response.faq.FaqRes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FaqsViewModel @Inject constructor(private val faqsRepo: FaqsRepo) : BaseViewModel(){

    private val _faqsData: MutableLiveData<FaqRes> = MutableLiveData()
    val faqsLiveData: LiveData<FaqRes> get() = _faqsData
    private val lastPage = false
    var  request = FaqReq()

    fun getFaqsData(page_number: Int){

        request = FaqReq(page_number, request.page_size, "")

        viewModelScope.launch {
            faqsRepo.getFaqData(request).onEach { resource ->
                // catch error
                if (resource.status == Status.ERROR) {
                    val code    = resource.errorCode
                    val title   = resource.messageTitle
                    val message = resource.messageDes
                    RxJava.publish(RxEvent.ServerError(code!!, title!!, message!!))
                } else {
                    _faqsData.value = resource.data
                }
            }.launchIn(viewModelScope)
        }
    }
}