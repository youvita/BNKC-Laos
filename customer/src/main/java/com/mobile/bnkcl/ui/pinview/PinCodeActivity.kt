package com.mobile.bnkcl.ui.pinview

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.observe
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.app.Constants.USER_ID
import com.bnkc.sourcemodule.base.BaseActivity
import com.bnkc.sourcemodule.dialog.ConfirmDialog
import com.mobile.bnkcl.R
import com.mobile.bnkcl.com.view.pincode.NOT_MATCH_PASSWORD
import com.mobile.bnkcl.data.request.auth.DeviceInfo
import com.mobile.bnkcl.data.request.auth.LoginRequestNoAuth
import com.mobile.bnkcl.databinding.ActivityPinCodeBinding
import com.mobile.bnkcl.ui.main.MainActivity
import com.mobile.bnkcl.ui.otp.OtpActivity
import com.mobile.bnkcl.utilities.SecureUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PinCodeActivity : BaseActivity<ActivityPinCodeBinding>() {

    private val viewModel : PinViewModel by viewModels()

    var username = ""
    var pinUI : String = ""
    var countAttempt = 0

    @Inject
    lateinit var confirmDialog: ConfirmDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        setStatusBarColor(resources.getColor(R.color.color_263238))
//        setStatusBarTransparent(this, false)
        super.onCreate(savedInstanceState)
        binding.pinViewModel = viewModel
        if (intent != null) {
//            extrasLogin = ExtrasLogin(this, intent)
            pinUI = intent.getStringExtra("pin_action").toString()
            username = intent.getStringExtra("username").toString()
//            when (pinAction) {
//                "login" -> setAnimateType(ANIMATE_LEFT)
//                else -> setAnimateType(ANIMATE_NORMAL)
//            }

            if (pinUI == "sign_up") {

                binding.pinViewModel!!.pinUI = 3

//                isRegister = true
//                if (intent.hasExtra("username")) {
//                    setUpPasswordViewModel.getSetupPasswordModel()
//                        .setUsername(intent.getStringExtra("username"))
//                }
//                if (intent.hasExtra("session_id")) {
//                    setUpPasswordViewModel.getSetupPasswordModel()
//                        .setSession_id(intent.getStringExtra("session_id"))
//                }

                // 6/17/2021 soktry
//                signUpViewModel.getSignUpModel()
//                    .setFamily_name(MemoryPreferenceDelegator.getInstance().get("family_name"))
//                signUpViewModel.getSignUpModel()
//                    .setGiven_name(MemoryPreferenceDelegator.getInstance().get("given_name"))
//                signUpViewModel.getSignUpModel().setRecommender_id(
//                    MemoryPreferenceDelegator.getInstance().get("recommender_id")
//                )
//                signUpViewModel.getSignUpModel()
//                    .setDate_of_birth(MemoryPreferenceDelegator.getInstance().get("date_of_birth"))
//                signUpViewModel.getSignUpModel()
//                    .setPhone_number(MemoryPreferenceDelegator.getInstance().get("phone_number"))
//                signUpViewModel.getSignUpModel()
//                    .setGender(MemoryPreferenceDelegator.getInstance().get("gender"))
//                signUpViewModel.getSignUpModel().setSession_id(intent.getStringExtra("session_id"))
            } else if (pinUI == "forget") {

                binding.pinViewModel!!.pinUI = 2

//                isForgetPin = true
//                if (intent.hasExtra("pin_id")) {
//                    preLoginViewModel.getPreLoginModel().setPin_id(intent.getStringExtra("pin_id"))
//                }
//                if (intent.hasExtra("username")) {
//                    preLoginViewModel.getPreLoginModel()
//                        .setUsername(intent.getStringExtra("username"))
//                }
//                preLoginViewModel.requestPreLogin()
            } else if (pinUI == "login") {

                binding.pinViewModel!!.pinUI = 1

//                binding.pinViewModel.pinUI = 1

//                isLogin = true
//                if (intent.hasExtra("username")) {
//                    loginViewModel.getLoginModel().setUsername(intent.getStringExtra("username"))
//                }
//                if (!PreferenceDelegator.getInstance(this).get(Constant.LoginInfo.USER_ID)
//                        .isEmpty()
//                ) {
//                    loginViewModel.getLoginModel().setUsername(
//                        PreferenceDelegator.getInstance(this).get(Constant.LoginInfo.USER_ID)
//                    )
//                }
            } else if(pinUI == "reset"){
                binding.pinViewModel!!.pinUI = 4

            }
        }

        binding.pinView.setOnActionListener = { action : String ->
            when(action){
                "close" -> {
                    finish()
                }
                "reset_pin" -> {
                    val intent = Intent(this, OtpActivity::class.java)
                    intent.putExtra("ACTION_TAG", "RESET")
                    startActivity(intent)
                }
            }

        }

        binding.pinView.setOnCompletedListener = { pinCode : String ->

            if (pinCode.isNotEmpty()){
                login(SecureUtils.encrypt(pinCode.trim { it <= ' ' }).trim())
            }
//            if(pinCode == "1234")
//                startActivity(Intent(this, LoginActivity::class.java))
//
//            else {
//                binding.pinView.errorAction()
//                binding.pinView.clearPin()
//            }
        }

        binding.pinView.setOnErrorListener = {errorCode : Int->
            if (errorCode == NOT_MATCH_PASSWORD){
                Toast.makeText(this,"Confirm password doesn't matched, Please try again!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.pinView.setOnPinKeyClickListener = { keyPressed : String ->

        }

    }

    private fun login(pinCode : String) {
        var deviceInfo = DeviceInfo("test", "Android", "S21", "30")
        var loginRequestNoAuth = LoginRequestNoAuth(
            username,
            pinCode,
            deviceInfo
        )
        viewModel.loginRequestNoAuth = loginRequestNoAuth
        viewModel.loginNoAuth()
        viewModel.loginLiveData.observe(this){
            Log.d("nng", it.toString())
            if (it.cust_no != null || it.cust_no != null) {
                sharedPrefer.putPrefer(Constants.KEY_TOKEN, it.token!!)
                sharedPrefer.putPrefer(Constants.USER_ID, username)

                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
            }else {
                countAttempt ++
                if (countAttempt > 4){
                    confirmDialog = ConfirmDialog.newInstance(
                        R.drawable.ic_badge_error,
                        getString(R.string.pin_11),
                        getString(R.string.pin_12),
                        getString(R.string.comm_reset_pin)
                    )
                    confirmDialog.onConfirmClickedListener {
                        binding.pinView.clearPin()
                        val intent = Intent(
                            this,
                            OtpActivity::class.java
                        )
                        intent.putExtra(
                            USER_ID,
                            username
                        )
                        intent.putExtra("ACTION_TAG", "RESET")
                        startActivity(intent)
                    }
                    confirmDialog.isCancelable = false
                    confirmDialog.show(supportFragmentManager, confirmDialog.tag)
                }
                else{
                    confirmDialog = ConfirmDialog.newInstance(
                        R.drawable.ic_badge_error,
                        getString(R.string.pin_14),
                        getString(R.string.pin_15),
                        getString(R.string.pin_16)
                    )
                    confirmDialog.onConfirmClickedListener {
                        binding.pinView.clearPin()
                    }
                    confirmDialog.isCancelable = false
                    confirmDialog.show(supportFragmentManager, confirmDialog.tag)
                }
            }
        }
    }

    fun checkResponse(){

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_pin_code
    }
}