package com.mobile.bnkcl.ui.management

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.base.BaseActivity
import com.bnkc.sourcemodule.util.FormatUtils
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityLeaseManagementBinding
import com.mobile.bnkcl.ui.management.full_payment.FullPaymentActivity
import com.mobile.bnkcl.ui.management.history.TransactionHistoryActivity
import com.mobile.bnkcl.ui.management.schedule.TotalLeaseScheduleActivity
import com.mobile.bnkcl.ui.pinview.PinCodeActivity
import com.mobile.bnkcl.utilities.UtilAnimation
import com.mobile.bnkcl.utilities.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LeaseManagementActivity : BaseActivity<ActivityLeaseManagementBinding>(),
    View.OnClickListener {

    private val viewModel: LeaseManagementViewModel by viewModels()
    private var mLeaseArraylist: ArrayList<*>? = null
    private var CONTRACT_NO: String? = null
    private var REPAYMENT_DATE: String? = null
    private var isWarning: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setAnimateType(ANIMATE_LEFT)
        super.onCreate(savedInstanceState)

        initView()
        initToolbar()
        initDisposable()
        initLiveData()

    }

    private fun initDisposable() {

        disposable = RxJava.listen(RxEvent.SessionExpired::class.java).subscribe {
            errorSessionDialog(it.title, it.message).onConfirmClicked {
                sharedPrefer.putPrefer(Constants.KEY_TOKEN, "")
                startActivity(Intent(this, PinCodeActivity::class.java))
            }
        }

//        disposable = RxJava.listen(RxEvent.ServerError::class.java).subscribe {
//            errorDialog(it.code, it.title, it.message)
//        }

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_lease_management
    }

    private fun initLiveData() {
        viewModel.leaseLiveData.observe(this) {
            successListener()
            binding.leaseInfo.leaseInfo = it
            binding.comingLeaseRepayment.comingInfo = it
            REPAYMENT_DATE = it.repaymentDay
            binding.comingLeaseRepayment.tvComingRepaymentAmount.text =
                FormatUtils.getNumberFormat(this, it.comingRepaymentAmount!!)

            // for warning lease management
            isWarning = true
            setUpRepaymentWithWarning(isWarning!!)
        }
    }

    private fun setUpRepaymentWithWarning(isWarning: Boolean) {
        binding.comingLeaseRepayment.llWarningRepayment.visibility =
            if (isWarning) View.VISIBLE else View.GONE
        binding.comingLeaseRepayment.llWarningNote.visibility =
            if (isWarning) View.VISIBLE else View.GONE
    }

    private fun initView() {

        if (intent != null) {
            mLeaseArraylist = intent.getSerializableExtra("CONTRACT_NO_RECORD") as ArrayList<*>?
            CONTRACT_NO = intent.getStringExtra("CONTRACT_NO") as String

            if (!sharedPrefer.getPrefer(Constants.USER_ID).isNullOrEmpty()) {
                viewModel.getLeaseInfo(CONTRACT_NO!!)
                showLoading()
            }

            binding.tvLeaseType.text = CONTRACT_NO
        }

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
                // to do
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