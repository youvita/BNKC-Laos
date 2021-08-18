package com.mobile.bnkcl.ui.notice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bnkc.library.data.type.Status
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.sourcemodule.base.BaseViewModel
import com.mobile.bnkcl.data.repository.notice.NoticeRepo
import com.mobile.bnkcl.data.request.notice.NoticeRequest
import com.mobile.bnkcl.data.response.notice.NoticeDataResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoticeViewModel @Inject constructor(private val noticeRepo: NoticeRepo) : BaseViewModel() {
    //Notice Data
    private val _noticeData: MutableLiveData<NoticeDataResponse> = MutableLiveData()
    val notice: LiveData<NoticeDataResponse> get() = _noticeData

    fun getNoticeData(request: NoticeRequest){
        viewModelScope.launch {
            noticeRepo.getNoticeData(request).onEach { resource ->
                // catch error
                if (resource.status == Status.ERROR) {
                    val code    = resource.errorCode
                    val title   = resource.messageTitle
                    val message = resource.messageDes
                    RxJava.publish(RxEvent.ServerError(code!!, title!!, message!!))
                } else {
                    _noticeData.value = resource.data
                }
            }.launchIn(viewModelScope)
        }
    }


}