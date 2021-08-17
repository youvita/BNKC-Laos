package com.mobile.bnkcl.ui.otp

import android.content.res.Resources.NotFoundException
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.View.OnFocusChangeListener
import androidx.activity.viewModels
import androidx.lifecycle.observe
import com.bnkc.sourcemodule.base.BaseActivity
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.request.auth.PreLoginRequest
import com.mobile.bnkcl.data.request.otp.OTPVerifyRequest
import com.mobile.bnkcl.databinding.ActivityOtpBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtpActivity : BaseActivity<ActivityOtpBinding>() {

    val viewModel : OtpViewModel by viewModels()

    var pinID = ""
    var sendOtp = false

    private var txtAgreement: String? = null
    /**
     * text watcher event changed listener
     */
    private val textOtpWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
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
            }

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
            viewModel.sendOTP(binding.edtPhonenumber.text.toString().replace("-", ""))
            binding.edtOtp.text!!.clear()
            binding.ivSendOtp.setOnClickListener(null)
            binding.ivSendOtp.setImageResource(R.drawable.ic_otp_send_off_ico)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    };

    /**
     * login request observable
     */
    private fun loginRequest() {
//        binding.otpViewModel.login.observe(this) {
//            if (it) {
//                val intent = Intent(applicationContext, MainActivity::class.java)
//                startActivity(intent)
//            }
//        }
    }

    // for testing
    private fun sendOTP() {
        viewModel.sendOTPLiveData.observe(this) {
            Log.d("nng", it.toString())
            pinID = it.pin_id.toString()
            viewModel.otpVerifyRequest = OTPVerifyRequest("", pinID)
//            binding.viverifyOTP()
        }
    }

    private fun verifyOTP() {
        viewModel.verifyOTPLiveData.observe(this){
            Log.d("nng", it.toString())
            binding.enableButton = it.verified
//            viewModel.prelogRequest = PreLoginRequest(viewModel.sendOTPRequest.to, viewModel.otpVerifyRequest!!.pin_id)
//            preLogin()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {

            viewModel.context = this
            binding.otpViewModel = viewModel
            binding.enableButton = false
            txtAgreement = getString(R.string.sign_up_28)
            binding.agreement = txtAgreement
            binding.edtOtp.addTextChangedListener(textOtpWatcher)
            binding.edtPhonenumber.addTextChangedListener(textPhoneNumberWatcher)

            if (intent != null) {
                val action = intent.getStringExtra("ACTION_TAG")
                Log.d(">>>>>>", "action :: $action")
                if (action.equals("LOGIN", ignoreCase = true)) {

                    binding.otpViewModel!!.uiMode = 0

//                    binding.tvTitleToolbar.setText(getString(R.string.login_01))
//                    binding.llAgreement.setVisibility(View.GONE)
//                    binding.llStepSignup.setVisibility(View.GONE)
//                    binding.ivLogin.setVisibility(View.VISIBLE)
//                    isLogin = true
                } else if (action.equals("FORGET", ignoreCase = true)) {

                    binding.otpViewModel!!.uiMode = 1

//                    binding.ivBack.setImageDrawable(resources.getDrawable(R.drawable.nav_close_dark_btn))
//                    binding.tvTitleToolbar.setText(getString(R.string.setting_02))
//                    binding.llAgreement.setVisibility(View.GONE)
//                    binding.llStepSignup.setVisibility(View.GONE)
//                    binding.ivLogin.setVisibility(View.VISIBLE)
                    binding.llPhoneNo.setBackground(getDrawable(R.drawable.round_solid_f3f6f7_8))
                    binding.edtPhonenumber.setFocusable(false)
                    binding.edtPhonenumber.setEnabled(false)
                    binding.edtPhonenumber.setTextColor(resources.getColor(R.color.color_c4d0d6))
//                    val phone: String?
//                    phone = if (intent.hasExtra(Constant.LoginInfo.USER_ID)) {
//                        intent.getStringExtra(Constant.LoginInfo.USER_ID)
//                    } else {
//                        PreferenceDelegator.getInstance(this).get(Constant.LoginInfo.USER_ID)
//                    }
//                    mOtpBinding.edtPhonenumber.setTag(phone)
//                    mOtpBinding.edtPhonenumber.setText(FormatUtil.formatPhoneNumberMask(phone))
//                    isLogin = true
                } else if (action.equals("SIGN_UP", ignoreCase = true)) {

                    binding.otpViewModel!!.uiMode = 2
                    binding.otpViewModel!!.statFocus = 1
                    binding.otpViewModel!!.step = 1

//                    mOtpBinding.tvTitleToolbar.setText(getString(R.string.sign_up_title))
//                    mOtpBinding.llAgreement.setVisibility(View.VISIBLE)
//                    mOtpBinding.llStepSignup.setVisibility(View.VISIBLE)
//                    mOtpBinding.ivLogin.setVisibility(View.GONE)
//                    isLogin = false
                }

            }

            binding.edtPhonenumber.setOnFocusChangeListener { _, hasFocus ->
                binding.llPhoneNo.isSelected = hasFocus
            }

            binding.edtOtp.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
                binding.llOptCode.isSelected = hasFocus
            }

            sendOTP()

////            setAnimateType(ANIMATE_LEFT)
//            binding.otpViewModel = viewModel
////            preLoginViewModel = PreLoginViewModel(application)
////            setContentViewBinding(R.layout.activity_otp, otpViewModel, BR.otpViewModel)
////            mOtpBinding = mViewDataBinding as ActivityOtpBinding
////            setLoadingDialogObserver()
////            Utils.setHideKeyboard(this@OTPActivity, mOtpBinding.llWrapContent)
////            setStatusBarColor(resources.getColor(R.color.color_f5f7fc))
//            binding.edtOtp.setFocusable(false)
//            binding.edtOtp.setEnabled(false)
//            binding.ivSendOtp.setOnClickListener(null)
//            txtAgreement = getString(R.string.sign_up_28)
//            if (LocaleHelper.getLanguage(this)
//                    .equals("en")
//            ) //                mOtpBinding.tvAgree.setText(FormatUtil.getSeparateFontByLang(OTPActivity.this, 15, 37, txtAgreement, mOtpBinding.cbAgreement.isChecked()), TextView.BufferType.SPANNABLE);
//                binding.tvAgree.setText(txtAgreement)
//            val intent = intent
//            if (intent != null) {
//                action = intent.getStringExtra("ACTION_TAG")
//                if (action.equals("LOGIN", ignoreCase = true)) {
//                    binding.tvTitleToolbar.setText(getString(R.string.login_01))
//                    binding.llAgreement.setVisibility(View.GONE)
//                    binding.llStepSignup.setVisibility(View.GONE)
//                    binding.ivLogin.setVisibility(View.VISIBLE)
//                    isLogin = true
//                } else if (action.equals("RESET", ignoreCase = true)) {
//                    binding.ivBack.setImageDrawable(resources.getDrawable(R.drawable.nav_close_dark_btn))
//                    binding.tvTitleToolbar.setText(getString(R.string.setting_02))
//                    binding.llAgreement.setVisibility(View.GONE)
//                    binding.llStepSignup.setVisibility(View.GONE)
//                    binding.ivLogin.setVisibility(View.VISIBLE)
//                    binding.llPhoneNo.setBackground(getDrawable(R.drawable.round_solid_f3f6f7_8))
//                    binding.edtPhonenumber.setFocusable(false)
//                    mOtpBinding.edtPhonenumber.setEnabled(false)
//                    mOtpBinding.edtPhonenumber.setTextColor(resources.getColor(R.color.color_c4d0d6))
//                    val phone: String?
//                    phone = if (intent.hasExtra(Constant.LoginInfo.USER_ID)) {
//                        intent.getStringExtra(Constant.LoginInfo.USER_ID)
//                    } else {
//                        PreferenceDelegator.getInstance(this).get(Constant.LoginInfo.USER_ID)
//                    }
//                    mOtpBinding.edtPhonenumber.setTag(phone)
//                    mOtpBinding.edtPhonenumber.setText(FormatUtil.formatPhoneNumberMask(phone))
//                    isLogin = true
//                } else if (action.equals("SIGNUP", ignoreCase = true)) {
//                    mOtpBinding.tvTitleToolbar.setText(getString(R.string.sign_up_title))
//                    mOtpBinding.llAgreement.setVisibility(View.VISIBLE)
//                    mOtpBinding.llStepSignup.setVisibility(View.VISIBLE)
//                    mOtpBinding.ivLogin.setVisibility(View.GONE)
//                    isLogin = false
//                }
//            }
//            mOtpBinding.tvResend.setTypeface(Utils.getTypeFace(this, 2))
//            if (mOtpBinding.edtPhonenumber.getText().length() > 0) {
//                mOtpBinding.ivSendOtp.setImageResource(R.drawable.otp_send_on_ico)
//                mOtpBinding.ivSendOtp.setOnClickListener(ivSendOTPClickListener)
//            }
//            mOtpBinding.edtPhonenumber.addTextChangedListener(object : TextWatcher {
//                override fun beforeTextChanged(
//                    s: CharSequence,
//                    start: Int,
//                    count: Int,
//                    after: Int
//                ) {
//                }
//
//                override fun onTextChanged(
//                    s: CharSequence,
//                    start: Int,
//                    before: Int,
//                    count: Int
//                ) {
//                }
//
//                override fun afterTextChanged(s: Editable) {
//                    mOtpBinding.edtPhonenumber.setTag(s)
//                    val text: String = mOtpBinding.edtPhonenumber.getText().toString()
//                    textLength = mOtpBinding.edtPhonenumber.getText().length()
//                    if (text.startsWith("0")) {
//                        mOtpBinding.edtPhonenumber.setText("")
//                        return
//                    }
//                    if (text.endsWith("-") || text.endsWith(" ")) return
//                    if (textLength == 3) {
//                        mOtpBinding.edtPhonenumber.setText(
//                            StringBuilder(text).insert(text.length - 1, "-").toString()
//                        )
//                        mOtpBinding.edtPhonenumber.setSelection(textLength)
//                    } else if (textLength == 7) {
//                        mOtpBinding.edtPhonenumber.setText(
//                            StringBuilder(text).insert(text.length - 1, "-").toString()
//                        )
//                        mOtpBinding.edtPhonenumber.setSelection(textLength)
//                    }
//                    if (textLength <= 0) {
//                        mOtpBinding.ivSendOtp.setImageResource(R.drawable.otp_send_off_ico)
//                        mOtpBinding.ivSendOtp.setOnClickListener(null)
//                    } else {
//                        mOtpBinding.ivSendOtp.setImageResource(R.drawable.otp_send_on_ico)
//                        mOtpBinding.ivSendOtp.setOnClickListener(ivSendOTPClickListener)
//                    }
//                }
//            })
//            mOtpBinding.edtOtp.addTextChangedListener(object : TextWatcher {
//                override fun beforeTextChanged(
//                    s: CharSequence,
//                    start: Int,
//                    count: Int,
//                    after: Int
//                ) {
//                }
//
//                override fun onTextChanged(
//                    s: CharSequence,
//                    start: Int,
//                    before: Int,
//                    count: Int
//                ) {
//                }
//
//                override fun afterTextChanged(s: Editable) {
//                    val text: String = mOtpBinding.edtOtp.getText().toString()
//                    if (text.endsWith("-") || text.endsWith(" ")) return
//                    if (text.length == 4) {
//                        mOtpBinding.edtOtp.setText(
//                            StringBuffer(text).insert(text.length - 1, "-").toString()
//                        )
//                        mOtpBinding.edtOtp.setSelection(mOtpBinding.edtOtp.getText().length())
//                    }
//                    val inputOtp =
//                        s.toString().trim { it <= ' ' }.replace("-".toRegex(), "")
//                    if (inputOtp.length == 6) {
//                        val otp: String =
//                            mOtpBinding.edtOtp.getText().toString().trim().replaceAll("-", "")
//                        otpViewModel.getVerifyOTPModel().setPin(otp)
//                        otpViewModel.requestVerifyOtp()
//                    }
//                    mOtpBinding.tvCorrect.setVisibility(View.GONE)
//                    mOtpBinding.llOptCode.setBackground(getDrawable(R.drawable.round_stroke_e1e5ec_8))
//                }
//            })
//            mOtpBinding.edtOtp.setOnFocusChangeListener(OnFocusChangeListener { v, hasFocus ->
//                mOtpBinding.llOptCode.setSelected(
//                    hasFocus
//                )
//            })
//            mOtpBinding.edtPhonenumber.setOnFocusChangeListener(OnFocusChangeListener { v, hasFocus ->
//                mOtpBinding.llPhoneNo.setSelected(
//                    hasFocus
//                )
//            })
//            mOtpBinding.cbAgreement.setOnClickListener(this)
//            //            mOtpBinding.tvAgree.setOnClickListener(this);
//            mOtpBinding.ivBack.setOnClickListener(this)
//            initLiveData()
        } catch (e: NotFoundException) {
            e.printStackTrace()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_otp
    }



}