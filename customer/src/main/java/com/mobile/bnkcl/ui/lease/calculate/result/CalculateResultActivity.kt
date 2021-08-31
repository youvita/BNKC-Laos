package com.mobile.bnkcl.ui.lease.calculate.result

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.bnkc.sourcemodule.base.BaseActivity
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityLeaseCalculateBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CalculateResultActivity : BaseActivity<ActivityLeaseCalculateBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setStatusBarColor(ContextCompat.getColor(this, R.color.color_f5f7fc))
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_calculate_result
    }
}