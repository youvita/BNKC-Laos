package com.mobile.bnkcl.ui.lease.calculate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.observe
import com.bnkc.sourcemodule.base.BaseActivity
import com.bnkc.sourcemodule.dialog.ListChoiceDialog
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.request.findoffice.BranchRequest
import com.mobile.bnkcl.data.request.lease.calcculate.LeaseCalculateReq
import com.mobile.bnkcl.data.response.office.AreaDataResponse
import com.mobile.bnkcl.databinding.ActivityLeaseCalculateBinding
import com.mobile.bnkcl.ui.lease.calculate.result.CalculateResultActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LeaseCalculateActivity : BaseActivity<ActivityLeaseCalculateBinding>() {

    private val viewModel : LeaseCalculateViewModel by viewModels()
    private var selectedItem = 0

    @Inject
    lateinit var listChoiceDialog: ListChoiceDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        setStatusBarColor(ContextCompat.getColor(this, R.color.color_f5f7fc))
        super.onCreate(savedInstanceState)
        binding.leaseCalRequest = viewModel.leaseCalculateReq
        observeViewModel()
        initView()
        initEvent()

    }

    fun observeViewModel(){
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
                binding.tvRepaymentTerm.setTextColor(ContextCompat.getColor(this, R.color.color_263238))
                binding.tvRepaymentTerm.text = viewModel.setUpData()[p]
            }
            listChoiceDialog.isCancelable = true
            listChoiceDialog.show(supportFragmentManager, listChoiceDialog.tag)
        }
    }

    private fun initView(){
        binding.include.colToolbar.title = "Calculator"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_lease_calculate
    }

}