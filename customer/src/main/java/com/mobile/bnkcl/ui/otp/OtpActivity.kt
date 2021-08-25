package com.mobile.bnkcl.ui.otp

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources.NotFoundException
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.observe
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.base.BaseActivity
import com.bnkc.sourcemodule.util.Formats
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.request.auth.PreLoginRequest
import com.mobile.bnkcl.data.request.otp.OTPVerifyRequest
import com.mobile.bnkcl.databinding.ActivityOtpBinding
import com.mobile.bnkcl.ui.signup.TermsAndConditionsActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtpActivity : BaseActivity<ActivityOtpBinding>(), View.OnClickListener {

    val viewModel : OtpViewModel by viewModels()

    var lifeTime = 0
    var phoneNumber = ""
    var pinID = ""
    var sendOtp = false
    private var countDownTimer: CountDownTimer? = null
    private var txtAgreement: String? = null
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
                val otp: String =
                    binding.edtOtp.text.toString().trim().replace("-", "")
//                    binding.otpViewModel.getVerifyOTPModel().setPin(otp)
                    viewModel.otpVerifyRequest = OTPVerifyRequest(otp, pinID)
                    binding.otpViewModel!!.verifyOTP()
                verifyOTP()
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

            if (textLength == 3) {
                binding.edtPhonenumber.setText(
                    StringBuilder(text).insert(text.length - 1, "-").toString()
                )
                binding.edtPhonenumber.setSelection(textLength)
            } else if (textLength == 7) {
                binding.edtPhonenumber.setText(
                    StringBuilder(text).insert(text.length - 1, "-").toString()
                )
                binding.edtPhonenumber.setSelection(textLength)
            }else if (textLength == 11){
                binding.edtPhonenumber.setText(
                    StringBuilder(text).insert(text.length - 1, "-").toString()
                )
                binding.edtPhonenumber.setSelection(textLength)
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
            phoneNumber = binding.edtPhonenumber.text.toString().replace("-", "")
            viewModel._phoneNumberContent.value = phoneNumber
            viewModel.sendOTP(phoneNumber)
            binding.edtOtp.text!!.clear()
            binding.ivSendOtp.setOnClickListener(null)
            binding.ivSendOtp.setImageResource(R.drawable.ic_otp_send_off_ico)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    };

    // for testing
    private fun sendOTP() {
        viewModel.sendOTPLiveData.observe(this) {
            Log.d("nng", it.toString())
            pinID = it.pin_id.toString()
            binding.tvResend.setTextColor(resources.getColor(R.color.colorPrimary))
            lifeTime = it.lifetime!!
            resendTimer()
            Toast.makeText(this, "OTP : ${it.pin}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun verifyOTP() {
        viewModel.verifyOTPLiveData.observe(this){
            Log.d(">>>>>>>>", it.toString())
            binding.isVerified = it.verified!!
            Log.d(">>>>>>>>", "Verified livedata " + viewModel.isVerified)
            binding.tvCorrect.visibility = View.VISIBLE
            if (it.verified!!){
                viewModel.prelogRequest = PreLoginRequest(viewModel.sendOTPRequest.to, viewModel.otpVerifyRequest!!.pin_id)
                preLogin()

                binding.llOptCode.background = getDrawable(R.drawable.round_stroke_00695c_8)
                binding.tvCorrect.setTextColor(resources.getColor(R.color.color_00695c))
                binding.tvCorrect.text = getString(R.string.sign_up_23)
                binding.tvCorrect.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    getDrawable(R.drawable.ic_correct_ico),
                    null
                )
                binding.tvAgree.setTextColor(resources.getColor(R.color.color_263238))

            }else{
                countDownTimer!!.cancel()
                binding.tvCountdown.setTextColor(resources.getColor(R.color.color_cfd8dc))
                binding.llOptCode.background = getDrawable(R.drawable.round_stroke_d7191f_solid_ffffff_8)
                binding.tvCorrect.setTextColor(resources.getColor(R.color.colorPrimary))
                binding.tvCorrect.text = getString(R.string.sign_up_06)
                binding.tvCorrect.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    getDrawable(R.drawable.ic_info_red_ico_1),
                    null
                )
//                binding.tvResend.setOnClickListener(tvReSendOTPClickListener)
            }
        }
    }

    private fun preLogin() {
        viewModel.preLogin()
        viewModel.preloginLiveData.observe(this){
            Log.d("nng", it.toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setStatusBarColor(resources.getColor(R.color.color_f5f7fc))
        super.onCreate(savedInstanceState)
        try {

            viewModel.context = this
            binding.otpViewModel = viewModel
            binding.isVerified = false
            txtAgreement = getString(R.string.sign_up_28)
            binding.agreement = txtAgreement
            binding.edtOtp.addTextChangedListener(textOtpWatcher)
            binding.edtPhonenumber.addTextChangedListener(textPhoneNumberWatcher)

            if (intent != null) {
                val action = intent.getStringExtra("ACTION_TAG")
                val phoneNumber = intent.getStringExtra("PHONE_NUMBER")
                when {
                    action.equals("LOGIN", ignoreCase = true) -> {

                        binding.otpViewModel!!.uiMode = 0

                    }
                    action.equals("SIGN_UP", ignoreCase = true) -> {

                        binding.otpViewModel!!.uiMode = 1
                        binding.otpViewModel!!.step = 1

                    }
                    action.equals("FORGET", ignoreCase = true) -> {

                        binding.otpViewModel!!.uiMode = 2
                        binding.llPhoneNo.setBackground(getDrawable(R.drawable.round_solid_f3f6f7_8))
                        binding.edtPhonenumber.setFocusable(false)
                        binding.edtPhonenumber.setEnabled(false)
                        binding.edtPhonenumber.setTextColor(resources.getColor(R.color.color_c4d0d6))

                    }
                    action.equals("RESET", ignoreCase = true) -> {

                        binding.otpViewModel!!.uiMode = 3

                    }

                }

            }

            sendOTP()

            initView()

            initEvent()

        } catch (e: NotFoundException) {
            e.printStackTrace()
        }
    }

    fun initView(){
        binding.tvAgree.setTextColor(resources.getColor(R.color.color_cfd8dc))
        binding.tvAgree.setText(
            Formats.getSeparateFontByLang(
                this,
                18,
                txtAgreement!!.length,
                txtAgreement,
                binding.cbAgreement.isChecked()
            ), TextView.BufferType.SPANNABLE
        )
    }

    fun initEvent(){
        binding.edtPhonenumber.setOnFocusChangeListener { _, hasFocus ->
            binding.llPhoneNo.isSelected = hasFocus
        }

        binding.edtOtp.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            binding.llOptCode.isSelected = hasFocus
        }

        binding.cbAgreement.setOnClickListener {

            binding.tvAgree.setOnClickListener(if (binding.cbAgreement.isChecked()) this else null)
            if (binding.cbAgreement.isChecked()) {
                binding.tvAgree.setTextColor(resources.getColor(R.color.color_263238))
                binding.tvAgree.setText(
                    Formats.getSeparateFontByLang(
                        this,
                        18,
                        txtAgreement!!.length,
                        txtAgreement,
                        binding.cbAgreement.isChecked()
                    ), TextView.BufferType.SPANNABLE
                )
            } else {
                binding.tvAgree.setTextColor(resources.getColor(R.color.color_cfd8dc))
                binding.tvAgree.setText(
                    Formats.getSeparateFontByLang(
                        this,
                        18,
                        txtAgreement!!.length,
                        txtAgreement,
                        binding.cbAgreement.isChecked()
                    ), TextView.BufferType.SPANNABLE
                )
            }
//            if (otpViewModel.getVerifyOTPResponse() != null) {
//                enableContinueBtn(
//                    otpViewModel.getVerifyOTPResponse()
//                        .isVerified() && mOtpBinding.cbAgreement.isChecked()
//                )
//            } else {
//                enableContinueBtn(false)
//            }

        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_otp
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.tv_agree -> {
                val intent1 = Intent(this, TermsAndConditionsActivity::class.java)
                intent1.putExtra(Constants.WEB_URL, "/pages/terms_and_conditions.html")
                intent1.putExtra(Constants.WEB_TITLE, getString(R.string.sign_up_29))
                startActivity(intent1)
            }
        }
    }

    fun resendTimer(){
        binding.tv1.setTextColor(resources.getColor(com.bnkc.sourcemodule.R.color.color_263238))
        binding.tvCountdown.setTextColor(resources.getColor(com.bnkc.sourcemodule.R.color.color_263238))
        val lifeTime: Int = lifeTime * 1000
        if (countDownTimer != null) countDownTimer!!.cancel()
        countDownTimer = object : CountDownTimer(lifeTime.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvCountdown.setText(viewModel.millisecondsToTime(millisUntilFinished))
            }

            override fun onFinish() {
                binding.tvResend.setOnClickListener(tvReSendOTPClickListener)
                binding.tvCountdown.setTextColor(resources.getColor(com.bnkc.sourcemodule.R.color.color_cfd8dc))
            }
        }.start()
        binding.tvResend.setOnClickListener(null)
    }

    private var tvReSendOTPClickListener = View.OnClickListener() {
//        try {
//            otpViewModel.getSendOTPModel().setTo(getPhoneNo());
//            otpViewModel.requestSendOtp();
//            binding.edtOtp.getText().clear();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

}