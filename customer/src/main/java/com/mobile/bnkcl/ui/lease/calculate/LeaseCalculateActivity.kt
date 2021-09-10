package com.mobile.bnkcl.ui.lease.calculate

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.IntegerRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.observe
import com.bnkc.sourcemodule.base.BaseActivity
import com.bnkc.sourcemodule.dialog.ListChoiceDialog
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.request.lease.calcculate.LeaseCalculateReq
import com.mobile.bnkcl.data.request.otp.OTPVerifyRequest
import com.mobile.bnkcl.data.response.office.AreaDataResponse
import com.mobile.bnkcl.databinding.ActivityLeaseCalculateBinding
import com.mobile.bnkcl.ui.lease.calculate.result.CalculateResultActivity
import dagger.hilt.android.AndroidEntryPoint
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
            val text: String = binding.edRate.text.toString()
            if (text.endsWith("-") || text.endsWith(" ")) return

//            binding.edRate.setText(
//                StringBuffer(text).insert(text.length - 1, "-").toString()
//            )
            viewModel.leaseCalculateReq.interest_rate = Integer.parseInt(text)
            binding.edRate.setSelection(binding.edRate.text!!.length)

            val inputOtp =
                s.toString().trim { it <= ' ' }.replace("-".toRegex(), "")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setStatusBarColor(ContextCompat.getColor(this, R.color.color_f5f7fc))
        super.onCreate(savedInstanceState)
        binding.leaseCalRequest = viewModel.leaseCalculateReq
        observeViewModel()
        initView()
        initEvent()

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
            showLoading()
            Log.d("nng", "leaseCalculateReq :: " + viewModel.leaseCalculateReq.lease_amount)
            viewModel.leaseCalculateReq = LeaseCalculateReq(
                "1000 USD",
                1,
                1,
            )
            viewModel.calculateLease()
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
                var term = viewModel.setUpData()[p]
                binding.tvRepaymentTerm.setTextColor(ContextCompat.getColor(this, R.color.color_263238))
                binding.tvRepaymentTerm.text = term
                viewModel.leaseCalculateReq.repayment_term = Integer.parseInt(term.split(" ")[0])
            }
            listChoiceDialog.isCancelable = true
            listChoiceDialog.show(supportFragmentManager, listChoiceDialog.tag)
        }
    }

    private fun initView(){
        binding.include.colToolbar.title = getString(R.string.calculate)
        binding.edRate.addTextChangedListener(textInterestRateWatcher)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_lease_calculate
    }

}