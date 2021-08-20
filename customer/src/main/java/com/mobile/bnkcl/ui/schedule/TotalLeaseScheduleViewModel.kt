package com.mobile.bnkcl.ui.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bnkc.library.data.type.Status
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.sourcemodule.base.BaseViewModel
import com.mobile.bnkcl.data.repository.lease.total_schedule.TotalLeaseScheduleRepo
import com.mobile.bnkcl.data.request.lease.total_schedule.TotalLeaseScheduleRequest
import com.mobile.bnkcl.data.response.lease.total_lease_schedules.TotalLeaseScheduleResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TotalLeaseScheduleViewModel @Inject constructor(private val totalLeaseScheduleRepo: TotalLeaseScheduleRepo) :
    BaseViewModel() {

    private val _totalLeaseSchedule: MutableLiveData<TotalLeaseScheduleResponse> = MutableLiveData()
    val totalLeaseScheduleLiveData: LiveData<TotalLeaseScheduleResponse> = _totalLeaseSchedule

    fun getTotalLeaseSchedule(totalLeaseScheduleRequest: TotalLeaseScheduleRequest){
        viewModelScope.launch {
            totalLeaseScheduleRepo.getTotalLeaseSchedule(totalLeaseScheduleRequest).onEach { resource ->
                if (resource.status == Status.ERROR) {
                    val code = resource.errorCode
                    val title = resource.messageTitle
                    val message = resource.messageDes
                    RxJava.publish(RxEvent.ServerError(code!!, title!!, message!!))
                } else {
                    _totalLeaseSchedule.value = resource.data
                }
            }.launchIn(viewModelScope)
        }
    }
}