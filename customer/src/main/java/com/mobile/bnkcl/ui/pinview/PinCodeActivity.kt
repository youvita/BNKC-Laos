package com.mobile.bnkcl.ui.pinview

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
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
import com.mobile.bnkcl.databinding.ActivityPinCodeBinding
import com.mobile.bnkcl.ui.dialog.LogOutDialog
import com.mobile.bnkcl.ui.home.HomeActivity
import com.mobile.bnkcl.ui.otp.OtpActivity
import com.mobile.bnkcl.ui.success.ResultActivity
import com.mobile.bnkcl.utilities.SecureUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PinCodeActivity : BaseActivity<ActivityPinCodeBinding>() {

    private val viewModel : PinViewModel by viewModels()
    private var inputExistingPwd = true
    private var forceActionClick = false
    private var sessionId = ""
    private var needAuth = false
    private var from : String = ""
    private var username = ""
    private var pinUI : String = ""
    private var countAttempt = 0

    @Inject
    lateinit var confirmDialog: ConfirmDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        setStatusBarColor(ContextCompat.getColor(this, R.color.color_263238))
        super.onCreate(savedInstanceState)
        binding.pinViewModel = viewModel
        if (intent != null) {
            pinUI = intent.getStringExtra("pin_action").toString()
            if (intent.hasExtra("username")){
                username = intent.getStringExtra("username").toString()
            }
            when (pinUI) {
                "sign_up" -> {
                    viewModel.signUpRequest = intent.getSerializableExtra("req_signup_obj") as SignUpRequest
                    binding.pinUi = 2
                    setUpRegisterPinUI()
                }
                "forget" -> {
                    sessionId = intent.getStringExtra(Constants.SESSION_ID).toString()
                    binding.pinView.reEnterPassword = true
                    binding.pinUi = 3
                }
                "login" -> {
                    if (intent.hasExtra("from")){
                        from = intent.getStringExtra("from").toString()
                    }
                    if (intent.extras!!.containsKey(Constants.SESSION_ID)){
                        needAuth = true
                        sessionId = intent.getStringExtra(Constants.SESSION_ID).toString()
                    }
                    binding.pinUi = 1
                }
                "reset_pin" -> {
                    binding.pinUi = 4
                }
            }
        }

        binding.pinView.setOnActionListener = { action : String ->
            when(action){
                "close" -> {
                    onBackPressed()
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
            if (!forceActionClick) {
                forceActionClick = true
                if (pinCode.isNotEmpty()) {
                    when (binding.pinUi) {
                        1 -> {
                            login(SecureUtils.encrypt(pinCode.trim { it <= ' ' }).trim())
                        }
                        2 -> {
                            signUp(SecureUtils.encrypt(pinCode.trim { it <= ' ' }).trim())
                        }
                        3 -> {
                            forgetPin(SecureUtils.encrypt(pinCode.trim { it <= ' ' }).trim())
                        }
                        4 -> {
                            //reset
                            resetPassword(SecureUtils.encrypt(pinCode.trim { it <= ' ' }).trim())
                        }
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

        observeViewModel()

    }

    private fun observeViewModel() {
        viewModel.signUpLiveData.observe(this, {
            forceActionClick = false
            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("from", Constants.SIGN_UP)
            if (it.username!!.isNotEmpty()) {
                intent.putExtra("username", it.username)
            }
            intent.putExtra("result", it.username != null)
            startActivity(intent)
        })
        viewModel.logoutLiveData.observe(this, {
            RunTimeDataStore.LoginToken.value = ""
            sharedPrefer.remove(USER_ID)
            forceActionClick = false
            val intent = Intent(this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            successListener()
        })
        viewModel.preResetLiveData.observe(this, {
            viewModel.resetPasswordRequest.session_id = it.session_id
            inputExistingPwd = false
            setUpRegisterPinUI()
        })

        viewModel.resetLiveData.observe(this, {
            forceActionClick = false
            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("from", Constants.RESET_PIN)
            intent.putExtra("result", true)
            startActivity(intent)
        })

        viewModel.forgetPinLiveData.observe(this, {
            forceActionClick = false
            finish()
        })

        viewModel.loginLiveData.observe(this, {
            successListener()
            if (it.cust_no != null || it.cust_no != null) {
                RunTimeDataStore.LoginToken.value = it.token!!
                sharedPrefer.putPrefer(USER_ID, username)
                AppLogin.PIN.code = "Y"
                if (from.isEmpty()) {
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                    finish()
                } else {
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
        })
    }

    private fun setUpRegisterPinUI(){
        binding.pinView.clearPin()
        binding.pinView.reEnterPassword = true
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
        }

    private fun resetPassword(pin : String){
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

    override fun onBackPressed() {
        if (binding.pinUi == 1) {
            val logOutDialog = LogOutDialog()
            logOutDialog.onConfirmClickedListener {
                showLoading()
                viewModel.logout()
            }
            logOutDialog.show(
                supportFragmentManager,
                logOutDialog.tag
            )
        }
        else {
            finish()
        }
    }
}