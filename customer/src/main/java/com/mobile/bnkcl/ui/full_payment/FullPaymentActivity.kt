package com.mobile.bnkcl.ui.full_payment

import android.os.Bundle
import com.bnkc.sourcemodule.base.BaseActivity
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityFullPaymentBinding

class FullPaymentActivity : BaseActivity<ActivityFullPaymentBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_full_payment
    }
}