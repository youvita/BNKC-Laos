package com.mobile.bnkcl.ui.success

import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.base.BaseViewModel
import com.mobile.bnkcl.R
import com.mobile.bnkcl.ui.cscenter.AskBNKCActivity
import com.mobile.bnkcl.ui.lease.apply.ApplyLeaseActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor() : BaseViewModel() {

    private val _actionMuLiveData = MutableLiveData<String>()
    val actionLiveData = _actionMuLiveData

    var from = ""
    var resultLiveData = false
    var showSomeView = true

//    fun setResultToMuLiveData (result : Boolean){
//        resultLiveData = result
//    }

    fun resultImage() : Drawable?{
        when(from){

            Constants.SIGN_UP -> {
                if (resultLiveData){
                    ContextCompat.getDrawable(context, R.drawable.ic_badge_success_g_char)
                }else{
                    ContextCompat.getDrawable(context, R.drawable.ic_badge_failed_g_char)
                }
            }
            else -> {
                return if (resultLiveData){
                    ContextCompat.getDrawable(context, R.drawable.ic_badge_success_b_char)
                }else{
                    ContextCompat.getDrawable(context, R.drawable.ic_badge_failed_b_char)
                }
            }

        }
        return ContextCompat.getDrawable(context, R.drawable.ic_badge_success_b_char)
    }

    fun resultMsg() : String{
        when(from){
            AskBNKCActivity::class.java.simpleName -> {
                return if (resultLiveData){
                    "Inquiry is Submitted"
                }else{
                    "Inquiry is Failed"
                }
            }
            Constants.RESET_PIN -> {
                return if (resultLiveData){
                    "PIN Reset Successful"
                }else{
                    "PIN Reset Failed"
                }
            }
            Constants.SIGN_UP -> {
                return if (resultLiveData){
                    "Registration Successful"
                }else{
                    "Registration Failed"
                }
            }
            ApplyLeaseActivity::class.java.simpleName -> {
                return if (resultLiveData){
                    "Lease Application has been\n" +
                            "Successfully Submitted"
                }else{
                    "Lease Application has been\n" +
                            "Failed Submitted"
                }
            }
        }
        return ""
    }

    fun resultDescription() : String{
        when(from){
            AskBNKCActivity::class.java.simpleName -> {
                return if (resultLiveData){
                    "BNKC will review your inquiry\n" +
                            "and get back to you soon."
                }else{
                    "Internal Server Error\n" +
                            "(Error Code: 500)"
                }
            }

            Constants.SIGN_UP -> {
                if (resultLiveData){
                    "You have registered. Please Log In."
                }else{
                    "PIN number entry error and re-entry request statement.\n" +
                            "(Error Code: 001)"
                }
            }

        }
        return ""
    }

    fun resultLabelButton() : String{
        when(from){
            AskBNKCActivity::class.java.simpleName -> {
                return if (resultLiveData){
                    "CLOSE"
                }else{
                    "TRY AGAIN"
                }
            }
            Constants.RESET_PIN -> {
                return if (resultLiveData){
                    "LOG IN"
                }else{
                    "SET UP NEW PIN"
                }
            }
            Constants.SIGN_UP -> {
                return if (resultLiveData){
                    "LOG IN"
                }else{
                    "REGISTER AGAIN"
                }
            }
            ApplyLeaseActivity::class.java.simpleName -> {
                return if (resultLiveData){
                    "GO TO MAIN SCREEN"
                }else{
                    "TRY AGAIN"
                }
            }
        }
        return ""
    }

    fun actionClicked(){
        when(from) {
            AskBNKCActivity::class.java.simpleName -> _actionMuLiveData.value = AskBNKCActivity::class.java.simpleName
            Constants.RESET_PIN -> _actionMuLiveData.value = Constants.RESET_PIN
            Constants.SIGN_UP -> _actionMuLiveData.value = Constants.SIGN_UP
            ApplyLeaseActivity::class.java.simpleName -> _actionMuLiveData.value = AskBNKCActivity::class.java.simpleName
        }
    }

}