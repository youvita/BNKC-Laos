package com.mobile.bnkcl.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bnkc.library.data.type.Status
import com.bnkc.sourcemodule.base.BaseViewModel
import com.bnkc.sourcemodule.data.error.ErrorItem
import com.mobile.bnkcl.data.repository.auth.AuthRepo
import com.mobile.bnkcl.data.request.auth.SignUpRequest
import com.mobile.bnkcl.data.request.signup.PreSignUpRequest
import com.mobile.bnkcl.data.response.auth.AddressReqObj
import com.mobile.bnkcl.data.response.signup.PreSignUpResponse
import com.mobile.bnkcl.data.response.signup.SignUpResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val authRepo: AuthRepo) : BaseViewModel() {

   var addressReqObj : AddressReqObj? = null
   var signUpRequest : SignUpRequest = SignUpRequest()

   private val _preSignUp: MutableLiveData<PreSignUpResponse> = MutableLiveData()
   val preSignUpLiveData: LiveData<PreSignUpResponse> = _preSignUp
   var preSignUpRequest: PreSignUpRequest? = null
   fun preSignUp(){
      viewModelScope.launch {
         authRepo.preSignUp(preSignUpRequest!!).onEach { resource ->
            if (resource.status == Status.ERROR) {
               setError(ErrorItem(null, resource.code, resource.message, null))
            } else {
               if (resource.data != null) _preSignUp.value = resource.data
            }
         }.launchIn(viewModelScope)
      }
   }

}