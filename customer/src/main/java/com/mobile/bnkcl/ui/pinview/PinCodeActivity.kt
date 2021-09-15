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
import com.mobile.bnkcl.data.request.auth.LoginRequest
import com.mobile.bnkcl.data.request.auth.LoginRequestNoAuth
import com.mobile.bnkcl.data.request.auth.SignUpRequest
import com.mobile.bnkcl.databinding.ActivityPinCodeBinding
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

    var sessionId = ""
    var needAuth = false
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

            if (pinUI == "sign_up") {

                viewModel.signUpRequest = intent.getSerializableExtra("req_signup_obj") as SignUpRequest

                binding.pinView.reEnterPassword = true
                binding.pinViewModel!!.pinUI = 2

            } else if (pinUI == "forget") {

                binding.pinView.reEnterPassword = true
                binding.pinViewModel!!.pinUI = 3

            } else if (pinUI == "login") {
                if (intent.extras!!.containsKey(Constants.SESSION_ID)){
                    needAuth = true
                    sessionId = intent.getStringExtra(Constants.SESSION_ID).toString()
                }
                binding.pinViewModel!!.pinUI = 1

            } else if(pinUI == "reset"){
                binding.pinView.reEnterPassword = true
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
                when (viewModel.pinUI){
                    1->{
                        login(SecureUtils.encrypt(pinCode.trim { it <= ' ' }).trim())
                    }
                    2->{
                        signUp(SecureUtils.encrypt(pinCode.trim { it <= ' ' }).trim())
                    }
                    3->{

                    }
                    4->{

                    }
                }
            }

        }

        binding.pinView.setOnErrorListener = {errorCode : Int->
            if (errorCode == NOT_MATCH_PASSWORD){
                confirmDialog = ConfirmDialog.newInstance(
                    R.drawable.ic_badge_error,
                    getString(R.string.pin_14),
                    "Confirm password doesn't matched, Please try again",
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

    private fun observeViewModel(){
        viewModel.signUpLiveData.observe(this){
            Log.d(">>>", "Sign Up ::: $it")
            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("from", Constants.SIGN_UP_FAIL)
            intent.putExtra("result", it.username != null)
            startActivity(intent)
        }
    }

    private fun signUp(pin :String){
        viewModel.signUpRequest?.password = pin
        viewModel.signUp()
    }

    private fun login(pinCode : String) {
        val deviceInfo = DeviceInfo("test", "Android", "S21", "30")
        if (needAuth){
            val loginRequest = LoginRequest(
                sessionId,
                username,
                pinCode,
                deviceInfo
            )
            viewModel.logRequest = loginRequest
            viewModel.loginWithAuth()
        }else {
            val loginRequestNoAuth = LoginRequestNoAuth(
                username,
                pinCode,
                deviceInfo
            )
            viewModel.loginRequestNoAuth = loginRequestNoAuth
            viewModel.loginNoAuth()
        }

        viewModel.loginLiveData.observe(this){
            successListener()
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