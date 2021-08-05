package com.mobile.bnkcl.ui.mobile_payment

import android.os.Bundle
import com.bnkc.sourcemodule.base.BaseActivity
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityMobilePaymentBinding

class MobilePaymentActivity : BaseActivity<ActivityMobilePaymentBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mobile_payment)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_mobile_payment
    }
}