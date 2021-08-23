package com.mobile.bnkcl.ui.setting

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
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
        setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
        super.onCreate(savedInstanceState)
        binding.activity = this

        initAppVersion()
        initView()
        initLiveData()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_setting
    }

    private fun initView() {
        if (intent.hasExtra("push_alarm_enabled")) {
            settingViewModel.push_notification_yn = intent.getStringExtra("push_alarm_enabled")
            binding.toggleUser.isChecked = settingViewModel.push_notification_yn.equals("Y")
        }
        binding.includedLayout.toolbarLeftButton.setOnClickListener{
            onBackPressed()
        }
        binding.toggleUser.setOnCheckedChangeListener { _, isChecked ->
            val settingData = SettingData()
            if (isChecked) settingData.push_alarm_enabled = "Y"
            else settingData.push_alarm_enabled = "N"
            settingViewModel.settingData = settingData
            settingViewModel.updateUserSetting()
        }
    }

    private fun initLiveData(){
        settingViewModel.userSettingLiveData.observe(this@SettingActivity) {
            Log.d("nng", "checked: ${settingViewModel.settingData!!.push_alarm_enabled}")
        }
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