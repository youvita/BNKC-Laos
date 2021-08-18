package com.mobile.bnkcl.ui.pinview

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bnkc.library.data.type.Status
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.sourcemodule.base.BaseViewModel
import com.mobile.bnkcl.data.repository.auth.AuthRepo
import com.mobile.bnkcl.data.repository.intro.MGRepo
import com.mobile.bnkcl.data.request.auth.LoginRequest
import com.mobile.bnkcl.data.request.auth.LoginRequestNoAuth
import com.mobile.bnkcl.data.request.auth.PreLoginRequest
import com.mobile.bnkcl.data.response.auth.LoginResponse
import com.mobile.bnkcl.data.response.auth.PreLoginResponse
import com.mobile.bnkcl.ui.main.MainActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PinViewModel @Inject constructor(private val authRepo: AuthRepo) : BaseViewModel(){

    var pinUI : Int = 0

    private val _login: MutableLiveData<LoginResponse> = MutableLiveData()
    val loginLiveData: LiveData<LoginResponse> = _login
    var logRequest: LoginRequest? = null
    var loginRequestNoAuth: LoginRequestNoAuth? = null
    fun loginNoAuth(){
        viewModelScope.launch {
            authRepo.loginUserNoAuth(loginRequestNoAuth!!).onEach { resource ->
                if (resource.status == Status.ERROR) {
                    val code = resource.errorCode
                    val title = resource.messageTitle
                    val message = resource.messageDes
                    RxJava.publish(RxEvent.ServerError(code!!, title!!, message!!))
                } else {
                    _login.value = resource.data
                }
            }.launchIn(viewModelScope)
        }
    }

}