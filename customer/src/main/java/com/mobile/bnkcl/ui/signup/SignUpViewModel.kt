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

   var addressReqObj : AddressReqObj? = null
   var signUpRequest : SignUpRequest = SignUpRequest()

}