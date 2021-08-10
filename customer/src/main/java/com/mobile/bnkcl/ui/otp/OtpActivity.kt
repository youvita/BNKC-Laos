package com.mobile.bnkcl.ui.otp

import android.content.res.Resources.NotFoundException
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import com.bnkc.sourcemodule.base.BaseActivity
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityOtpBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtpActivity : BaseActivity<ActivityOtpBinding>() {

    val viewModel : OtpViewModel by viewModels()

    var sendOtp = false

    private var txtAgreement: String? = null
    /**
     * text watcher event changed listener
     */
    private val textOtpWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            val text: String = binding.edtOtp.getText().toString()
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
//                val otp: String =
//                    binding.edtOtp.text.toString().trim().replaceAll("-", "")
//                    binding.otpViewModel.getVerifyOTPModel().setPin(otp)
//                    binding.otpViewModel.requestVerifyOtp()
            }
            binding.tvCorrect.visibility = View.GONE
            binding.llOptCode.background = getDrawable(R.drawable.round_stroke_e1e5ec_8)
        }
    }

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            txtAgreement = getString(R.string.sign_up_28)
            binding.edtOtp.addTextChangedListener(textOtpWatcher)

            if (intent != null) {
                val action = intent.getStringExtra("ACTION_TAG")
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
//                    binding.llPhoneNo.setBackground(getDrawable(R.drawable.round_solid_f3f6f7_8))
//                    binding.edtPhonenumber.setFocusable(false)
//                    binding.edtPhonenumber.setEnabled(false)
//                    binding.edtPhonenumber.setTextColor(resources.getColor(R.color.color_c4d0d6))
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


//                    mOtpBinding.tvTitleToolbar.setText(getString(R.string.sign_up_title))
//                    mOtpBinding.llAgreement.setVisibility(View.VISIBLE)
//                    mOtpBinding.llStepSignup.setVisibility(View.VISIBLE)
//                    mOtpBinding.ivLogin.setVisibility(View.GONE)
//                    isLogin = false
                }
            }

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