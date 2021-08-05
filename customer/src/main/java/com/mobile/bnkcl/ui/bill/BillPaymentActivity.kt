package com.mobile.bnkcl.ui.bill

import android.os.Bundle
import com.bnkc.sourcemodule.base.BaseActivity
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityBillPaymentBinding

class BillPaymentActivity : BaseActivity<ActivityBillPaymentBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_bill_payment
    }
}