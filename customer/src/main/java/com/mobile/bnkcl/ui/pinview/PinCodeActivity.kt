package com.mobile.bnkcl.ui.pinview

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.bnkc.sourcemodule.base.BaseActivity
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityPinCodeBinding
import com.mobile.bnkcl.ui.login.LoginActivity
import com.mobile.bnkcl.com.view.pincode.NOT_MATCH_PASSWORD
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PinCodeActivity : BaseActivity<ActivityPinCodeBinding>() {

    private val viewModel : PinViewModel by viewModels()

    var pinUI : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.pinViewModel = viewModel
        if (intent != null) {
//            extrasLogin = ExtrasLogin(this, intent)
            pinUI = intent.getStringExtra("pin_action").toString()
//            when (pinAction) {
//                "login" -> setAnimateType(ANIMATE_LEFT)
//                else -> setAnimateType(ANIMATE_NORMAL)
//            }

            if (pinUI == "sign_up") {

//                binding.pinViewModel.pinUI = 3

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

//                binding.pinViewModel.pinUI = 2

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

                viewModel.pinUI = 1

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
//                binding.pinViewModel.pinUI = 4



            }
        }

        binding.pinView.setOnClosePinListener = {
            finish()
        }

        binding.pinView.setOnCompletedListener = { pinCode : String ->

            if(pinCode == "1234")
                startActivity(Intent(this, LoginActivity::class.java))

            else {
                binding.pinView.errorAction()
                binding.pinView.clearPin()
            }
        }

        binding.pinView.setOnErrorListener = {errorCode : Int->
            if (errorCode == NOT_MATCH_PASSWORD){
                Toast.makeText(this,"Confirm password doesn't matched, Please try again!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.pinView.setOnPinKeyClickListener = { keyPressed : String ->
            Toast.makeText(this,"Key pressed was $keyPressed", Toast.LENGTH_SHORT).show()
        }

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_pin_code
    }
}