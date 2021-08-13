package com.mobile.bnkcl.ui.home

import android.content.Intent
import android.os.Bundle
import com.bnkc.library.util.LocaleHelper
import com.bnkc.sourcemodule.base.BaseActivity
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityHomeBinding
import com.mobile.bnkcl.ui.dialog.LanguageDialog
import com.mobile.bnkcl.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_home
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initLanguage()

        binding.clLease.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun initLanguage() {
        binding.clChangeLanguage.setOnClickListener {
            val languageDialog = LanguageDialog()
            languageDialog.show(supportFragmentManager, languageDialog.tag)

            languageDialog.onLangSelected {
                val context = LocaleHelper.setLocale(this, LocaleHelper.getLanguage(this))
                binding.tvCurrentLang.text = context.getString(R.string.home_04)
                binding.tvLoan.text = context.getString(R.string.home_01)
                binding.tvLease.text = context.getString(R.string.home_02)
                binding.tvLeaseEx.text = context.getString(R.string.home_03)

                if (LocaleHelper.getLanguage(this) == "en"){
                    binding.ivFlag.setImageResource(R.drawable.ic_avatar_l)
                } else {
                    binding.ivFlag.setImageResource(R.drawable.laos_flag)
                }

                sharedPrefer.putPrefer("lang", LocaleHelper.getLanguage(this))
            }
        }

    }
}