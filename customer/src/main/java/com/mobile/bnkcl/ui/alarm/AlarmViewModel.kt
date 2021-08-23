package com.mobile.bnkcl.ui.alarm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bnkc.library.data.type.Status
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.sourcemodule.base.BaseViewModel
import com.mobile.bnkcl.data.repository.alarm.AlarmRepo
import com.mobile.bnkcl.data.request.alarm.AlarmRequest
import com.mobile.bnkcl.data.response.alarm.AlarmResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(private var alarmRepo: AlarmRepo) : BaseViewModel() {

    var pageNo = 0
    var lastpage = false

    private val _getAlarmList: MutableLiveData<AlarmResponse> = MutableLiveData()
    val alarmListLiveData: LiveData<AlarmResponse> = _getAlarmList
    fun getAlarmList() {
        viewModelScope.launch {
            alarmRepo.getAlarmList(AlarmRequest(pageNo, 10, "")).onEach { resource ->
                if (resource.status == Status.ERROR) {
                    val code = resource.errorCode
                    val title = resource.messageTitle
                    val message = resource.messageDes
                    RxJava.publish(RxEvent.ServerError(code!!, title!!, message!!))
                } else {
                    _getAlarmList.value = resource.data
                    lastpage = resource.data!!.last
                }
            }.launchIn(viewModelScope)
        }
    }

    fun isLastPage(): Boolean {
        return lastpage
    }
}