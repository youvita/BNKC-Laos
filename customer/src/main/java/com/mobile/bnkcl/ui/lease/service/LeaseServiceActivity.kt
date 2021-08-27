package com.mobile.bnkcl.ui.lease.service

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.bnkc.sourcemodule.base.BaseActivity
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityLeaseServiceBinding

class LeaseServiceActivity : BaseActivity<ActivityLeaseServiceBinding>() {

    val viewModel : LeaseServiceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setStatusBarColor(ContextCompat.getColor(this, R.color.color_f5f7fc))
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_lease_service
    }
}