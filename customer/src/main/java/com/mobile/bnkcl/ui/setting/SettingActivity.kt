package com.mobile.bnkcl.ui.setting

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.bnkc.sourcemodule.base.BaseActivity
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivitySettingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingActivity : BaseActivity<ActivitySettingBinding>() {

    private val settingViewModel: SettingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initAppVersion()

        updateUserSetting()

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_setting
    }

    private fun initAppVersion() {
        binding.appVersion = this.packageManager.getPackageInfo(this.packageName, 0).versionName
    }

    private fun updateUserSetting() {
        settingViewModel.updateUserSetting()
        settingViewModel.userSettingLiveData.observe(this){
            Log.d("nng", "${it.push_alarm_enabled}")
        }
    }
}