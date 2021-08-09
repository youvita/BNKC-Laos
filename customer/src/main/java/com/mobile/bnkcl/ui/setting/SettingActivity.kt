package com.mobile.bnkcl.ui.setting

import android.annotation.SuppressLint
import android.os.Bundle
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivitySettingBinding
import com.bnkc.sourcemodule.base.BaseActivity

class SettingActivity : BaseActivity<ActivitySettingBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initAppVersion()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_setting
    }

    private fun initAppVersion() {
        binding.appVersion = this.packageManager.getPackageInfo(this.packageName, 0).versionName
    }
}