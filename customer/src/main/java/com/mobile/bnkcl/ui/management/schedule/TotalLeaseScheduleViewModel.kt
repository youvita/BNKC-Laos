package com.mobile.bnkcl.ui.management.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bnkc.library.prefer.CredentialSharedPrefer
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.base.BaseViewModel
import com.mobile.bnkcl.data.repository.lease.LeaseRepo
import com.mobile.bnkcl.data.request.lease.total_schedule.TotalLeaseScheduleRequest
import com.mobile.bnkcl.data.response.lease.total_lease_schedules.TotalLeaseScheduleResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TotalLeaseScheduleViewModel @Inject constructor(private val leaseRepo: LeaseRepo) :
    BaseViewModel() {

    @Inject
    lateinit var sharedPrefer: CredentialSharedPrefer
    private val _totalLeaseSchedule: MutableLiveData<TotalLeaseScheduleResponse> = MutableLiveData()
    val totalLeaseScheduleLiveData: LiveData<TotalLeaseScheduleResponse> = _totalLeaseSchedule

    fun getTotalLeaseSchedule(totalLeaseScheduleRequest: TotalLeaseScheduleRequest) {
        if (!sharedPrefer.getPrefer(Constants.KEY_TOKEN).isNullOrEmpty()) {
            viewModelScope.launch {
                leaseRepo.getTotalLeaseSchedule(totalLeaseScheduleRequest).onEach { resource ->
                    _totalLeaseSchedule.value = resource.data
                }.launchIn(viewModelScope)
            }
        }
    }
}