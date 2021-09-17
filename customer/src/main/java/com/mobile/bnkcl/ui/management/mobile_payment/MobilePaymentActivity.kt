package com.mobile.bnkcl.ui.management.mobile_payment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.View.OnFocusChangeListener
import androidx.activity.viewModels
import com.bnkc.library.data.type.RunTimeDataStore
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.app.Constants.ANIMATE_LEFT
import com.bnkc.sourcemodule.base.BaseActivity
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityMobilePaymentBinding
import com.mobile.bnkcl.ui.pinview.PinCodeActivity
import com.mobile.bnkcl.utilities.Utils

class MobilePaymentActivity : BaseActivity<ActivityMobilePaymentBinding>(), View.OnClickListener {

    private val viewModel: MobilePaymentViewModel by viewModels()
    private var CONTRACT_NO: String? = null
    private var PAYMENT_AMOUNT: String? = null
    private var REPAYMENT_PLAN: String? = null
    private var paymentAccount: String? = null
    private var paymentCode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setAnimateType(ANIMATE_LEFT)
        super.onCreate(savedInstanceState)

        initToolbar()
        initView()
        initDisposable()

    }

    private fun initDisposable() {

        disposable = RxJava.listen(RxEvent.SessionExpired::class.java).subscribe {
            errorSessionDialog(it.title, it.message).onConfirmClicked {
                RunTimeDataStore.LoginToken.value = ""
                startActivity(Intent(this, PinCodeActivity::class.java))
            }
        }
    }

    private fun initView() {

        if (intent != null) {
            if (intent.hasExtra("CONTRACT_NO")) CONTRACT_NO = intent.getStringExtra("CONTRACT_NO")
//            if (intent.hasExtra("PAYMENT_AMOUNT")) viewModel.REPAYMENT_AMOUNT =
//                intent.getStringExtra("PAYMENT_AMOUNT").toString()
//            if (intent.hasExtra("REPAYMENT_PLAN")) viewModel.REPAYMENT_PLAN =
//                intent.getStringExtra("REPAYMENT_PLAN").toString()
        }

        Utils.setHideKeyboard(this, binding.parentLayout)

        binding.edtPaymentAccount.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            binding.llPaymentAccount.isSelected = hasFocus
        }

        binding.edtPaymentCode.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            binding.llPaymentCode.isSelected = hasFocus
        }

        binding.edtPaymentAccount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val text = binding.edtPaymentAccount.text.toString()
                if (text.endsWith("-") || text.endsWith(" ")) return

                if (text.length == 5) {
                    binding.edtPaymentAccount.setText(
                        StringBuffer(text).insert(
                            text.length - 1,
                            "-"
                        ).toString()
                    )
                    binding.edtPaymentAccount.setSelection(
                        binding.edtPaymentAccount.text!!.length
                    )
                }

                val inputTextAccount = s.toString().trim { it <= ' ' }.replace("-".toRegex(), "")
                binding.ivSendCode.setImageResource(if (inputTextAccount.length == 8) R.drawable.ic_otp_send_on_ico else R.drawable.ic_otp_send_off_ico)
                binding.ivSendCode.setOnClickListener(if (inputTextAccount.length == 8) this@MobilePaymentActivity else null)

                paymentAccount = if (inputTextAccount.length == 8) {
                    binding.edtPaymentAccount.text.toString().trim().replace("-", "")
                } else {
                    ""
                }
            }

        })

        binding.edtPaymentCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val text: String = binding.edtPaymentCode.text.toString()

                if (text.endsWith("-") || text.endsWith(" ")) return

                if (text.length == 4) {
                    binding.edtPaymentCode.setText(
                        StringBuffer(text).insert(
                            text.length - 1,
                            "-"
                        ).toString()
                    )
                    binding.edtPaymentCode.setSelection(binding.edtPaymentCode.length())
                }

                val inputSecurityCode = s.toString().trim { it <= ' ' }.replace("-".toRegex(), "")
                paymentCode = if (inputSecurityCode.length == 6) {
                    binding.edtPaymentCode.text.toString().trim().replace("-", "")
                } else {
                    ""
                }
                binding.btnPay.setOnClickListener(if (binding.btnPay.isActive()) this@MobilePaymentActivity else null)

            }

        })

        binding.btnPay.setActive(true)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_mobile_payment
    }

    private fun initToolbar() {
        binding.collToolbar.setExpandedTitleTypeface(Utils.getTypeFace(this, 3))
        binding.collToolbar.setCollapsedTitleTypeface(Utils.getTypeFace(this, 3))
        binding.toolbarLeftButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.toolbar_left_button -> {
                finish()
            }
            R.id.iv_send_code -> {
                Log.d(">>>", "onClick: SENDDD")
            }
            R.id.btn_pay -> {
                Log.d(">>>", "onClick: PAYYYY")
            }
        }
    }
}