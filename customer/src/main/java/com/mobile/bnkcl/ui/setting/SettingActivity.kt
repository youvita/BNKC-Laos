package com.mobile.bnkcl.ui.setting

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.bnkc.library.data.type.AppLogin
import com.bnkc.library.data.type.ErrorCode
import com.bnkc.library.data.type.RunTimeDataStore
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.app.Constants.ANIMATE_NORMAL
import com.bnkc.sourcemodule.base.BaseActivity
import com.bnkc.sourcemodule.dialog.SystemDialog
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.request.auth.DeviceInfo
import com.mobile.bnkcl.data.response.user.SettingData
import com.mobile.bnkcl.databinding.ActivitySettingBinding
import com.mobile.bnkcl.ui.otp.OtpActivity
import com.mobile.bnkcl.ui.pinview.PinCodeActivity
import com.mobile.bnkcl.utilities.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingActivity : BaseActivity<ActivitySettingBinding>(), View.OnClickListener {

    private val settingViewModel: SettingViewModel by viewModels()
    private var isUpdateProfile: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        setStatusBarColor(ContextCompat.getColor(this, R.color.color_f5f7fc))
        setAnimateType(ANIMATE_NORMAL)
        super.onCreate(savedInstanceState)

        initToolbar()
        initAppVersion()
        initView()
        initLiveData()
        handleError()
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

        if (sharedPrefer.getPrefer(Constants.USER_ID)?.isEmpty()!!) {
            binding.toggleUser.isChecked = if (sharedPrefer.getPrefer("is_enable")!!.isEmpty()
            ) true else sharedPrefer.getPrefer("is_enable").toBoolean()
        }
        if (AppLogin.PIN.code == "N" && sharedPrefer.getPrefer(Constants.USER_ID)!!.isNotEmpty()) {
            binding.toggleUser.isChecked = if (sharedPrefer.getPrefer("is_enable")!!.isEmpty()
            ) true else sharedPrefer.getPrefer("is_enable").toBoolean()
        }

        if (intent.hasExtra("push_alarm_enabled")) {
            settingViewModel.push_notification_yn =
                intent.getBooleanExtra("push_alarm_enabled", false)
            binding.toggleUser.isChecked = settingViewModel.push_notification_yn!!
        }

        if (AppLogin.PIN.code == "N") {
            binding.tvResetPin.isEnabled = false
            binding.tvResetPin.setTextColor(ContextCompat.getColor(this, R.color.color_90A4AE))
            binding.ivResetPinMore.setColorFilter(
                ContextCompat.getColor(
                    this,
                    R.color.color_90A4AE
                )
            )
        }

        binding.toggleUser.setOnCheckedChangeListener { _, isChecked ->
            val settingData = SettingData()
            val deviceInfo = DeviceInfo(
                RunTimeDataStore.PushId.value,
                "Android",
                Build.MODEL,
                Build.VERSION.SDK_INT.toString()
            )
            settingData.push_alarm_enabled = isChecked
            settingData.device_info = deviceInfo
            settingViewModel.settingData = settingData

            settingViewModel.updateUserSetting()
            showLoading(true)
        }

        binding.tvResetPin.setOnClickListener {
            if (AppLogin.PIN.code == "N") {
                if (sharedPrefer.getPrefer(Constants.USER_ID).isNullOrEmpty()) {
                    startActivity(Intent(this, OtpActivity::class.java))
                } else {
                    goToResetIfAlreadyLogin(false)
                }
            } else {
                goToResetIfAlreadyLogin(true)
            }
        }
    }

    private fun goToResetIfAlreadyLogin(isAlreadyLogin: Boolean) {
        val intent = Intent(this, PinCodeActivity::class.java)
        intent.putExtra("from", SettingActivity::class.java.simpleName)
        intent.putExtra("username", sharedPrefer.getPrefer(Constants.USER_ID))
        val pinAction = if (isAlreadyLogin) "reset_pin" else "login"
        intent.putExtra("pin_action", pinAction)
        startActivity(intent)
    }

    private fun initLiveData() {
        settingViewModel.userSettingLiveData.observe(this@SettingActivity) {
            sharedPrefer.putPrefer(
                Constants.Push.PUSH_ALARM,
                if (settingViewModel.settingData!!.push_alarm_enabled!!) "Y" else "N"
            )

            if (sharedPrefer.getPrefer(Constants.USER_ID)?.isEmpty()!! || AppLogin.PIN.code == "N"
            ) sharedPrefer.putPrefer(
                "is_enable",
                settingViewModel.settingData!!.push_alarm_enabled.toString()
            )

            isUpdateProfile = true
        }
    }

    private fun initAppVersion() {
        binding.appVersion = this.packageManager.getPackageInfo(this.packageName, 0).versionName
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra("IS_UPDATE_PROFILE", isUpdateProfile)
        if (isUpdateProfile)
            setResult(RESULT_OK, intent)
        finish()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.toolbar_left_button -> {
                onBackPressed()
            }
        }
    }

    /**
     * catch error
     */
    private fun handleError() {
        settingViewModel.handleError.observe(this) {
            val error = getErrorMessage(it)
            systemDialog = SystemDialog.newInstance(error.icon!!, error.code!!, error.message!!, error.button!!)
            systemDialog.show(supportFragmentManager, systemDialog.tag)
            systemDialog.onConfirmClicked {
                // session expired
                if (error.code == ErrorCode.UNAUTHORIZED) {
                    RunTimeDataStore.LoginToken.value = ""
                    startActivity(Intent(this, PinCodeActivity::class.java))
                    finish()
                }
            }
        }
    }
}