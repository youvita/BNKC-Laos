package com.mobile.bnkcl.ui.cscenter.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bnkc.library.data.type.Status
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.sourcemodule.base.BaseViewModel
import com.mobile.bnkcl.data.repository.cscenter.ClaimDetailRepo
import com.mobile.bnkcl.data.request.cscenter.ClaimDetailReq
import com.mobile.bnkcl.data.response.cscenter.ClaimDetailRes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AskBNKCDetailViewModel @Inject constructor(private val claimRepo: ClaimDetailRepo) : BaseViewModel() {

    //Notice Data
    private val _claimDetailData: MutableLiveData<ClaimDetailRes> = MutableLiveData()
    val claimDetailLiveData: LiveData<ClaimDetailRes> get() = _claimDetailData

    fun getClaimDetailData(request: ClaimDetailReq){
        viewModelScope.launch {
            claimRepo.getClaimDetailData(request).onEach { resource ->
                _claimDetailData.value = resource.data
            }.launchIn(viewModelScope)
        }
    }
}