package com.mobile.bnkcl.ui.lease.calculate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bnkc.sourcemodule.base.BaseActivity
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityLeaseCalculateBinding

class LeaseCalculateActivity : BaseActivity<ActivityLeaseCalculateBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lease_calculate)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_lease_calculate
    }
}