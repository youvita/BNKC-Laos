package com.mobile.bnkcl.ui.signup

import com.bnkc.sourcemodule.base.BaseViewModel
import com.mobile.bnkcl.data.repository.signup.SignUpRepo
import com.mobile.bnkcl.data.request.signup.PreSignUpRequest
import com.mobile.bnkcl.data.request.signup.SignUpRequest
import com.mobile.bnkcl.data.response.signup.PreSignUpResponse
import com.mobile.bnkcl.data.response.signup.SignUpResponse
import javax.inject.Inject

class SignUpViewModel @Inject constructor(private val signUpRepo: SignUpRepo) : BaseViewModel() {

   lateinit var signUpRequest : SignUpRequest
   lateinit var preSignUpRequest: PreSignUpRequest
   lateinit var signUpResponse: SignUpResponse
   lateinit var preSignUpResponse: PreSignUpResponse





}