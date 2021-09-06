package com.mobile.bnkcl.ui.home

import android.content.Intent
import android.os.Bundle
import com.bnkc.sourcemodule.base.BaseActivity
import com.bnkcl.employeemodule.ui.EmployeeActivity
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityHomeBinding
import com.mobile.bnkcl.ui.dialog.LanguageDialog
import com.mobile.bnkcl.ui.lease.apply.ApplyLeaseActivity
import com.mobile.bnkcl.ui.main.MainActivity
import com.mobile.bnkcl.ui.success.ResultActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_home
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initLanguage()

        binding.localeCode = Locale.getDefault().language

        binding.clLease.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
//            intent.putExtra("from", ApplyLeaseActivity::class.java.simpleName)
//            intent.putExtra("result", true)
            startActivity(intent)
        }
    }

    private fun initLanguage() {
        binding.clChangeLanguage.setOnClickListener {
            val languageDialog = LanguageDialog()
            languageDialog.show(supportFragmentManager, languageDialog.tag)
        }
    }
}