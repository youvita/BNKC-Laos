package com.mobile.bnkcl.ui.cscenter.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bnkc.sourcemodule.base.BaseViewModel
import com.mobile.bnkcl.data.repository.cscenter.ClaimRepo
import com.mobile.bnkcl.data.request.cscenter.ClaimReq
import com.mobile.bnkcl.data.response.cscenter.ClaimRes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AskBNKCViewModel @Inject constructor(private val claimRepo: ClaimRepo) : BaseViewModel() {

    private val _claimData: MutableLiveData<ClaimRes> = MutableLiveData()
    val claimLiveData: LiveData<ClaimRes> get() = _claimData
    private var claimReq = ClaimReq()

    fun getClaim(category: String, subject : String, description : String){

        claimReq = ClaimReq(category,subject,description)

        viewModelScope.launch {
            claimRepo.getClaim(claimReq).onEach { resource ->
                _claimData.value = resource.data
            }.launchIn(viewModelScope)

        }
    }
    }
