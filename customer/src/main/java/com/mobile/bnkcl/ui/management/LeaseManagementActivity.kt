package com.mobile.bnkcl.ui.management

import android.os.Bundle
import android.util.Log
import com.bnkc.sourcemodule.base.BaseActivity
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityLeaseManagementBinding

class LeaseManagementActivity : BaseActivity<ActivityLeaseManagementBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(">>>", "onCreate: " + intent.getIntExtra("CONTACT_NO", 0))
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_lease_management
    }
}