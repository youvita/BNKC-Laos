package com.mobile.bnkcl.ui.notification

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
class NotificationViewModel @Inject constructor(private var alarmRepo: AlarmRepo) : BaseViewModel() {

    private val _getAlarmList: MutableLiveData<AlarmResponse> = MutableLiveData()
    val alarmListLiveData: LiveData<AlarmResponse> = _getAlarmList
    var alarmRequest: AlarmRequest? = null
    fun getAlarmList() {
        viewModelScope.launch {
            alarmRepo.getAlarmList(alarmRequest!!).onEach { resource ->
                if (resource.status == Status.ERROR) {
                    val code = resource.errorCode
                    val title = resource.messageTitle
                    val message = resource.messageDes
                    RxJava.publish(RxEvent.ServerError(code!!, title!!, message!!))
                } else {
                    _getAlarmList.value = resource.data
                }
            }.launchIn(viewModelScope)
        }
    }
}