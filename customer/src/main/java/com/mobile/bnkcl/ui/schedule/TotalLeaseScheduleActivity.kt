package com.mobile.bnkcl.ui.schedule

import android.os.Bundle
import com.bnkc.sourcemodule.base.BaseActivity
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityTotalLeaseScheduleBinding

class TotalLeaseScheduleActivity : BaseActivity<ActivityTotalLeaseScheduleBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_total_lease_schedule
    }
}