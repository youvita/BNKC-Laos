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
    private val _leaseScreeningLiveData: MutableLiveData<LeaseApplicationResponse> =
        MutableLiveData()
    private val _leaseResultLiveData: MutableLiveData<LeaseApplicationResponse> = MutableLiveData()
    private val _productCodesLiveData: MutableLiveData<CodesResponse> = MutableLiveData()
    private val _requestCodesLiveData: MutableLiveData<CodesResponse> = MutableLiveData()
    private val _progressCodesLiveData: MutableLiveData<CodesResponse> = MutableLiveData()
    val dashboardLiveData: LiveData<DashboardResponse> = _dashboardLiveData
    val leaseApplicationLiveData: LiveData<LeaseApplicationResponse> = _leaseApplicationLiveData
    val leaseScreeningLiveData: LiveData<LeaseApplicationResponse> = _leaseScreeningLiveData
    val leaseResultLiveData: LiveData<LeaseApplicationResponse> = _leaseResultLiveData
    val productCodesLiveData: LiveData<CodesResponse> = _productCodesLiveData
    val requestCodesLiveData: LiveData<CodesResponse> = _requestCodesLiveData
    val progressCodesLiveData: LiveData<CodesResponse> = _progressCodesLiveData

    fun getDashboard() {
        viewModelScope.launch {
            dashboardRepo.getDashboard().onEach { resource ->
                _dashboardLiveData.value = resource.data
            }.launchIn(viewModelScope)
        }
    }

    fun getLeaseApplication(lease_request_status: String) {
        viewModelScope.launch {
            dashboardRepo.getLeaseApplication(lease_request_status).onEach { resource ->
                _leaseApplicationLiveData.value = resource.data
            }.launchIn(viewModelScope)
        }
    }

    fun getLeaseScreening(lease_request_status: String) {
        viewModelScope.launch {
            dashboardRepo.getLeaseApplication(lease_request_status).onEach { resource ->
                _leaseScreeningLiveData.value = resource.data
            }.launchIn(viewModelScope)
        }
    }

    fun getLeaseResult(lease_request_status: String) {
        viewModelScope.launch {
            dashboardRepo.getLeaseApplication(lease_request_status).onEach { resource ->
                _leaseResultLiveData.value = resource.data
            }.launchIn(viewModelScope)
        }
    }

    fun getProductCodes() {
        viewModelScope.launch {
            codesRepo.getCodes("PRODUCT_TYPE").onEach { resource ->
                _productCodesLiveData.value = resource.data
            }.launchIn(viewModelScope)
        }
    }

    fun getLeaseProgressCodes() {
        viewModelScope.launch {
            codesRepo.getCodes("LEASE_PROGRESS_STATUS").onEach { resource ->
                _progressCodesLiveData.value = resource.data
            }.launchIn(viewModelScope)
        }
    }

    fun getLeaseRequestCodes() {
        viewModelScope.launch {
            codesRepo.getCodes("LEASE_REQUEST_STATUS").onEach { resource ->
                _requestCodesLiveData.value = resource.data
            }.launchIn(viewModelScope)
        }
    }
}