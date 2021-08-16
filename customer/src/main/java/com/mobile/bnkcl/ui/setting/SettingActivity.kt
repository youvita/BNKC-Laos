package com.mobile.bnkcl.ui.setting

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import com.bnkc.sourcemodule.base.BaseActivity
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.response.user.SettingData
import com.mobile.bnkcl.databinding.ActivitySettingBinding
import com.mobile.bnkcl.ui.pinview.PinCodeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingActivity : BaseActivity<ActivitySettingBinding>() {

    private val settingViewModel: SettingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.activity = this

        initAppVersion()

        settingViewModel.userSettingLiveData.observe(this@SettingActivity) {
            Log.d("nng", "checked: ${settingViewModel.settingData!!.push_alarm_enabled}")
        }
        binding.toggleUser.setOnCheckedChangeListener { _, isChecked ->
            val settingData = SettingData()
            if (isChecked) settingData.push_alarm_enabled = "Y"
            else settingData.push_alarm_enabled = "N"
            settingViewModel.settingData = settingData
            settingViewModel.updateUserSetting()
        }

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_setting
    }

    private fun initAppVersion() {
        binding.appVersion = this.packageManager.getPackageInfo(this.packageName, 0).versionName
    }

    fun goToResetPin() {
        val intent = Intent(this, PinCodeActivity::class.java)
        intent.putExtra("owner", "setting")
        intent.putExtra("label", resources.getString(R.string.pin_05))
        intent.putExtra("RESETPIN", true)
        startActivity(intent)
    }
}