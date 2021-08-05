package com.mobile.bnkcl.ui.home

import android.os.Bundle
import com.mobile.bnkcl.R
import com.mobile.bnkcl.com.alert.DlgLanguage
import com.mobile.bnkcl.databinding.ActivityHomeBinding
import com.bnkc.sourcemodule.base.BaseActivity

class HomeActivity : BaseActivity<ActivityHomeBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.clChangeLanguage.setOnClickListener {

            val dlgLanguage = DlgLanguage()
            dlgLanguage.dlgLanguage(this,"la",true)

        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_home
    }
}