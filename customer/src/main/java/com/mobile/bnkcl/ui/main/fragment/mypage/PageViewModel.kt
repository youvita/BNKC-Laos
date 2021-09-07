package com.mobile.bnkcl.ui.main.fragment.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bnkc.library.prefer.CredentialSharedPrefer
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.base.BaseViewModel
import com.mobile.bnkcl.data.repository.code.CodesRepo
import com.mobile.bnkcl.data.repository.dashboard.DashboardRepo
import com.mobile.bnkcl.data.response.code.CodesResponse
import com.mobile.bnkcl.data.response.dashboard.DashboardResponse
import com.mobile.bnkcl.data.response.dashboard.LeaseApplicationResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PageViewModel @Inject constructor(
    private val dashboardRepo: DashboardRepo,
    private val codesRepo: CodesRepo
) :
    BaseViewModel() {

    @Inject
    lateinit var sharedPrefer: CredentialSharedPrefer
    private val _dashboardLiveData: MutableLiveData<DashboardResponse> = MutableLiveData()
    private val _leaseApplicationLiveData: MutableLiveData<LeaseApplicationResponse> =
        MutableLiveData()
    private val _codesLiveData: MutableLiveData<CodesResponse> = MutableLiveData()
    val dashboardLiveData: LiveData<DashboardResponse> = _dashboardLiveData
    val leaseApplicationLiveData: LiveData<LeaseApplicationResponse> = _leaseApplicationLiveData
    val codesLiveData: LiveData<CodesResponse> = _codesLiveData

    fun getDashboard() {
        if (!sharedPrefer.getPrefer(Constants.KEY_TOKEN).isNullOrEmpty()) {
            viewModelScope.launch {
                dashboardRepo.getDashboard().onEach { resource ->
                    _dashboardLiveData.value = resource.data
                }.launchIn(viewModelScope)
            }
        }
    }

    fun getLeaseApplication(lease_request_status: String) {
        if (!sharedPrefer.getPrefer(Constants.KEY_TOKEN).isNullOrEmpty()) {
            viewModelScope.launch {
                dashboardRepo.getLeaseApplication(lease_request_status).onEach { resource ->
                    _leaseApplicationLiveData.value = resource.data
                }.launchIn(viewModelScope)
            }
        }
    }

    fun getCodes(group_id: String) {
        if (!sharedPrefer.getPrefer(Constants.KEY_TOKEN).isNullOrEmpty()) {
            viewModelScope.launch {
                codesRepo.getCodes(group_id).onEach { resource ->
                    _codesLiveData.value = resource.data
                }.launchIn(viewModelScope)
            }
        }
    }
}