package com.mobile.bnkcl.ui.setting

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.bnkc.library.data.type.AppLogin
import com.bnkc.library.data.type.RunTimeDataStore
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.app.Constants.ANIMATE_NORMAL
import com.bnkc.sourcemodule.base.BaseActivity
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.request.auth.DeviceInfo
import com.mobile.bnkcl.data.response.user.SettingData
import com.mobile.bnkcl.databinding.ActivitySettingBinding
import com.mobile.bnkcl.ui.home.HomeActivity
import com.mobile.bnkcl.ui.lease.apply.ApplyLeaseActivity
import com.mobile.bnkcl.ui.lease.service.LeaseServiceActivity
import com.mobile.bnkcl.ui.otp.OtpActivity
import com.mobile.bnkcl.ui.pinview.PinCodeActivity
import com.mobile.bnkcl.utilities.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingActivity : BaseActivity<ActivitySettingBinding>(), View.OnClickListener {

    private val settingViewModel: SettingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setStatusBarColor(ContextCompat.getColor(this, R.color.color_f5f7fc))
        setAnimateType(ANIMATE_NORMAL)
        super.onCreate(savedInstanceState)

        initToolbar()
        initAppVersion()
        initView()
        initDisposable()
        initLiveData()

    }

    private fun initDisposable() {
        disposable = RxJava.listen(RxEvent.SessionExpired::class.java).subscribe {
            errorSessionDialog(it.title, it.message).onConfirmClicked {
                RunTimeDataStore.LoginToken.value = ""
                startActivity(Intent(this, PinCodeActivity::class.java))
            }
        }

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_setting
    }

    private fun initToolbar() {
        setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
        binding.collToolbar.setExpandedTitleTypeface(Utils.getTypeFace(this, 3))
        binding.collToolbar.setCollapsedTitleTypeface(Utils.getTypeFace(this, 3))
        binding.toolbarLeftButton.setOnClickListener(this)
    }

    private fun initView() {
        binding.activity = this

        if (intent.hasExtra("push_alarm_enabled")) {
            settingViewModel.push_notification_yn = intent.getBooleanExtra("push_alarm_enabled", false)
            binding.toggleUser.isChecked = settingViewModel.push_notification_yn!!
        }

        binding.toggleUser.setOnCheckedChangeListener { _, isChecked ->
            val settingData = SettingData()
            val deviceInfo = DeviceInfo(sharedPrefer.getPrefer(Constants.HandlePush.PUSH_ID), "Android", Build.MODEL, Build.VERSION.SDK_INT.toString())
            settingData.push_alarm_enabled = isChecked
            settingData.device_info = deviceInfo
            settingViewModel.settingData = settingData

            settingViewModel.updateUserSetting()
            showLoading()
        }

        binding.tvResetPin.setOnClickListener {
            if (AppLogin.PIN.code == "N"){
                if (sharedPrefer.getPrefer(Constants.USER_ID).isNullOrEmpty()){
                    startActivity(Intent(this, OtpActivity::class.java))
                }else{
                    goToResetIfAlreadyLogin(false)
                }
            }else{
                goToResetIfAlreadyLogin(true)
            }
        }
    }

    private fun goToResetIfAlreadyLogin(isAlreadyLogin : Boolean){
        val intent = Intent(this, PinCodeActivity::class.java)
        intent.putExtra("from", SettingActivity::class.java.simpleName)
        intent.putExtra("username", sharedPrefer.getPrefer(Constants.USER_ID))
        val pinAction = if (isAlreadyLogin) "reset_pin" else "login"
        intent.putExtra("pin_action", pinAction)
        startActivity(intent)
    }

    private fun initLiveData() {
        settingViewModel.userSettingLiveData.observe(this@SettingActivity) {
            Log.d("nng", "checked: ${settingViewModel.settingData!!.push_alarm_enabled}")
            sharedPrefer.putPrefer(Constants.Push.PUSH_ALARM, if (settingViewModel.settingData!!.push_alarm_enabled!!) "Y" else "N")
            successListener()
        }
    }

    private fun initAppVersion() {
        binding.appVersion = this.packageManager.getPackageInfo(this.packageName, 0).versionName
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.toolbar_left_button -> {
                finish()
            }
        }
    }
}