package com.mobile.bnkcl.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bnkc.library.data.type.Status
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.sourcemodule.base.BaseViewModel
import com.mobile.bnkcl.data.MGData
import com.mobile.bnkcl.data.repository.signup.SignUpRepo
import com.mobile.bnkcl.data.request.signup.PreSignUpRequest
import com.mobile.bnkcl.data.request.signup.SignUpRequest
import com.mobile.bnkcl.data.response.signup.PreSignUpResponse
import com.mobile.bnkcl.data.response.signup.SignUpResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val signUpRepo: SignUpRepo) : BaseViewModel() {

   private val _mgData: MutableLiveData<PreSignUpResponse> = MutableLiveData()
   val mgData: LiveData<PreSignUpResponse> get() = _mgData

   fun getPreSignUpData(){
      viewModelScope.launch {
         signUpRepo.getPreSignUpData().onEach { resource ->
            // catch error
            if (resource.status == Status.ERROR) {
               val code    = resource.errorCode
               val title   = resource.messageTitle
               val message = resource.messageDes
               RxJava.publish(RxEvent.ServerError(code!!, title!!, message!!))
            } else {
               _mgData.value = resource.data
            }

         }.launchIn(viewModelScope)
      }
   }

}