package com.mobile.bnkcl.ui.user

import android.os.Bundle
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityAccountInformationBinding
import com.mobile.bnkcl.ui.base.BaseActivity

class AccountInformationActivity : BaseActivity<ActivityAccountInformationBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_account_information
    }
}