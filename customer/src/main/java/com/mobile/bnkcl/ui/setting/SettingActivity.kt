package com.mobile.bnkcl.ui.setting

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.base.BaseActivity
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.response.user.SettingData
import com.mobile.bnkcl.databinding.ActivitySettingBinding
import com.mobile.bnkcl.ui.home.HomeActivity
import com.mobile.bnkcl.ui.pinview.PinCodeActivity
import com.mobile.bnkcl.utilities.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingActivity : BaseActivity<ActivitySettingBinding>(), View.OnClickListener {

    private val settingViewModel: SettingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
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
                sharedPrefer.putPrefer(Constants.KEY_TOKEN, "")
                startActivity(Intent(this, PinCodeActivity::class.java))
            }
        }

        disposable = RxJava.listen(RxEvent.ServerError::class.java).subscribe {
            errorDialog(it.code, it.title, it.message)
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
            settingData.push_alarm_enabled = isChecked
            settingViewModel.settingData = settingData

            settingViewModel.updateUserSetting()
            showLoading()
        }

        binding.tvResetPin.setOnClickListener {
            val intent: Intent
            if (sharedPrefer.getPrefer(Constants.USER_ID)!!.isNotEmpty()) {
                intent = Intent(this, PinCodeActivity::class.java)
                intent.putExtra("owner", "setting")
                intent.putExtra("label", resources.getString(R.string.pin_05))
                intent.putExtra("RESETPIN", true)
            } else {
                intent = Intent(this, HomeActivity::class.java)
            }
            startActivity(intent)
        }
    }

    private fun initLiveData() {
        settingViewModel.userSettingLiveData.observe(this@SettingActivity) {
            Log.d("nng", "checked: ${settingViewModel.settingData!!.push_alarm_enabled}")
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