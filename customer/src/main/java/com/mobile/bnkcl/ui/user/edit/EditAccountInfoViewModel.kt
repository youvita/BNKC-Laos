package com.mobile.bnkcl.ui.user.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bnkc.library.prefer.CredentialSharedPrefer
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.base.BaseViewModel
import com.mobile.bnkcl.data.repository.code.CodesRepo
import com.mobile.bnkcl.data.repository.user.EditAccountInfoRepo
import com.mobile.bnkcl.data.response.code.CodesResponse
import com.mobile.bnkcl.data.response.user.EditAccountInfoData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditAccountInfoViewModel @Inject constructor(
    private val editAccountInfoRepo: EditAccountInfoRepo,
    private val codesRepo: CodesRepo
) :
    BaseViewModel() {

    @Inject
    lateinit var sharedPrefer: CredentialSharedPrefer
    private val _editAccountInfo: MutableLiveData<Boolean> = MutableLiveData()
    private val _jobCodesLiveData: MutableLiveData<CodesResponse> = MutableLiveData()
    val editAccountInfoLiveData: LiveData<Boolean> = _editAccountInfo
    val jobCodesLiveData: LiveData<CodesResponse> = _jobCodesLiveData

    fun isUpdate(editAccountInfoData: EditAccountInfoData) {
        if (!sharedPrefer.getPrefer(Constants.KEY_TOKEN).isNullOrEmpty()) {
            viewModelScope.launch {
                editAccountInfoRepo.isUpdate(editAccountInfoData).onEach { resource ->
                    _editAccountInfo.value = resource.data
                }.launchIn(viewModelScope)
            }
        }
    }

    fun getJobTypeCodes() {
        if (!sharedPrefer.getPrefer(Constants.KEY_TOKEN).isNullOrEmpty()) {
            viewModelScope.launch {
                codesRepo.getCodes("JOB_TYPE").onEach { resource ->
                    _jobCodesLiveData.value = resource.data
                }.launchIn(viewModelScope)
            }
        }
    }
}