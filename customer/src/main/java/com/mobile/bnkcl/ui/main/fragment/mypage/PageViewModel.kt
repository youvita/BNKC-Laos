package com.mobile.bnkcl.ui.main.fragment.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bnkc.library.data.type.Status
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.sourcemodule.base.BaseViewModel
import com.mobile.bnkcl.data.repository.dashboard.DashboardRepo
import com.mobile.bnkcl.data.response.dashboard.DashboardResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PageViewModel @Inject constructor(private val dashboardRepo: DashboardRepo) : BaseViewModel() {
    private val _dashboardLiveData: MutableLiveData<DashboardResponse> = MutableLiveData()
    val dashboardLiveData: LiveData<DashboardResponse> = _dashboardLiveData
    fun getDashboard() {
        viewModelScope.launch {
            dashboardRepo.getDashboard().onEach { resource ->
                if (resource.status == Status.ERROR) {
                    val code = resource.errorCode
                    val title = resource.messageTitle
                    val message = resource.messageDes
                    RxJava.publish(RxEvent.ServerError(code!!, title!!, message!!))
                } else {
                    _dashboardLiveData.value = resource.data
                }
            }.launchIn(viewModelScope)
        }
    }
}