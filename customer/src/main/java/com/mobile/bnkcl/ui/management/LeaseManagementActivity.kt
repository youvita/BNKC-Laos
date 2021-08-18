package com.mobile.bnkcl.ui.management

import android.os.Bundle
import androidx.activity.viewModels
import com.bnkc.sourcemodule.base.BaseActivity
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityLeaseManagementBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LeaseManagementActivity : BaseActivity<ActivityLeaseManagementBinding>() {

    private val viewModel: LeaseManagementViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.gerLeaseInfo()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_lease_management
    }
}