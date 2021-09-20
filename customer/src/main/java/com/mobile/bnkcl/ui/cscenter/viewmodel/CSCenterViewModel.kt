package com.mobile.bnkcl.ui.cscenter.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bnkc.library.data.type.Resource
import com.bnkc.sourcemodule.base.BaseViewModel
import com.mobile.bnkcl.data.repository.cscenter.ClaimRepo
import com.mobile.bnkcl.data.request.cscenter.ClaimDataRequest
import com.mobile.bnkcl.data.response.cscenter.ClaimDataResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CSCenterViewModel @Inject constructor(private val claimRepo: ClaimRepo) :BaseViewModel() {

    //Notice Data
    private val _claimData: MutableLiveData<Resource<ClaimDataResponse>> = MutableLiveData()
    val claimLiveData: LiveData<Resource<ClaimDataResponse>> get() = _claimData

    var  request = ClaimDataRequest()
    var res = ClaimDataResponse()

    fun getClaimData(page_number: Int, loading: Boolean){
        request = ClaimDataRequest(page_number, request.page_size, "asc")
        viewModelScope.launch {
            claimRepo.getClaimData(request).onEach { resource ->
                _claimData.value = resource
            }.launchIn(viewModelScope)
        }
    }
}