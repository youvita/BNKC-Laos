package com.mobile.bnkcl.ui.lease.calculate

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.bnkc.library.data.type.RunTimeDataStore
import com.bnkc.library.util.Constants.REPAYMENT_TERM
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.base.BaseActivity
import com.bnkc.sourcemodule.dialog.ListChoiceDialog
import com.bnkc.sourcemodule.dialog.SystemDialog
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.response.lease.ItemResponseObject
import com.mobile.bnkcl.databinding.ActivityLeaseCalculateBinding
import com.mobile.bnkcl.ui.lease.calculate.result.CalculateResultActivity
import com.mobile.bnkcl.ui.pinview.PinCodeActivity
import com.mobile.bnkcl.utilities.FormatUtil
import com.mobile.bnkcl.utilities.Utils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LeaseCalculateActivity : BaseActivity<ActivityLeaseCalculateBinding>() {

    private val viewModel : LeaseCalculateViewModel by viewModels()
    private var selectedItem = -1
    private lateinit var repaymentCodes : ArrayList<ItemResponseObject>

    @Inject
    lateinit var systemDialog: SystemDialog

    @Inject
    lateinit var listChoiceDialog: ListChoiceDialog

    private val textInterestRateWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        @SuppressLint("UseCompatLoadingForDrawables")
        override fun afterTextChanged(s: Editable?) {
            if (s.toString().isNotEmpty()) viewModel.leaseCalculateReq.interest_rate = s.toString().toInt()
            binding.edRate.setSelection(binding.edRate.text!!.length)

            val amount = if(viewModel.leaseCalculateReq.lease_amount != null){
                viewModel.leaseCalculateReq.lease_amount
            }else {
                ""
            }
            val term = if(viewModel.leaseCalculateReq.repayment_term != null){
                viewModel.leaseCalculateReq.repayment_term
            }else {
                ""
            }

            binding.btnCalculate.isEnable(
                amount!!,
                s.toString(),
                term.toString()
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setStatusBarColor(ContextCompat.getColor(this, R.color.color_f5f7fc))
        setAnimateType(Constants.ANIMATE_LEFT)
        super.onCreate(savedInstanceState)
        binding.leaseCalRequest = viewModel.leaseCalculateReq
        Utils.setHideKeyboard(this, binding.root)

        viewModel.reqRepaymentCode(REPAYMENT_TERM)

        observeViewModel()
        initView()
        initEvent()
    }

    override fun handleSessionExpired(icon: Int, title: String, message: String, button: String) {
        super.handleSessionExpired(icon, title, message, button)
        systemDialog = SystemDialog.newInstance(icon, title, message, button)
        systemDialog.show(supportFragmentManager, systemDialog.tag)
        systemDialog.onConfirmClicked {
            RunTimeDataStore.LoginToken.value = ""
            startActivity(Intent(this, PinCodeActivity::class.java))
        }
    }

    private fun observeViewModel(){
        viewModel.leaseCalLiveData.observe(this, {
            Log.d(
                "nng",
                "leaseCalculateReq :: ${it.installments} :: ${it.monthly_repayment} :: ${it.total_interest_paid}"
            )
            val intent = Intent(this, CalculateResultActivity::class.java)
            intent.putExtra("leaseCalResponse", it)
            startActivity(intent)
        })
        viewModel.repaymentLiveData.observe(this, {
            repaymentCodes = it.codes!!
            viewModel.setUpRepaymentData(repaymentCodes)
        })
    }

    private fun initEvent(){
        binding.btnCalculate.setOnClickListener {
            if (binding.btnCalculate.isActive()){
                showLoading(true)
                viewModel.calculateLease()
            }
        }
        binding.llRepaymentTerm.setOnClickListener {
            listChoiceDialog = ListChoiceDialog.newInstance(
                R.drawable.ic_badge_general,
                getString(R.string.repayment_term),
                viewModel.repaymentData!!,
                selectedItem
            )
            listChoiceDialog.item = 5

            listChoiceDialog.setOnItemListener = { p: Int ->
                selectedItem = p
                val term = repaymentCodes[p].title
                binding.tvRepaymentTerm.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.color_263238
                    )
                )
                binding.tvRepaymentTerm.text = term
                viewModel.leaseCalculateReq.repayment_term = repaymentCodes[p].code!!.toInt()

                binding.btnCalculate.isEnable(
                    binding.edLeaseAmt.text.toString(),
                    binding.edRate.text.toString(),
                    term.toString()
                )
            }
            listChoiceDialog.isCancelable = true
            listChoiceDialog.show(supportFragmentManager, listChoiceDialog.tag)
        }

        binding.edLeaseAmt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                val term = if (viewModel.leaseCalculateReq.repayment_term != null) {
                    viewModel.leaseCalculateReq.repayment_term
                } else {
                    ""
                }
                binding.btnCalculate.isEnable(
                    s.toString(),
                    binding.edRate.text.toString(),
                    term.toString()
                )
            }

            override fun afterTextChanged(s: Editable) {
                viewModel.leaseCalculateReq.lease_amount = if (s.toString().isNotEmpty()) {
                    "LAK ".plus(s.toString())
                } else {
                    ""
                }
            }
        })

        binding.edRate.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            @SuppressLint("UseCompatLoadingForDrawables")
            override fun afterTextChanged(s: Editable?) {
                val value = s.toString()
                try {
                    binding.edRate.removeTextChangedListener(this)
                    val fixedValue: String = FormatUtil.getPercentageFormattedString(value)!!
                    val preSelection = binding.edRate.selectionEnd
                    s?.replace(0, value.length, fixedValue)
                    val selection = preSelection + fixedValue.length - value.length
//                    if (binding.edRate.selectionEnd == s?.length) {
//                        binding.edRate.setSelection(s.length - 1)
//                    } else {
//                        binding.edRate.setSelection(selection)
//                    }
                    binding.edRate.setSelection(selection)
                    if (s.toString().isNotEmpty()) viewModel.leaseCalculateReq.interest_rate =
                        s.toString().toInt()

                    val amount = if (viewModel.leaseCalculateReq.lease_amount != null) {
                        viewModel.leaseCalculateReq.lease_amount
                    } else {
                        ""
                    }
                    val term = if (viewModel.leaseCalculateReq.repayment_term != null) {
                        viewModel.leaseCalculateReq.repayment_term
                    } else {
                        ""
                    }

                    binding.btnCalculate.isEnable(
                        amount!!,
                        s.toString(),
                        term.toString()
                    )
                    binding.edRate.addTextChangedListener(this)
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }

            }
        })

        binding.edLeaseAmt.addTextChangedListener(NumberTextWatcherForThousand(binding.edLeaseAmt))

    }

    private fun initView(){
        binding.include.colToolbar.title = getString(R.string.product_calculate)
        binding.include.toolbarLeftButton.setOnClickListener {
            finish()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_lease_calculate
    }

    class NumberTextWatcherForThousand internal constructor(var editText: EditText) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) {
            val value = s.toString()
            try {
                editText.removeTextChangedListener(this)
                val fixedValue: String = FormatUtil.getDecimalFormattedString(value, false).toString()
                s.replace(0, value.length, fixedValue)
                editText.setSelection(Math.max(s.length, 0))
                editText.addTextChangedListener(this)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}