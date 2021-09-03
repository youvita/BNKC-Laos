package com.mobile.bnkcl.ui.management.schedule

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.bnkc.sourcemodule.base.BaseActivity
import com.bnkc.sourcemodule.util.FormatUtils
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.request.lease.total_schedule.TotalLeaseScheduleRequest
import com.mobile.bnkcl.data.response.lease.total_lease_schedules.TotalLeaseScheduleData
import com.mobile.bnkcl.databinding.ActivityTotalLeaseScheduleBinding
import com.mobile.bnkcl.ui.adapter.TotalLeaseScheduleAdapter
import com.mobile.bnkcl.ui.dialog.SortDialog
import com.mobile.bnkcl.utilities.Utils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TotalLeaseScheduleActivity : BaseActivity<ActivityTotalLeaseScheduleBinding>(),
    View.OnClickListener {

    @Inject
    lateinit var totalLeaseScheduleAdapter: TotalLeaseScheduleAdapter

    private val viewModel: TotalLeaseScheduleViewModel by viewModels()
    private var CONTRACT_NO: String? = null
    private var sortCode: String? = "asc"
    private lateinit var totalLeaseScheduleRequest: TotalLeaseScheduleRequest

    override fun getLayoutId(): Int {
        return R.layout.activity_total_lease_schedule
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initToolbar()
        totalLeaseScheduleRequest = TotalLeaseScheduleRequest()

        if (intent != null) {
            CONTRACT_NO = intent.getStringExtra("CONTRACT_NO") as String
            totalLeaseScheduleRequest.contract_no = CONTRACT_NO
            totalLeaseScheduleRequest.payment_date_dir = "asc"
            viewModel.getTotalLeaseSchedule(totalLeaseScheduleRequest)
            showLoading()
        }

        initLiveData()

        binding.segmentButton.setOnPositionChangedListener {
            if (it == 0) {
                loadWebViewContent(true)
            } else {
                loadWebViewContent(false)
            }
        }

    }

    private fun initLiveData() {
        viewModel.totalLeaseScheduleLiveData.observe(this) {
            initAdapter(it.totalLeaseScheduleData!!)
            successListener()
            binding.tvTotalInterest.text = FormatUtils.getNumberFormat(this, it.totalInterest!!)
            binding.tvTotalPrincipal.text = FormatUtils.getNumberFormat(this, it.totalPrincipal!!)
        }
    }

    private fun initToolbar() {
        binding.collToolbar.setExpandedTitleTypeface(Utils.getTypeFace(this, 3))
        binding.collToolbar.setCollapsedTitleTypeface(Utils.getTypeFace(this, 3))
        binding.toolbarLeftButton.setOnClickListener(this)
        binding.tvSort.setOnClickListener(this)
    }

    private fun loadWebViewContent(hide: Boolean) {
//        binding.llWebContainer.visibility = if (hide) View.GONE else View.VISIBLE
//        binding.totalLeaseScheduleRecyclerview.visibility = if (hide) View.VISIBLE else View.GONE
//        binding.llTotalInfo.visibility = if (hide) View.VISIBLE else View.GONE
//
//        if (binding.llWebContainer.visibility == View.VISIBLE) {
//            binding.webView.setInitialScale(1)
//        }
    }

    private fun initAdapter(totalLeaseScheduleList: List<TotalLeaseScheduleData>) {
        binding.totalLeaseScheduleRecyclerview.adapter = totalLeaseScheduleAdapter
        totalLeaseScheduleAdapter.clearItemList()
        totalLeaseScheduleAdapter.addItemList(totalLeaseScheduleList)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.toolbar_left_button -> {
                finish()
            }
            R.id.tv_sort -> {
                val sortDialog = SortDialog(sortCode!!)
                sortDialog.show(supportFragmentManager, sortDialog.tag)
                sortDialog.onDismissListener {
                    if (sortDialog.sortCode != null) {
                        totalLeaseScheduleRequest.payment_date_dir = sortDialog.sortCode
                        sortCode = sortDialog.sortCode
                        viewModel.getTotalLeaseSchedule(totalLeaseScheduleRequest)
                        showLoading()
                        if (sortDialog.sortCode.toString() != "asc") {
                            binding.tvSort.text = getString(R.string.schedule_newest)
                        } else {
                            binding.tvSort.text = getString(R.string.schedule_oldest)
                        }
                    }
                }
            }
        }
    }
}