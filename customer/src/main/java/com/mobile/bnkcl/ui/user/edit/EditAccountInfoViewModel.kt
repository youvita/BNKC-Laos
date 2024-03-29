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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
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
    private val _editAccountInfo: MutableLiveData<Unit> = MutableLiveData()
    val editAccountInfoLiveData: LiveData<Unit> = _editAccountInfo


    fun setFile(file: File?) {
        this.file = file
    }

    fun getFile(): File? {
        return file
    }

    fun editAccountInfo(editAccountInfoData: EditAccountInfoData) {
        viewModelScope.launch {
            editAccountInfoRepo.editAccountInfo(editAccountInfoData).onEach { resource ->
                _editAccountInfo.value = resource.data
            }.launchIn(viewModelScope)
        }
    }

    fun uploadProfile() {
        // MultipartBody.Part is used to send also the actual filename
        val multipartBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "file_data", file!!.name, file!!
                    .asRequestBody("image/png".toMediaTypeOrNull())
            )
            .build()
        viewModelScope.launch {
            editAccountInfoRepo.uploadProfile(multipartBody).onEach { resource ->
                _editAccountInfo.value = resource.data
            }.launchIn(viewModelScope)
        }
    }
}