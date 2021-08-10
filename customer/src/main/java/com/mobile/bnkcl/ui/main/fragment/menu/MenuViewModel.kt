package com.mobile.bnkcl.ui.main.fragment.menu

import android.content.Intent
import com.bnkc.sourcemodule.base.BaseViewModel
import com.mobile.bnkcl.ui.otp.OtpActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor() : BaseViewModel(){

    fun goToLogin(){
        val intent1 = Intent(context, OtpActivity::class.java)
        intent1.putExtra("ACTION_TAG", "LOGIN")
        context.startActivity(intent1)
    }

}