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
import androidx.lifecycle.observe
import com.bnkc.library.data.type.RunTimeDataStore
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.base.BaseActivity
import com.bnkc.sourcemodule.dialog.ListChoiceDialog
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityLeaseCalculateBinding
import com.mobile.bnkcl.ui.lease.calculate.result.CalculateResultActivity
import com.mobile.bnkcl.ui.pinview.PinCodeActivity
import com.mobile.bnkcl.utilities.FormatUtil
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.Disposable
import javax.inject.Inject

@AndroidEntryPoint
class LeaseCalculateActivity : BaseActivity<ActivityLeaseCalculateBinding>() {

    private val viewModel : LeaseCalculateViewModel by viewModels()
    private var selectedItem = -1

    @Inject
    lateinit var listChoiceDialog: ListChoiceDialog

    private val textInterestRateWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        @SuppressLint("UseCompatLoadingForDrawables")
        override fun afterTextChanged(s: Editable?) {
            if (s.toString().isNotEmpty()) viewModel.leaseCalculateReq.interest_rate = s.toString().toInt()
            binding.edRate.setSelection(binding.edRate.text!!.length)
            binding.btnCalculate.isEnable(
                viewModel.leaseCalculateReq.lease_amount.toString(),
                s.toString(),
                viewModel.leaseCalculateReq.repayment_term.toString()
            )
            val inputOtp =
                s.toString().trim { it <= ' ' }.replace("-".toRegex(), "")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setStatusBarColor(ContextCompat.getColor(this, R.color.color_f5f7fc))
        setAnimateType(Constants.ANIMATE_LEFT)
        super.onCreate(savedInstanceState)
        binding.leaseCalRequest = viewModel.leaseCalculateReq
        observeViewModel()
        initView()
        initEvent()
        checkError()
    }

    private var disposableError: Disposable? = null
    private fun checkError(){
        //Session expired
        disposableError = RxJava.listen(RxEvent.SessionExpired::class.java).subscribe{
            errorSessionDialog(it.title, it.message).onConfirmClicked {
                RunTimeDataStore.LoginToken.value = ""//clear token when session expired
                startActivity(Intent(this, PinCodeActivity::class.java))
            }
        }
    }

    private fun observeViewModel(){
        viewModel.leaseCalLiveData.observe(this, {
            Log.d("nng", "leaseCalculateReq :: ${it.installments} :: ${it.monthly_repayment} :: ${it.total_interest_paid}")
            val intent = Intent(this, CalculateResultActivity::class.java)
            intent.putExtra("leaseCalResponse", it)
            startActivity(intent)
        })
    }

    private fun initEvent(){
        binding.btnCalculate.setOnClickListener {
            if (binding.btnCalculate.isActive()){
                showLoading()
                viewModel.calculateLease()
            }
        }
        binding.llRepaymentTerm.setOnClickListener {
            listChoiceDialog = ListChoiceDialog.newInstance(
                R.drawable.ic_badge_error,
                getString(R.string.area),
                viewModel.setUpData(),
                selectedItem
            )
            listChoiceDialog.item = 5

            listChoiceDialog.setOnItemListener = {
                    p : Int ->
                selectedItem = p
                val term = viewModel.setUpData()[p]
                binding.tvRepaymentTerm.setTextColor(ContextCompat.getColor(this, R.color.color_263238))
                binding.tvRepaymentTerm.text = term
                viewModel.leaseCalculateReq.repayment_term = term.split(" ")[0].toInt()
                binding.btnCalculate.isEnable(
                    binding.edLeaseAmt.text.toString(),
                    binding.edRate.text.toString(),
                    term
                )
            }
            listChoiceDialog.isCancelable = true
            listChoiceDialog.show(supportFragmentManager, listChoiceDialog.tag)
        }

        binding.edLeaseAmt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                var term = if (viewModel.leaseCalculateReq.repayment_term != null){
                    viewModel.leaseCalculateReq.repayment_term
                }else{
                    ""
                }
                binding.btnCalculate.isEnable(
                    s.toString(),
                    binding.edRate.text.toString(),
                    term.toString()
                )
            }

            override fun afterTextChanged(s: Editable) {
                viewModel.leaseCalculateReq.lease_amount = if (s.toString().isNotEmpty()){
                    "LAK ".plus(s.toString())
                }else{
                    ""
                }
            }
        })

        binding.edLeaseAmt.addTextChangedListener(NumberTextWatcherForThousand(binding.edLeaseAmt))
    }

    private fun initView(){
        binding.include.colToolbar.title = getString(R.string.product_calculate)
        binding.edRate.addTextChangedListener(textInterestRateWatcher)
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
                val preSelection = editText.selectionEnd
                s.replace(0, value.length, fixedValue)
                val selection = preSelection + fixedValue.length - value.length
//            DevLog.devLog(">>>>>>>", ">>>>>>>>>>> :: " + s.length)
                editText.setSelection(Math.max(s.length, 0))
//            try {
//                if (editText === mBinding.edLoanAmount) { //_Auto generate of amount when user input amount in foreign currency and exchange rate
//                    viewModel.setLOAN_AMOUNT(s.toString().replace(",".toRegex(), ""))
//                } else if (editText === mBinding.edMonthlyIncome) {
//                    viewModel.setMONTHLY_INCOME(s.toString().replace(",".toRegex(), ""))
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
                editText.addTextChangedListener(this)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}