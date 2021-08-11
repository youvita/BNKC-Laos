package com.mobile.bnkcl.ui.home

import android.content.Intent
import android.os.Bundle
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityHomeBinding
import com.bnkc.sourcemodule.base.BaseActivity
import com.bnkc.sourcemodule.dialog.SystemDialog
import com.mobile.bnkcl.ui.dialog.LanguageDialog
import com.mobile.bnkcl.ui.main.MainActivity

class HomeActivity : BaseActivity<ActivityHomeBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.clChangeLanguage.setOnClickListener {
//            val dlgLanguage = DlgLanguage()
//            dlgLanguage.dlgLanguage(this,"la",true)

            val languageDialog = LanguageDialog()
            languageDialog.show(supportFragmentManager, languageDialog.tag)
        }

        binding.clLease.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_home
    }
}