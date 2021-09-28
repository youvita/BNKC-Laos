package com.mobile.bnkcl.ui.management

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.bnkc.library.data.type.RunTimeDataStore
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.app.Constants.ANIMATE_LEFT
import com.bnkc.sourcemodule.base.BaseActivity
import com.bnkc.sourcemodule.dialog.ListChoiceDialog
import com.bnkc.sourcemodule.dialog.SystemDialog
import com.bnkc.sourcemodule.util.FormatUtils
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.response.code.CodesData
import com.mobile.bnkcl.databinding.ActivityLeaseManagementBinding
import com.mobile.bnkcl.ui.management.full_payment.FullPaymentActivity
import com.mobile.bnkcl.ui.management.history.TransactionHistoryActivity
import com.mobile.bnkcl.ui.management.schedule.TotalLeaseScheduleActivity
import com.mobile.bnkcl.ui.pinview.PinCodeActivity
import com.mobile.bnkcl.utilities.UtilAnimation
import com.mobile.bnkcl.utilities.Utils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LeaseManagementActivity : BaseActivity<ActivityLeaseManagementBinding>(),
    View.OnClickListener {

    @Inject
    lateinit var systemDialog: SystemDialog

    @Inject
    lateinit var listChoiceDialog: ListChoiceDialog

    private val viewModel: LeaseManagementViewModel by viewModels()
    private var contractNoList: ArrayList<String>? = ArrayList()
    private var productTypeList: ArrayList<CodesData>? = ArrayList()
    private var CONTRACT_NO: String? = ""
    private var REPAYMENT_DATE: String? = ""
    private var selectedIndex: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        setStatusBarColor(ContextCompat.getColor(this, R.color.color_f5f7fc))
        setAnimateType(ANIMATE_LEFT)
        super.onCreate(savedInstanceState)

        initView()
        initToolbar()
        initLiveData()

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

    override fun getLayoutId(): Int {
        return R.layout.activity_lease_management
    }

    private fun initLiveData() {
        viewModel.leaseLiveData.observe(this) {

            binding.leaseInfo.leaseInfo = it
            binding.comingLeaseRepayment.comingInfo = it
            REPAYMENT_DATE = it.repaymentDay

            binding.leaseInfo.tvRepaymentDate.text =
                getString(R.string.lease_every).plus(" ").plus(
                    FormatUtils.getFormatOnlyDate(
                        it.repaymentDay!!,
                        sharedPrefer.getPrefer(Constants.LANGUAGE).toString()
                    )
                )

            binding.leaseInfo.tvProductType.text = ""
            for (i in 0 until productTypeList!!.size) {
                if (it.productType.equals(productTypeList!![i].code, ignoreCase = true)) {
                    binding.leaseInfo.tvProductType.text = productTypeList!![i].title
                }
            }
            binding.comingLeaseRepayment.tvComingRepaymentAmount.text =
                FormatUtils.getNumberFormat(this, it.comingRepaymentAmount!!)

            val day: String = when {
                it.overduePenaltyDays == null || it.overduePenaltyDays == "" -> {
                    ""
                }
                it.overduePenaltyDays.toInt() == 1 -> {
                    getString(R.string.day)
                }
                else -> {
                    getString(R.string.days)
                }
            }

            binding.comingLeaseRepayment.tvOverduePenaltyTitle.text =
                getString(R.string.lease_overdue_penalty).plus(" (").plus(it.overduePenaltyDays)
                    .plus(" ").plus(day).plus(")")

            // for warning lease management
            val principal = it.overduePrincipal
            val interest = it.overdueInterest
            val penalty = it.overduePenalty
            if (!principal.isNullOrEmpty() || !interest.isNullOrEmpty() || !penalty.isNullOrEmpty()) {
                setUpRepaymentWithWarning(true)
            } else {
                setUpRepaymentWithWarning(false)
            }

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
            contractNoList = intent.getSerializableExtra("CONTRACT_NO_RECORD") as ArrayList<String>?
            productTypeList =
                intent.getSerializableExtra("PRODUCT_TYPE_LIST") as ArrayList<CodesData>?
            CONTRACT_NO = intent.getStringExtra("CONTRACT_NO") as String

            if (!sharedPrefer.getPrefer(Constants.USER_ID).isNullOrEmpty()) {
                viewModel.getLeaseInfo(CONTRACT_NO!!)
                showLoading(true)
            }

            for (i in 0 until contractNoList!!.size) {
                if (CONTRACT_NO.equals(contractNoList!![i])) {
                    selectedIndex = i
                }
            }

            binding.tvLeaseType.text = CONTRACT_NO
        }

        binding.leaseInfo.tvDisbursementDate.text = ""
        binding.leaseInfo.tvMaturityDate.text = ""
        binding.leaseInfo.tvRepaymentDate.text = ""
        binding.comingLeaseRepayment.tvOverduePenaltyTitle.text =
            getString(R.string.lease_overdue_penalty)

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
                listChoiceDialog = ListChoiceDialog.newInstance(
                    R.drawable.ic_badge_list_icon,
                    getString(R.string.lease_account),
                    contractNoList!!,
                    selectedIndex!!
                )
                listChoiceDialog.item = 5

                listChoiceDialog.setOnItemListener = {
                    viewModel.getLeaseInfo(contractNoList!![it])
                    showLoading(true)
                }
                listChoiceDialog.isCancelable = true
                listChoiceDialog.show(supportFragmentManager, listChoiceDialog.tag)
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