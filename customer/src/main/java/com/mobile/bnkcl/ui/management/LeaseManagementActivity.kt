package com.mobile.bnkcl.ui.management

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import com.bnkc.sourcemodule.base.BaseActivity
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityLeaseManagementBinding
import com.mobile.bnkcl.ui.full_payment.FullPaymentActivity
import com.mobile.bnkcl.ui.history.TransactionHistoryActivity
import com.mobile.bnkcl.ui.schedule.TotalLeaseScheduleActivity
import com.mobile.bnkcl.utilities.UtilAnimation
import com.mobile.bnkcl.utilities.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LeaseManagementActivity : BaseActivity<ActivityLeaseManagementBinding>(),
    View.OnClickListener {

    private val viewModel: LeaseManagementViewModel by viewModels()
    private var CONTRACT_NO_RECORD: ArrayList<*>? = null
    private var CONTRACT_NO: String? = null
    private var REPAYMENT_DATE: String? = null
    private var isWarning: Boolean? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_lease_management
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initToolbar()
        setUpClickListener()

        if (intent != null) {
            CONTRACT_NO_RECORD = intent.getSerializableExtra("CONTRACT_NO_RECORD") as ArrayList<*>?
            CONTRACT_NO = intent.getStringExtra("CONTRACT_NO") as String

            Log.d(">>>", "onCreate: " + CONTRACT_NO_RECORD!!.size + " -- " + CONTRACT_NO)
        }

        viewModel.getLeaseInfo(CONTRACT_NO!!)
        viewModel.leaseLiveData.observe(this) {
            binding.leaseInfo.leaseInfo = it.leaseInfoData
            binding.comingLeaseRepayment.comingInfo = it.comingRepaymentInfoData
            binding.comingLeaseRepayment.tvComingRepaymentAmount.text = it.comingRepaymentAmount
            isWarning = true
            setUpRepaymentWithWarning(isWarning!!)
            REPAYMENT_DATE = it.leaseInfoData!!.repaymentDate
        }
    }

    private fun setUpRepaymentWithWarning(isWarning: Boolean) {
        binding.comingLeaseRepayment.llWarningRepayment.visibility = if (isWarning) View.VISIBLE else View.GONE
        binding.comingLeaseRepayment.llWarningNote.visibility = if (isWarning) View.VISIBLE else View.GONE
    }

    private fun setUpClickListener() {
        binding.btnTotalSchedule.setOnClickListener(this)
        binding.btnTransactionHistory.setOnClickListener(this)
        binding.leaseInfo.llLeaseInfo.setOnClickListener(this)
        binding.comingLeaseRepayment.llComingRepayment.setOnClickListener(this)
        binding.btnCheckFullpayment.setOnClickListener(this)
        binding.llFilterLease.setOnClickListener(this)

        binding.comingLeaseRepayment.llComingRepaymentExpand.visibility = View.GONE
        binding.comingLeaseRepayment.imgExpandColl.setImageResource(R.drawable.ic_unfold_ico)

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
            R.id.btn_total_schedule -> {
                val intentLeaseSchedule = Intent(this, TotalLeaseScheduleActivity::class.java)
                intentLeaseSchedule.putExtra("CONTRACT_NO", CONTRACT_NO)
                startActivity(intentLeaseSchedule)
            }
            R.id.btn_transaction_history -> {
                val intent = Intent(this, TransactionHistoryActivity::class.java)
                intent.putExtra("CONTRACT_NO", CONTRACT_NO)
                startActivity(intent)
            }
            R.id.ll_lease_info -> {
                val isExpandLoanInfo =
                    binding.leaseInfo.llLeaseInfoExpand.visibility == View.VISIBLE
                binding.leaseInfo.ivFold.setImageDrawable(
                    if (isExpandLoanInfo) {
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.ic_unfold_ico, null
                        )
                    } else {
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_fold_ico, null)
                    }
                )
                UtilAnimation.expandCollpaseAnimation(
                    binding.leaseInfo.llLeaseInfoExpand,
                    300,
                    isExpandLoanInfo
                )
            }
            R.id.ll_coming_repayment -> {
                val isExpandRepayment =
                    binding.comingLeaseRepayment.llComingRepaymentExpand.visibility == View.VISIBLE
                binding.comingLeaseRepayment.imgExpandColl.setImageResource(if (isExpandRepayment) R.drawable.ic_unfold_ico else R.drawable.ic_fold_ico)
                UtilAnimation.expandCollpaseAnimation(
                    binding.comingLeaseRepayment.llComingRepaymentExpand,
                    300,
                    isExpandRepayment
                )
            }
            R.id.ll_filter_lease -> {
                finish()
            }
            R.id.btn_check_fullpayment -> {
                val openFullPayment = Intent(this, FullPaymentActivity::class.java)
                openFullPayment.putExtra("REPAYMENT_DATE", REPAYMENT_DATE)
                openFullPayment.putExtra("CONTRACT_NO", CONTRACT_NO)
                startActivity(openFullPayment)
            }
        }
    }

}