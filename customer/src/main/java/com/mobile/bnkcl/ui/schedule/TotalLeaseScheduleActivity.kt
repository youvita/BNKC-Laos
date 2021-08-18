package com.mobile.bnkcl.ui.schedule

import android.os.Bundle
import android.view.View
import com.bnkc.sourcemodule.base.BaseActivity
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityTotalLeaseScheduleBinding
import com.mobile.bnkcl.ui.adapter.TotalLeaseScheduleAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TotalLeaseScheduleActivity : BaseActivity<ActivityTotalLeaseScheduleBinding>() {

    @Inject
    lateinit var totalLeaseScheduleAdapter: TotalLeaseScheduleAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_total_lease_schedule
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initAdapter()

        binding.segmentButton.setOnPositionChangedListener {
            if (it == 0) {
                loadWebViewContent(true)
            } else {
                loadWebViewContent(false)
            }
        }

    }

    private fun loadWebViewContent(hide: Boolean) {
        binding.llWebContainer.visibility = if (hide) View.GONE else View.VISIBLE
        binding.totalLeaseScheduleRecyclerview.visibility = if (hide) View.VISIBLE else View.GONE
        binding.llTotalInfo.visibility = if (hide) View.VISIBLE else View.GONE

        if (binding.llWebContainer.visibility == View.VISIBLE) {
            binding.webView.setInitialScale(1)
        }
    }

    private fun initAdapter() {
        val list = mutableListOf<CommentsItem>()

        val item = CommentsItem()
        item.postId = 1
        item.id = 1
        item.name = "Charity Plan"
        item.email = "email"
        item.body = "body"

        for (i in 0..14) {
            list.add(i, item)
        }

        binding.totalLeaseScheduleRecyclerview.adapter = totalLeaseScheduleAdapter
        totalLeaseScheduleAdapter.addItemList(list)
    }
}