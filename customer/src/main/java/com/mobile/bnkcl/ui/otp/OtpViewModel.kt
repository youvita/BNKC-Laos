package com.mobile.bnkcl.ui.otp

import android.graphics.drawable.Drawable
import android.widget.TextView
import com.bnkc.sourcemodule.base.BaseViewModel
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.repository.login.LoginRepo
import com.mobile.bnkcl.data.request.LoginRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OtpViewModel @Inject constructor(private val loginRepo: LoginRepo) : BaseViewModel(){

    var uiMode : Int = 0

    fun setUpTextView(uiMode : Int) : String{
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

    fun setUpImageView(uiMode : Int) : Int{
        return when(uiMode){
            0->{
                R.drawable.ic_nav_back_dark_btn
            }
            1->{
                R.drawable.ic_nav_close_dark_btn
            }
            2->{
                R.drawable.ic_nav_back_dark_btn
            }
            else -> {
                R.drawable.ic_nav_back_dark_btn
            }
        }
    }

    fun setUpVisibilityView(uiMode : Int) : Boolean{
        return when(uiMode){
            0->{
                true
            }
            1->{
                false
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

    fun sendOtp(){

    }

    fun resendOtp(){

    }

    fun agreementViewClick(){

    }

    fun agreementCheckBoxClick(){

    }

    fun reqLogin(){

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