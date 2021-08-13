package com.mobile.bnkcl.ui.main

import androidx.lifecycle.*
import com.bnkc.library.data.type.Status
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.mobile.bnkcl.data.CommentsItem
import com.mobile.bnkcl.data.repository.comment.CommentRepo
import com.bnkc.sourcemodule.base.BaseViewModel
import com.mobile.bnkcl.data.repository.auth.AuthRepo
import com.mobile.bnkcl.data.repository.otp.OTPRepo
import com.mobile.bnkcl.data.request.auth.LoginRequest
import com.mobile.bnkcl.data.request.auth.PreLoginRequest
import com.mobile.bnkcl.data.request.otp.OTPVerifyRequest
import com.mobile.bnkcl.data.request.otp.SendOTPRequest
import com.mobile.bnkcl.data.response.auth.LoginResponse
import com.mobile.bnkcl.data.response.auth.PreLoginResponse
import com.mobile.bnkcl.data.response.otp.OTPVerifyResponse
import com.mobile.bnkcl.data.response.otp.SendOTPResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val commentRepo: CommentRepo, private val otpRepo: OTPRepo, private val authRepo: AuthRepo) : BaseViewModel() {

    private val _comments: MutableLiveData<List<CommentsItem>> = MutableLiveData()
    val comments: LiveData<List<CommentsItem>> get() = _comments

    fun getComments(){
        viewModelScope.launch {
            commentRepo.getComments().onEach { comments -> _comments.value = comments }.launchIn(viewModelScope)
        }
    }

    // for testing
    private val _sendOTP: MutableLiveData<SendOTPResponse> = MutableLiveData()
    val sendOTPLiveData: LiveData<SendOTPResponse> = _sendOTP
    var sendOTPRequest: SendOTPRequest = SendOTPRequest("2012345678")
    fun sendOTP(){
        viewModelScope.launch {
            otpRepo.sendOTP(sendOTPRequest).onEach { resource ->
                if (resource.status == Status.ERROR) {
                    val code = resource.errorCode
                    val title = resource.messageTitle
                    val message = resource.messageDes
                    RxJava.publish(RxEvent.ServerError(code!!, title!!, message!!))
                } else {
                    _sendOTP.value = resource.data
                }
            }.launchIn(viewModelScope)
        }
    }

    private val _verifyOTP: MutableLiveData<OTPVerifyResponse> = MutableLiveData()
    val verifyOTPLiveData: LiveData<OTPVerifyResponse> = _verifyOTP
    var otpVerifyRequest: OTPVerifyRequest? = null
    fun verifyOTP(){
        viewModelScope.launch {
            otpRepo.OTPVerify(otpVerifyRequest!!).onEach { resource ->
                if (resource.status == Status.ERROR) {
                    val code = resource.errorCode
                    val title = resource.messageTitle
                    val message = resource.messageDes
                    RxJava.publish(RxEvent.ServerError(code!!, title!!, message!!))
                } else {
                    _verifyOTP.value = resource.data
                }
            }.launchIn(viewModelScope)
        }
    }

    private val _preLogin: MutableLiveData<PreLoginResponse> = MutableLiveData()
    val preloginLiveData: LiveData<PreLoginResponse> = _preLogin
    var prelogRequest: PreLoginRequest? = null
    fun preLogin(){
        viewModelScope.launch {
            authRepo.preLogin(prelogRequest!!).onEach { resource ->
                if (resource.status == Status.ERROR) {
                    val code = resource.errorCode
                    val title = resource.messageTitle
                    val message = resource.messageDes
                    RxJava.publish(RxEvent.ServerError(code!!, title!!, message!!))
                } else {
                    _preLogin.value = resource.data
                }
            }.launchIn(viewModelScope)
        }
    }

    private val _login: MutableLiveData<LoginResponse> = MutableLiveData()
    val loginLiveData: LiveData<LoginResponse> = _login
    var logRequest: LoginRequest? = null
    fun login(){
        viewModelScope.launch {
            authRepo.loginUser(logRequest!!).onEach { resource ->
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
    // end for testing
}
