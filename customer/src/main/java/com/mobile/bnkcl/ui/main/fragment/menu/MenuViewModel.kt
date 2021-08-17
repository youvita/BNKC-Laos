package com.mobile.bnkcl.ui.main.fragment.menu

import android.content.Intent
import com.bnkc.sourcemodule.base.BaseViewModel
import com.mobile.bnkcl.ui.otp.OtpActivity
import com.mobile.bnkcl.ui.pinview.PinCodeActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor() : BaseViewModel(){

    fun goToLogin(){
        val intent1 = Intent(context, PinCodeActivity::class.java)
        intent1.putExtra("pin_action", "login")
        context.startActivity(intent1)
    }

    fun goToSignUp(){
        val intent1 = Intent(context, OtpActivity::class.java)
        intent1.putExtra("ACTION_TAG", "SIGN_UP")
        context.startActivity(intent1)
    }

}