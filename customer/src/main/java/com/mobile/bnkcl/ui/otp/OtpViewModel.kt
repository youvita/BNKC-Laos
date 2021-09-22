package com.mobile.bnkcl.ui.otp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bnkc.library.data.type.Status
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.base.BaseViewModel
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.repository.auth.AuthRepo
import com.mobile.bnkcl.data.repository.otp.OTPRepo
import com.mobile.bnkcl.data.request.auth.LoginRequest
import com.mobile.bnkcl.data.request.auth.LoginRequestNoAuth
import com.mobile.bnkcl.data.request.auth.PreLoginRequest
import com.mobile.bnkcl.data.request.otp.OTPVerifyRequest
import com.mobile.bnkcl.data.request.otp.SendOTPRequest
import com.mobile.bnkcl.data.request.signup.PreSignUpRequest
import com.mobile.bnkcl.data.response.auth.LoginResponse
import com.mobile.bnkcl.data.response.auth.PreLoginResponse
import com.mobile.bnkcl.data.response.otp.OTPVerifyResponse
import com.mobile.bnkcl.data.response.otp.SendOTPResponse
import com.mobile.bnkcl.data.response.signup.PreSignUpResponse
import com.mobile.bnkcl.ui.main.MainActivity
import com.mobile.bnkcl.ui.pinview.PinCodeActivity
import com.mobile.bnkcl.ui.signup.SignUpActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OtpViewModel @Inject constructor(private val otpRepo: OTPRepo, private val authRepo: AuthRepo) : BaseViewModel(){

    var uiMode : Int = 0
    var isChecked : Boolean = false
    var step : Int = 1
    var isFromPage: Boolean = false

    val _isSignUP = MutableLiveData<Int>()
    val isSignUpLiveData = _isSignUP

    var phoneNumber = ""

//    val _phoneNumberContent = MutableLiveData<String>()
//    val phoneNumber : LiveData<String>
//        get() = _phoneNumberContent

    var isVerified = false

    fun setUpToolbarTitle() : String{
        Log.d(">>>>>>", "setUpTextView $uiMode")
        return when(uiMode){
            0->{
                "Login"
            }
            1->{
                "Sign UP"
            }
            2->{
                "Forget PIN"
            }
            3->{
                "Reset PIN"
            }
            else -> {
                "Login"
            }
        }
    }

    // 0 : Login, 1 : Sign up , 2 : Forget , 3 : Reset
    fun setUpButtonText() : String {
        Log.d(">>>>>>", "setUpTextView $uiMode")
        return when(uiMode){
            0->{
                "Login"
            }
            else -> {
                "Continue"
            }
        }
    }

    // 0 : Login, 1 : Sign up , 2 : Forget , 3 : Reset
    @SuppressLint("UseCompatLoadingForDrawables")
    fun setUpImageView() : Drawable{
        Log.d(">>>>>>", "" + uiMode)
        return when(uiMode){
            0->{
                ContextCompat.getDrawable(context, R.drawable.ic_nav_back_dark_btn)!!
            }
            1->{
                ContextCompat.getDrawable(context, R.drawable.ic_nav_back_dark_btn)!!
            }
            2->{
                ContextCompat.getDrawable(context, R.drawable.ic_nav_close_dark_btn)!!
            }
            3->{
                ContextCompat.getDrawable(context, R.drawable.ic_nav_close_dark_btn)!!
            }
            else -> {
                context.resources.getDrawable(R.drawable.ic_nav_back_dark_btn)
            }
        }
    }

//    @SuppressLint("UseCompatLoadingForDrawables")
//    fun setUpSendOtpImg() : Int {
//        Log.d(">>>>>>", "setUpSendOtpImg >>> " + phoneNumber.value)
//        return if (phoneNumber.value != null && phoneNumber.value!!.isEmpty()){
//            R.drawable.ic_otp_send_off_ico
//        }
//        else{
//            R.drawable.ic_otp_send_on_ico
//        }
//    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun setUpImageStep1() : Drawable? {
        Log.d(">>>>>>", "" + uiMode)
        return when(step){
            1->{
                ContextCompat.getDrawable(context, R.drawable.ic_step_otp_on)
            }
            else -> {
                ContextCompat.getDrawable(context, R.drawable.ic_step_otp_off)
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun setUpImageStep2() : Drawable? {
        Log.d(">>>>>>", "" + uiMode)
        return when(step){
            1->{
                ContextCompat.getDrawable(context, R.drawable.ic_step_personal_info_off)
            }
            else -> {
                ContextCompat.getDrawable(context, R.drawable.ic_step_personal_info_on)
            }
        }
    }

    // 0 : Login, 1 : Sign up , 2 : Forget , 3 : Reset
    fun setUpVisibilityView() : Boolean{
        Log.d(">>>>>>", "" + uiMode)
        return when(uiMode){
            0->{
                true
            }
            1->{
                false
            }
            2->{
                true
            }
            3->{
                true
            }
            else -> {
                true
            }
        }
    }

    /**
     * handle enable/disable valid login button
     */
    fun enableLogin() : Boolean {
        return true
    }

    fun closeButton_Click(){
        if (isFromPage) {
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
        (context as Activity).finish()
    }

    private val _sendOTP: MutableLiveData<SendOTPResponse> = MutableLiveData()
    val sendOTPLiveData: LiveData<SendOTPResponse> = _sendOTP
    var sendOTPRequest : SendOTPRequest = SendOTPRequest()
    fun sendOTP(phoneNumber : String){
        sendOTPRequest = SendOTPRequest(phoneNumber)
        Log.d(">>>>>>", "sendOTP ::: " + sendOTPRequest.to)
        viewModelScope.launch {
            otpRepo.sendOTP(sendOTPRequest).onEach { resource ->
                _sendOTP.value = resource.data
            }.launchIn(viewModelScope)
        }
    }

    private val _verifyOTP: MutableLiveData<OTPVerifyResponse> = MutableLiveData()
    val verifyOTPLiveData: LiveData<OTPVerifyResponse> = _verifyOTP
    var otpVerifyRequest: OTPVerifyRequest? = null
    fun verifyOTP(){
        viewModelScope.launch {
            otpRepo.OTPVerify(otpVerifyRequest!!).onEach { resource ->
                _verifyOTP.value = resource.data
            }.launchIn(viewModelScope)
        }
    }

    private val _preLogin: MutableLiveData<PreLoginResponse> = MutableLiveData()
    val preloginLiveData: LiveData<PreLoginResponse> = _preLogin
    var prelogRequest: PreLoginRequest? = null
    fun preLogin(){
        viewModelScope.launch {
            authRepo.preLogin(prelogRequest!!).onEach { resource ->
                _preLogin.value = resource.data
            }.launchIn(viewModelScope)
        }
    }

    private val _preSignUp: MutableLiveData<PreSignUpResponse> = MutableLiveData()
    val preSignUpLiveData: LiveData<PreSignUpResponse> = _preSignUp
    var preSignUpRequest: PreSignUpRequest? = null
    fun preSignUp(){
        viewModelScope.launch {
            authRepo.preSignUp(preSignUpRequest!!).onEach { resource ->
                if (resource.data != null) _preSignUp.value = resource.data
            }.launchIn(viewModelScope)
        }
    }

    fun agreementViewClick(){

    }

    fun agreementCheckBoxClick(){
        isChecked = !isChecked
        Log.d(">>>>>>", "Hello $isChecked")
    }

    //test countdown
    fun millisecondsToTime(milliseconds: Long): String? {
        return try {
            val minutes = milliseconds / 1000 / 60
            val seconds = milliseconds / 1000 % 60
            val secondsStr = seconds.toString()
            val minuteStr = minutes.toString()
            val min: String = if (secondsStr.length >= 2) {
                secondsStr.substring(0, 2)
            } else {
                "0$secondsStr"
            }
            val secs: String = if (secondsStr.length >= 2) {
                secondsStr.substring(0, 2)
            } else {
                "0$secondsStr"
            }
            if (minuteStr.length == 1) {
                "0$minutes:$secs"
            } else {
                "$minutes:$secs"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun setIsFromPage(isTrue: Boolean) {
        this.isFromPage = isTrue
    }



}