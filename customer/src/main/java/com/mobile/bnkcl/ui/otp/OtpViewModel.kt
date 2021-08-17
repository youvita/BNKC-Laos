package com.mobile.bnkcl.ui.otp

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.TextView
import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bnkc.library.data.type.Status
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.sourcemodule.base.BaseViewModel
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.repository.login.LoginRepo
import com.mobile.bnkcl.data.repository.otp.OTPRepo
import com.mobile.bnkcl.data.request.LoginRequest
import com.mobile.bnkcl.data.request.otp.OTPVerifyRequest
import com.mobile.bnkcl.data.request.otp.SendOTPRequest
import com.mobile.bnkcl.data.response.otp.OTPVerifyResponse
import com.mobile.bnkcl.data.response.otp.SendOTPResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OtpViewModel @Inject constructor(private val otpRepo: OTPRepo) : BaseViewModel(){

    var uiMode : Int = 0
    var isChecked : Boolean = false
    var statFocus : Int = 0
    var step : Int = 1

    private val _phoneNumberContent = MutableLiveData<String>()
    val phoneNumber : LiveData<String>
        get() = _phoneNumberContent

    fun setUpToolbarTitle() : String{
        Log.d(">>>>>>", "setUpTextView $uiMode")
        return when(uiMode){
            0->{
                "Login"
            }
            1->{
                "Forget"
            }
            2->{
                "Sign up"
            }
            else -> {
                "Login"
            }
        }
    }

    fun setUpButtonText() : String{
        Log.d(">>>>>>", "setUpTextView $uiMode")
        return when(uiMode){
            0->{
                "Login"
            }
            1->{
                "Continue"
            }
            2->{
                "Continue"
            }
            else -> {
                "Login"
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun setUpImageView() : Drawable{
        Log.d(">>>>>>", "" + uiMode)
        return when(uiMode){
            0->{
                context.resources.getDrawable(R.drawable.ic_nav_back_dark_btn)
            }
            1->{
                context.resources.getDrawable(R.drawable.ic_nav_close_dark_btn)
            }
            2->{
                context.resources.getDrawable(R.drawable.ic_nav_back_dark_btn)
            }
            else -> {
                context.resources.getDrawable(R.drawable.ic_nav_back_dark_btn)
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun setUpSendOtpImg() : Int {
        Log.d(">>>>>>", "setUpSendOtpImg >>> " + phoneNumber.value)
        return if (phoneNumber.value != null && phoneNumber.value!!.isEmpty()){
            R.drawable.ic_otp_send_off_ico
        }
        else{
            R.drawable.ic_otp_send_on_ico
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun setUpImageViewFocus() : Drawable{
        Log.d(">>>>>>", "" + uiMode)
        return when(step){
            0->{
                if (statFocus == 0){
                    context.resources.getDrawable(R.drawable.ic_step_otp_off)
                }else{
                    context.resources.getDrawable(R.drawable.ic_step_otp_on)
                }
            }
            1->{
                if (statFocus == 0){
                    context.resources.getDrawable(R.drawable.ic_step_personal_info_off)
                }else{
                    context.resources.getDrawable(R.drawable.ic_step_personal_info_on)
                }
            }
            else -> {
                context.resources.getDrawable(R.drawable.ic_step_otp_off)
            }
        }
    }

    fun setUpVisibilityView() : Boolean{
        Log.d(">>>>>>", "" + uiMode)
        return when(uiMode){
            0->{
                true
            }
            1->{
                true
            }
            2->{
                false
            }
            else -> {
                true
            }
        }
    }

    /** handle request data class */
    var loginRequest = LoginRequest()

    /**
     * handle enable/disable valid login button
     */
    fun enableLogin() : Boolean {
        return true
    }

    fun closeButton_Click(){
        (context as Activity).finish()
    }

    private val _sendOTP: MutableLiveData<SendOTPResponse> = MutableLiveData()
    val sendOTPLiveData: LiveData<SendOTPResponse> = _sendOTP

    fun sendOTP(phoneNumber : String){
        var sendOTPRequest: SendOTPRequest = SendOTPRequest(phoneNumber)
        Log.d(">>>>>>", "sendOTP ::: " + sendOTPRequest.to)
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

    fun resendOtp(){

    }

    fun agreementViewClick(){

    }

    fun agreementCheckBoxClick(){
        if (isChecked){
            isChecked = false
        }else{
            isChecked = true
        }
        Log.d(">>>>>>", "Hello $isChecked")
    }

    fun reqLogin(){
        Log.d(">>>>>>", "reqLogin ::: ")

    }

    //test countdown
    private fun millisecondsToTime(milliseconds: Long): String? {
        return try {
            val minutes = milliseconds / 1000 / 60
            val seconds = milliseconds / 1000 % 60
            val secondsStr = java.lang.Long.toString(seconds)
            val minuteStr = java.lang.Long.toString(minutes)
            val secs: String
            val min: String
            min = if (secondsStr.length >= 2) {
                secondsStr.substring(0, 2)
            } else {
                "0$secondsStr"
            }
            secs = if (secondsStr.length >= 2) {
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

}