package com.mobile.bnkcl.ui.otp

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources.NotFoundException
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.View.VISIBLE
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.base.BaseActivity
import com.bnkc.sourcemodule.dialog.SystemDialog
import com.bnkc.sourcemodule.util.Formats
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.request.auth.PreLoginRequest
import com.mobile.bnkcl.data.request.otp.OTPVerifyRequest
import com.mobile.bnkcl.data.request.signup.PreSignUpRequest
import com.mobile.bnkcl.databinding.ActivityOtpBinding
import com.mobile.bnkcl.ui.main.MainActivity
import com.mobile.bnkcl.ui.pinview.PinCodeActivity
import com.mobile.bnkcl.ui.signup.SignUpActivity
import com.mobile.bnkcl.ui.signup.TermsAndConditionsActivity
import com.mobile.bnkcl.utilities.FormatUtil
import com.mobile.bnkcl.utilities.UtilActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class OtpActivity : BaseActivity<ActivityOtpBinding>(), View.OnClickListener {

    val viewModel : OtpViewModel by viewModels()
    private var from : String = ""
    var lifeTime = 0
    var pinID = ""
    var sendOtp = false
    var sessionId : String = ""
    private var countDownTimer: CountDownTimer? = null
    private var txtAgreement: String? = null
    private var isFromPage: Boolean = false

    @Inject lateinit var systemDialog: SystemDialog

    /**
     * text watcher event changed listener
     */
    private val textOtpWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        @SuppressLint("UseCompatLoadingForDrawables")
        override fun afterTextChanged(s: Editable?) {
            val text: String = binding.edtOtp.text.toString()
            if (text.endsWith("-") || text.endsWith(" ")) return

            if (text.length == 4) {
                binding.edtOtp.setText(
                    StringBuffer(text).insert(text.length - 1, "-").toString()
                )
                binding.edtOtp.setSelection(binding.edtOtp.text!!.length)
            }

            val inputOtp =
                s.toString().trim { it <= ' ' }.replace("-".toRegex(), "")
            if (inputOtp.length == 6) {
                showLoading(true)
                val otp: String =
                    binding.edtOtp.text.toString().trim().replace("-", "")
//                    binding.otpViewModel.getVerifyOTPModel().setPin(otp)
                    viewModel.otpVerifyRequest = OTPVerifyRequest(otp, pinID)
                    binding.otpViewModel!!.verifyOTP()
            }
            binding.tvCorrect.visibility = View.GONE
            binding.llOptCode.background = getDrawable(R.drawable.round_stroke_e1e5ec_8)
        }
    }
    var textLength = 0
    private val textPhoneNumberWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            binding.edtPhonenumber.tag = s
            val text: String = binding.edtPhonenumber.text.toString()
            textLength = binding.edtPhonenumber.text.toString().length

            if (text.startsWith("0")) {
                binding.edtPhonenumber.setText("")
                return
            }

            if (text.endsWith("-") || text.endsWith(" ")) return

            when (textLength) {
                3 -> {
                    binding.edtPhonenumber.setText(
                        StringBuilder(text).insert(text.length - 1, "-").toString()
                    )
                    binding.edtPhonenumber.setSelection(textLength)
                }
                7 -> {
                    binding.edtPhonenumber.setText(
                        StringBuilder(text).insert(text.length - 1, "-").toString()
                    )
                    binding.edtPhonenumber.setSelection(textLength)
                }
                11 -> {
                    binding.edtPhonenumber.setText(
                        StringBuilder(text).insert(text.length - 1, "-").toString()
                    )
                    binding.edtPhonenumber.setSelection(textLength)
                }
            }
            binding.tvCorrect.visibility = View.GONE
            if (textLength <= 0) {
                binding.ivSendOtp.setImageResource(R.drawable.ic_otp_send_off_ico)
                binding.ivSendOtp.setOnClickListener(null)
            } else {
                binding.ivSendOtp.setImageResource(R.drawable.ic_otp_send_on_ico)
                binding.ivSendOtp.setOnClickListener(ivSendOTPClickListener)
            }
        }
    }

    private val ivSendOTPClickListener = View.OnClickListener() {
        try {
            showLoading(true)
            viewModel.phoneNumber = getPhoneNo().toString()
            viewModel.sendOTP(getPhoneNo().toString())
            binding.edtOtp.text!!.clear()
            binding.ivSendOtp.setOnClickListener(null)
            binding.ivSendOtp.setImageResource(R.drawable.ic_otp_send_off_ico)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // for testing
    private fun sendOTP() {
        viewModel.sendOTPLiveData.observe(this) {
            pinID = it.pin_id.toString()
            binding.tvResend.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
            lifeTime = it.lifetime!!
            binding.edtOtp.isEnabled = true
            resendTimer()
            Toast.makeText(this, "OTP : ${it.pin}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setStatusBarColor(ContextCompat.getColor(this, R.color.color_f5f7fc))
        setAnimateType(Constants.ANIMATE_LEFT)
        super.onCreate(savedInstanceState)
        try {

            UtilActivity.otpActivity = this

            viewModel.context = this
            binding.otpViewModel = viewModel
            binding.isVerified = false
            txtAgreement = getString(R.string.sign_up_28)
            binding.agreement = txtAgreement
            binding.edtOtp.addTextChangedListener(textOtpWatcher)
            binding.edtPhonenumber.addTextChangedListener(textPhoneNumberWatcher)

            if (intent != null) {
                val action = intent.getStringExtra("ACTION_TAG")

                when {
                    action.equals("LOGIN", ignoreCase = true) -> {
                        if (intent.hasExtra("from")){
                            from = intent.getStringExtra("from").toString()
                        }
                        binding.otpViewModel!!.uiMode = 0

                    }
                    action.equals("SIGN_UP", ignoreCase = true) -> {

                        binding.otpViewModel!!.uiMode = 1
                        binding.otpViewModel!!.step = 1
                        viewModel._isSignUP.value = 1

                    }
                    action.equals("FORGET", ignoreCase = true) -> {
                        var phoneNumber : String? = ""
                        if (intent.hasExtra("username")){
                            phoneNumber = intent.getStringExtra("username")
                        }

                        binding.otpViewModel!!.uiMode = 2
                        binding.llPhoneNo.background = ContextCompat.getDrawable(
                            this,
                            R.drawable.round_solid_f3f6f7_8
                        )
                        binding.edtPhonenumber.isFocusable = false
                        binding.edtPhonenumber.isEnabled = false
                        binding.edtPhonenumber.setTextColor(
                            ContextCompat.getColor(
                                this,
                                R.color.color_c4d0d6
                            )
                        )
                        binding.edtPhonenumber.setText(FormatUtil.formatPhoneNumberMask(phoneNumber.toString()))
                        binding.edtPhonenumber.tag = phoneNumber
                    }
                    action.equals("RESET", ignoreCase = true) -> {

                        binding.otpViewModel!!.uiMode = 3

                    }
                    action.equals("REQUIRE_LOGIN", ignoreCase = true) -> {

                        isFromPage = true
                        viewModel.setIsFromPage(isFromPage)

                    }

                }

            }

            observeData()

            sendOTP()

            initView()

            initEvent()

        } catch (e: NotFoundException) {
            e.printStackTrace()
        }
    }

    private fun getPhoneNo(): String? {
        return try {
            var phone: String =
                binding.edtPhonenumber.tag.toString().trim().replace("-", "")
            if (!phone.startsWith("0")) phone = "0$phone"
            phone
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            ""
        }
    }

    private fun observeData(){
        viewModel.preloginLiveData.observe(this){
            Log.d("nng", it.toString())
            if (it.session_id!!.isNotEmpty()){
                sessionId = it.session_id!!
                binding.btnContinue.setActive(true)
            }
        }
        viewModel.preSignUpLiveData.observe(this){
            Log.d("nng", it.toString())
            if (it.session_id!!.isNotEmpty()){
                sessionId = it.session_id!!
                binding.cbAgreement.isEnabled = true
            }
        }
        viewModel.verifyOTPLiveData.observe(this, {
            binding.isVerified = it.verified!!
            binding.tvCorrect.visibility = VISIBLE
            if (it.verified!!){
                when (binding.otpViewModel!!.uiMode) {
                    0 -> {
                        viewModel.prelogRequest = PreLoginRequest(
                            viewModel.sendOTPRequest.to,
                            viewModel.otpVerifyRequest!!.pin_id
                        )
                        viewModel.preLogin()
                    }
                    1 -> {
                        viewModel.preSignUpRequest = PreSignUpRequest(
                            viewModel.sendOTPRequest.to,
                            viewModel.otpVerifyRequest!!.pin_id
                        )
                        viewModel.preSignUp()
                    }
                    2 -> {
                        viewModel.prelogRequest = PreLoginRequest(
                            viewModel.sendOTPRequest.to,
                            viewModel.otpVerifyRequest!!.pin_id
                        )
                        viewModel.preLogin()
                    }
                }

                binding.llOptCode.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.round_stroke_00695c_8
                )
                binding.tvCorrect.setTextColor(ContextCompat.getColor(this, R.color.color_00695c))
                binding.tvCorrect.text = getString(R.string.sign_up_23)
                binding.tvCorrect.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    ContextCompat.getDrawable(this, R.drawable.ic_correct_ico),
                    null
                )
                binding.tvAgree.setTextColor(ContextCompat.getColor(this, R.color.color_263238))

            }else{
                countDownTimer!!.cancel()
                binding.tvCountdown.setTextColor(ContextCompat.getColor(this, R.color.color_cfd8dc))
                binding.llOptCode.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.round_stroke_d7191f_solid_ffffff_8
                )
                binding.tvCorrect.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
                binding.tvCorrect.text = getString(R.string.sign_up_06)
                binding.tvCorrect.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    ContextCompat.getDrawable(this, R.drawable.ic_info_red_ico_1),
                    null
                )
//                binding.tvResend.setOnClickListener(tvReSendOTPClickListener)
            }
        })
        viewModel.isSignUpLiveData.observe(this, {
            if (binding.otpViewModel!!.step == 1) {
                binding.rlOtp.visibility = VISIBLE
                binding.rlSignupInfo.visibility = View.GONE
            } else {
                binding.rlOtp.visibility = View.GONE
                binding.rlSignupInfo.visibility = VISIBLE

                binding.tvStep2.setTextColor(ContextCompat.getColor(this, R.color.color_d7191f))
                binding.ivStep2.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_step_personal_info_on
                    )
                )
                binding.tvStep1.setTextColor(ContextCompat.getColor(this, R.color.color_90A4AE))
                binding.ivStep1.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_step_otp_off
                    )
                )

                binding.btnContinue.setLabelButton(getString(R.string.comm_submit))
                binding.btnContinue.setActive(false)
            }
        })

        viewModel.isSignUpLiveData.observe(this, {
            if (binding.otpViewModel!!.step == 1) {
                binding.rlOtp.visibility = VISIBLE
                binding.rlSignupInfo.visibility = View.GONE
            } else {
                binding.rlOtp.visibility = View.GONE
                binding.rlSignupInfo.visibility = VISIBLE

                binding.tvStep2.setTextColor(ContextCompat.getColor(this, R.color.color_d7191f))
                binding.ivStep2.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_step_personal_info_on
                    )
                )
                binding.tvStep1.setTextColor(ContextCompat.getColor(this, R.color.color_90A4AE))
                binding.ivStep1.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_step_otp_off
                    )
                )

                binding.btnContinue.setLabelButton(getString(R.string.comm_submit))
                binding.btnContinue.setActive(false)
            }
        })
    }

    private fun initView(){
        binding.edtOtp.isEnabled = false
        binding.btnContinue.text = viewModel.setUpButtonText()
        binding.tvAgree.setTextColor(ContextCompat.getColor(this, R.color.color_cfd8dc))
        binding.tvAgree.setText(
            Formats.getSeparateFontByLang(
                this,
                18,
                txtAgreement!!.length,
                txtAgreement,
                binding.cbAgreement.isChecked
            ), TextView.BufferType.SPANNABLE
        )
    }

    private fun initEvent(){
        binding.btnContinue.setOnClickListener {
            if (binding.btnContinue.isActive()){
                when(viewModel.uiMode){
                    0->{  //Login
                        val intent = Intent(this, PinCodeActivity::class.java)
                        if (sessionId.isNotEmpty()) intent.putExtra(Constants.SESSION_ID, sessionId)
                        intent.putExtra("pin_action", "login")
                        intent.putExtra("username", viewModel.phoneNumber)
                        intent.putExtra("from", from)
                        startActivity(intent)
                    }
                    1->{ //Sign up
                        val intent = Intent(this, SignUpActivity::class.java)
                        if (sessionId.isNotEmpty()) intent.putExtra(Constants.SESSION_ID, sessionId)
                        intent.putExtra(Constants.USER_ID, viewModel.phoneNumber)
                        startActivity(intent)
                    }
                    2->{ //Forget
                        val intent = Intent(this, PinCodeActivity::class.java)
                        if (sessionId.isNotEmpty()) intent.putExtra(Constants.SESSION_ID, sessionId)
                        intent.putExtra("pin_action", "forget")
                        startActivity(intent)
                    }
                }
            }
        }

        binding.edtPhonenumber.setOnFocusChangeListener { _, hasFocus ->
            binding.llPhoneNo.isSelected = hasFocus
        }

        binding.edtOtp.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            binding.llOptCode.isSelected = hasFocus
        }

        binding.cbAgreement.setOnClickListener {
            var isAgreementOk = true
            if(binding.llAgreement.visibility == VISIBLE){
                isAgreementOk = binding.cbAgreement.isChecked
            }
            Log.d(">>>>", "initEvent: $isAgreementOk ")

            binding.btnContinue.setActive(binding.cbAgreement.isChecked)
            binding.tvAgree.setOnClickListener(if (binding.cbAgreement.isChecked) this else null)
            if (binding.cbAgreement.isChecked) {
                binding.tvAgree.setTextColor(ContextCompat.getColor(this, R.color.color_263238))
                binding.tvAgree.setText(
                    Formats.getSeparateFontByLang(
                        this,
                        18,
                        txtAgreement!!.length,
                        txtAgreement,
                        binding.cbAgreement.isChecked
                    ), TextView.BufferType.SPANNABLE
                )
            } else {
                binding.tvAgree.setTextColor(ContextCompat.getColor(this, R.color.color_cfd8dc))
                binding.tvAgree.setText(
                    Formats.getSeparateFontByLang(
                        this,
                        18,
                        txtAgreement!!.length,
                        txtAgreement,
                        binding.cbAgreement.isChecked
                    ), TextView.BufferType.SPANNABLE
                )
            }

        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_otp
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.tv_agree -> {
                val intent1 = Intent(this, TermsAndConditionsActivity::class.java)
                intent1.putExtra(Constants.WEB_URL, Constants.WB_TER_AND_CON)
                intent1.putExtra(Constants.WEB_TITLE, getString(R.string.sign_up_29))
                startActivity(intent1)
            }
        }
    }

    private fun resendTimer(){
        binding.tv1.setTextColor(ContextCompat.getColor(this, R.color.color_263238))
        binding.tvCountdown.setTextColor(ContextCompat.getColor(this, R.color.color_263238))
        val lifeTime: Int = lifeTime * 1000
        if (countDownTimer != null) countDownTimer!!.cancel()
        countDownTimer = object : CountDownTimer(lifeTime.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvCountdown.text = viewModel.millisecondsToTime(millisUntilFinished)
            }

            override fun onFinish() {
                binding.tvResend.setOnClickListener(tvReSendOTPClickListener)
                binding.tvCountdown.setTextColor(
                    ContextCompat.getColor(
                        this@OtpActivity,
                        R.color.color_cfd8dc
                    )
                )
            }
        }.start()
        binding.tvResend.setOnClickListener(null)
    }

    private var tvReSendOTPClickListener = View.OnClickListener {
        try {
            viewModel.sendOTP(viewModel.phoneNumber)
            binding.edtOtp.text?.clear()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (isFromPage) {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    override fun handleError(icon: Int, title: String, message: String, button: String) {
        super.handleError(icon, title, message, button)
        systemDialog = SystemDialog.newInstance(R.drawable.ic_badge_signed_up, getString(R.string.already_signed_up), getString(R.string.already_signed_up_msg), getString(R.string.nav_login))
        systemDialog.show(supportFragmentManager, systemDialog.tag)
        systemDialog.onConfirmClicked {
            val intent = Intent(this , PinCodeActivity::class.java)
            intent.putExtra("pin_action", "login")
            intent.putExtra("username", viewModel.phoneNumber)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        UtilActivity.otpActivity = null
    }

}