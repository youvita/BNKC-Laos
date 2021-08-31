package com.mobile.bnkcl.ui.lease.calculate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.bnkc.sourcemodule.base.BaseActivity
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityLeaseCalculateBinding
import com.mobile.bnkcl.ui.lease.calculate.result.CalculateResultActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LeaseCalculateActivity : BaseActivity<ActivityLeaseCalculateBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setStatusBarColor(ContextCompat.getColor(this, R.color.color_f5f7fc))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lease_calculate)

        initView()
        initEvent()

    }

    private fun initEvent(){
        binding.btnCalculate.setOnClickListener {
            startActivity(Intent(this, CalculateResultActivity::class.java))
        }
    }

    private fun initView(){
        binding.include.colToolbar.title = "Calculator"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_lease_calculate
    }
}