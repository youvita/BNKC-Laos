package com.mobile.bnkcl.ui.pinview

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.observe
import com.bnkc.library.data.type.AppLogin
import com.bnkc.library.data.type.RunTimeDataStore
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.app.Constants.USER_ID
import com.bnkc.sourcemodule.base.BaseActivity
import com.bnkc.sourcemodule.dialog.ConfirmDialog
import com.mobile.bnkcl.R
import com.mobile.bnkcl.com.view.pincode.NOT_MATCH_PASSWORD
import com.mobile.bnkcl.data.request.auth.DeviceInfo
import com.mobile.bnkcl.data.request.auth.LoginRequest
import com.mobile.bnkcl.data.request.auth.LoginRequestNoAuth
import com.mobile.bnkcl.data.request.auth.SignUpRequest
import com.mobile.bnkcl.data.request.user.PreChangeRequest
import com.mobile.bnkcl.data.request.user.ResetPasswordRequest
import com.mobile.bnkcl.databinding.ActivityPinCodeBinding
import com.mobile.bnkcl.ui.dialog.LogOutDialog
import com.mobile.bnkcl.ui.home.HomeActivity
import com.mobile.bnkcl.ui.main.MainActivity
import com.mobile.bnkcl.ui.otp.OtpActivity
import com.mobile.bnkcl.ui.signup.SignUpViewModel
import com.mobile.bnkcl.ui.signup.SignUpViewModel_Factory
import com.mobile.bnkcl.ui.success.ResultActivity
import com.mobile.bnkcl.utilities.SecureUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PinCodeActivity : BaseActivity<ActivityPinCodeBinding>() {

    private val viewModel : PinViewModel by viewModels()
    var inputExistingPwd = true
    var sessionId = ""
    var needAuth = false
    var from : String = ""
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
            pinUI = intent.getStringExtra("pin_action").toString()
            if (intent.hasExtra("username")){
                username = intent.getStringExtra("username").toString()
            }
            if (pinUI == "sign_up") {

                viewModel.signUpRequest = intent.getSerializableExtra("req_signup_obj") as SignUpRequest
                setUpRegisterPinUI()

            } else if (pinUI == "forget") {

                sessionId = intent.getStringExtra(Constants.SESSION_ID).toString()

                binding.pinView.reEnterPassword = true
                binding.pinUi = 3

            } else if (pinUI == "login") {
                if (intent.hasExtra("from")){
                    from = intent.getStringExtra("from").toString()
                }

                if (intent.extras!!.containsKey(Constants.SESSION_ID)){
                    needAuth = true
                    sessionId = intent.getStringExtra(Constants.SESSION_ID).toString()
                }
                binding.pinUi = 1

            } else if(pinUI == "reset_pin"){

//                binding.pinView.reEnterPassword = true
                binding.pinUi = 4

            }
        }

        binding.pinView.setOnActionListener = { action : String ->
            when(action){
                "close" -> {
                    if (pinUI == "login") {
                        val logOutDialog = LogOutDialog()
                        logOutDialog.onConfirmClickedListener {
                            showLoading()
                            viewModel.logout()
                        }
                        logOutDialog.show(
                            supportFragmentManager,
                            logOutDialog.tag
                        )
                    } else {
                        finish()
                    }

                }
                "reset_pin" -> {
                    binding.pinView.clearPin()
                    val intent = Intent(this, OtpActivity::class.java)
                    intent.putExtra("username", username)
                    intent.putExtra("ACTION_TAG", "FORGET")
                    startActivity(intent)
                }
            }

        }

        binding.pinView.setOnCompletedListener = { pinCode : String ->
            Log.d(">>>>>", "setOnCompletedListener: $pinCode --- ${binding.pinUi}")
            if (pinCode.isNotEmpty()){
                when (binding.pinUi){
                    1->{
                        login(SecureUtils.encrypt(pinCode.trim { it <= ' ' }).trim())
                    }
                    2->{
                        signUp(SecureUtils.encrypt(pinCode.trim { it <= ' ' }).trim())
                    }
                    3->{
                        forgetPin(SecureUtils.encrypt(pinCode.trim { it <= ' ' }).trim())
                    }
                    4->{
                        //reset
                        resetPassword(SecureUtils.encrypt(pinCode.trim { it <= ' ' }).trim())
                    }
                }
            }

        }

        binding.pinView.setOnErrorListener = {errorCode : Int->
            if (errorCode == NOT_MATCH_PASSWORD){
                confirmDialog = ConfirmDialog.newInstance(
                    R.drawable.ic_badge_error,
                    getString(R.string.pin_14),
                    getString(R.string.pin_10),
                    getString(R.string.pin_16)
                )
                confirmDialog.onConfirmClickedListener {
                    binding.pinView.clearPin()
                }
                confirmDialog.isCancelable = false
                confirmDialog.show(supportFragmentManager, confirmDialog.tag)
            }
        }

        binding.pinView.setOnPinKeyClickListener = { keyPressed : String ->

        }

        observeViewModel()

    }

    private fun observeViewModel() {
        viewModel.signUpLiveData.observe(this) {
            Log.d(">>>", "Sign Up ::: $it")
            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("from", Constants.SIGN_UP)
            intent.putExtra("result", it.username != null)
            startActivity(intent)
        }
        viewModel.logoutLiveData.observe(this) {
            RunTimeDataStore.LoginToken.value = ""
            sharedPrefer.remove(USER_ID)

            val intent = Intent(this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            successListener()
        }
        viewModel.preResetLiveData.observe(this) {
            Log.d(">>>", "Pre Reset ::: $it")
            viewModel.resetPasswordRequest.session_id = it.session_id
            inputExistingPwd = false
            setUpRegisterPinUI()
        }

        viewModel.resetLiveData.observe(this) {
            Log.d(">>>", "Reset ::: $it")
            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("from", Constants.RESET_PIN)
            intent.putExtra("result", true)
            startActivity(intent)

        }

        viewModel.forgetPinLiveData.observe(this) {
            Log.d(">>>", "Sign Up ::: $it")
            finish()

        }
    }

    private fun setUpRegisterPinUI(){
        binding.pinView.clearPin()
        binding.pinView.reEnterPassword = true
//        binding.pinUi = 4
        binding.pinView.mPinMessage!!.text = getString(R.string.pin_registration)
    }

        private fun signUp(pin: String) {
            viewModel.signUpRequest?.password = pin
            viewModel.signUp()
        }

        private fun login(pinCode: String) {
            val deviceInfo = DeviceInfo("test", "Android", "S21", "30")
            if (needAuth) {
                val loginRequest = LoginRequest(
                    sessionId,
                    username,
                    pinCode,
                    deviceInfo
                )
                viewModel.logRequest = loginRequest
                viewModel.loginWithAuth()
            } else {
                val loginRequestNoAuth = LoginRequestNoAuth(
                    username,
                    pinCode,
                    deviceInfo
                )
                viewModel.loginRequestNoAuth = loginRequestNoAuth
                viewModel.loginNoAuth()
            }

            viewModel.loginLiveData.observe(this) {
                successListener()
                Log.d("nng", it.toString())

                if (it.cust_no != null || it.cust_no != null) {
                    RunTimeDataStore.LoginToken.value = it.token!!
                    sharedPrefer.putPrefer(USER_ID, username)
                    AppLogin.PIN.code = "Y"
                    if (from.isEmpty()) {
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                        finish()
                    } else {
                        Log.d("nng", "it.toString() " + AppLogin.PIN_TYPE)
                        AppLogin.InterceptIntent.code = "Y"
                        finish()
                    }

                } else {
                    countAttempt++
                    if (countAttempt > 4) {
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
                    } else {
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

    private fun resetPassword(pin : String){
        Log.d("nng", "resetPassword :: $inputExistingPwd")
        if (inputExistingPwd){
            viewModel.preChangeRequest.password = pin
            viewModel.preResetPassword()
        }else{
            viewModel.resetPasswordRequest.password = pin
            viewModel.resetPassword()
        }
    }

    private fun forgetPin(pin : String){
        viewModel.forgetPinRequest.session_id = sessionId
        viewModel.forgetPinRequest.username = username
        viewModel.forgetPinRequest.password = pin
        viewModel.forgetPIN()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_pin_code
    }
}