package com.mobile.bnkcl.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bnkc.sourcemodule.base.BaseViewModel
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

   var sessionId = ""

   private val _mgData: MutableLiveData<PreSignUpResponse> = MutableLiveData()
   val mgData: LiveData<PreSignUpResponse> get() = _mgData

   private val _preSignUp: MutableLiveData<PreSignUpResponse> = MutableLiveData()
   val preSignUpLiveData: LiveData<PreSignUpResponse> = _preSignUp
   var preSignUpRequest: PreSignUpRequest = PreSignUpRequest()
   fun preSignUp(){
      viewModelScope.launch {
         authRepo.preSignUp(preSignUpRequest!!).onEach { resource ->
            _preSignUp.value = resource.data
         }.launchIn(viewModelScope)
      }
   }

   var addressReqObj : AddressReqObj = AddressReqObj()

   private val _signUpLiveData : MutableLiveData<SignUpResponse> = MutableLiveData()
   val signUpLiveData: LiveData<SignUpResponse> = _signUpLiveData
   var signUpRequest : SignUpRequest = SignUpRequest()
   fun signUp(){
      viewModelScope.launch {
         authRepo.signUpUser(signUpRequest).onEach { resource ->
            _signUpLiveData.value = resource.data
         }.launchIn(viewModelScope)
      }
   }

}