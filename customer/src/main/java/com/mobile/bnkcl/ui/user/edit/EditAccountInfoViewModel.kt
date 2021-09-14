package com.mobile.bnkcl.ui.user.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bnkc.library.prefer.CredentialSharedPrefer
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.base.BaseViewModel
import com.mobile.bnkcl.data.repository.user.EditAccountInfoRepo
import com.mobile.bnkcl.data.response.user.EditAccountInfoData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class EditAccountInfoViewModel @Inject constructor(
    private val editAccountInfoRepo: EditAccountInfoRepo
) :
    BaseViewModel() {

    @Inject
    lateinit var sharedPrefer: CredentialSharedPrefer
    private var file: File? = null
    private val _editAccountInfo: MutableLiveData<Boolean> = MutableLiveData()
    val editAccountInfoLiveData: LiveData<Boolean> = _editAccountInfo


    fun setFile(file: File?) {
        this.file = file
    }

    fun getFile(): File? {
        return file
    }

    fun editAccountInfo(editAccountInfoData: EditAccountInfoData) {
        if (!sharedPrefer.getPrefer(Constants.KEY_TOKEN).isNullOrEmpty()) {
            viewModelScope.launch {
                editAccountInfoRepo.editAccountInfo(editAccountInfoData).onEach { resource ->
                    _editAccountInfo.value = resource.data
                }.launchIn(viewModelScope)
            }
        }
    }
}