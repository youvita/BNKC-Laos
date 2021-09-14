package com.mobile.bnkcl.ui.intro

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.lifecycle.observe
import com.bnkc.library.data.type.ErrorCode
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.library.util.LocaleHelper
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.base.BaseActivity
import com.bnkc.sourcemodule.dialog.ConfirmDialog
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.request.auth.DeviceInfo
import com.mobile.bnkcl.data.response.user.SettingData
import com.mobile.bnkcl.databinding.ActivityIntroBinding
import com.mobile.bnkcl.ui.home.HomeActivity
import com.mobile.bnkcl.ui.pinview.PinCodeActivity
import com.mobile.bnkcl.ui.setting.SettingViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class IntroActivity : BaseActivity<ActivityIntroBinding>() {

    private val introViewModel: IntroViewModel by viewModels()
    private val settingViewModel: SettingViewModel by viewModels()

    private lateinit var preLang: String

    override fun getLayoutId(): Int = R.layout.activity_intro

    @Inject
    lateinit var confirmDialog: ConfirmDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        setStatusBarColor(resources.getColor(R.color.color_ffffff))
//        setStatusBarTransparent(this, false)
        super.onCreate(savedInstanceState)

        initLanguage()
        initAppVersion()
        initDisposable()
        initImageLoadingRotate()
        getMGData()

        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result
                sharedPrefer.putPrefer(Constants.HandlePush.PUSH_ID, token!!)
                Log.d("nng: ", "token:: $token")
            })
    }

    private fun initLanguage() {
        preLang = sharedPrefer.getPrefer(Constants.LANGUAGE).toString()
        LocaleHelper.setLanguage(this, if ("" == preLang) "lo" else preLang)
    }


    private fun initDisposable() {
        disposable = RxJava.listen(RxEvent.SessionExpired::class.java).subscribe {
            errorSessionDialog(it.title, it.message).onConfirmClicked {
                sharedPrefer.putPrefer(Constants.KEY_TOKEN, "")
                startActivity(Intent(this, PinCodeActivity::class.java))
            }
        }

//        disposable = RxJava.listen(RxEvent.ServerError::class.java).subscribe {
//            errorDialog(it.code, it.title, it.message)
//        }

    }

    private fun initImageLoadingRotate() {
        val rotation = AnimationUtils.loadAnimation(this, R.anim.rotate_splash_loading)
        rotation.fillAfter = true

        binding.ivLoading.startAnimation(rotation)
    }

    private fun initAppVersion() {
        binding.appVersion = this.packageManager.getPackageInfo(this.packageName, 0).versionName
    }

    private fun getMGData() {
        try {
            introViewModel.getMGData()
            introViewModel.mgDataResponse.observe(this) {

                if (it != null) {
                    sharedPrefer.putPrefer(Constants.KEY_START_URL, it.c_start_url!!)
                }

                val availableService = it.c_available_service
                if (availableService!!) {

                    val appVer = getAppVersionName().trim()
                    val programVer = it.c_program_ver!!.trim()

                    if (validateAppVersion(appVer, programVer)) {

                        confirmDialog = ConfirmDialog.newInstance(
                            R.drawable.badge_new_version,
                            getString(R.string.dlg_new_version),
                            getString(R.string.dlg_update_desc),
                            getString(R.string.dlg_update_now)
                        )
                        confirmDialog.onConfirmClickedListener {
                            var marketUrl = it.c_appstore_url!!
                            if (marketUrl.isEmpty()) {
                                marketUrl = "market://details?id=$packageName"
                            }
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(marketUrl))
                            startActivity(intent)
                            finish()
                        }
                        confirmDialog.isCancelable = false
                        confirmDialog.show(supportFragmentManager, confirmDialog.tag)

                    } else {
                        if (it.c_act_yn!!) {
                            val serviceReason = it.c_act_msg
                            confirmDialog = ConfirmDialog.newInstance(
                                R.drawable.ic_badge_error,
                                getString(R.string.notice_01),
                                serviceReason!!,
                                getString(R.string.comm_confirm)
                            )
                            confirmDialog.onConfirmClickedListener {
//                                startApp()
                                if (sharedPrefer.getPrefer(Constants.USER_ID).isNullOrEmpty()) {
                                    sendPushID()
                                } else {
                                    startApp()
                                }
                            }
                            confirmDialog.isCancelable = false
                            confirmDialog.show(supportFragmentManager, confirmDialog.tag)
                        } else {
//                            startApp()
                            if (sharedPrefer.getPrefer(Constants.USER_ID).isNullOrEmpty()) {
                                sendPushID()
                            } else {
                                startApp()
                            }
                        }
                    }
                } else {
                    val serviceReason = it.c_act_msg
                    confirmDialog = ConfirmDialog.newInstance(
                        R.drawable.ic_badge_error,
                        getString(R.string.notice_01),
                        serviceReason!!,
                        getString(R.string.comm_confirm)
                    )
                    confirmDialog.onConfirmClickedListener {
                        finish()
                    }
                    confirmDialog.isCancelable = false
                    confirmDialog.show(supportFragmentManager, confirmDialog.tag)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Catch some error when MG down service
            val title = getString(R.string.comm_error)
            val message = getString(R.string.comm_error_during_process)
            RxJava.publish(RxEvent.ServerError(ErrorCode.SERVICE_ERROR, title, message))
        }
    }

    /**
     * nng:
     * send push id to server
     * in case user install the app and not yet login
     * sending user push notification without login
     */
    fun sendPushID(){
        val settingData = SettingData()
        val deviceInfo = DeviceInfo(sharedPrefer.getPrefer(Constants.HandlePush.PUSH_ID), "Android", Build.MODEL, Build.VERSION.SDK_INT.toString())
        settingData.push_alarm_enabled = true
        settingData.device_info = deviceInfo
        settingViewModel.settingData = settingData
        settingViewModel.updateUserSetting()
        settingViewModel.userSettingLiveData.observe(this@IntroActivity) {
            Log.d("nng", "checked: ${settingViewModel.settingData!!.push_alarm_enabled}")
            successListener()
            startApp()
        }
    }

    /**
     * start app
     * if auto login -> request login and then start main
     * else go -> login screen
     */
    fun startApp() {
        val user = sharedPrefer.getPrefer(Constants.USER_ID)
        Log.d(">>>>>>", "USER :: $user")
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    /**
     *  appVersion = 1.0.10 , validateVersion 1.0.10 => no update return false
     *  appVersion = 1.0.11 , validateVersion 1.0.11 => need update return true
     *  appVersion = 1.1.0  , validateVersion 1.0.10 => no update return false
     */
    fun validateAppVersion(appVersion: String, validateVersion: String): Boolean {
        try {
            val appVersions = appVersion.split(".")
            val valVersions = validateVersion.split(".")
            for (i in 1..valVersions.size) {
                val appVerDigit = appVersions[i].toInt()
                val valVerDigit = valVersions[i].toInt()
                if (appVerDigit > valVerDigit) {
                    return false
                }

                if (appVerDigit == valVerDigit) {
                    if (i == 2) {
                        return false
                    }
                } else {
                    return true
                }
            }
            return false
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    /**
     * get app version name
     */
    fun getAppVersionName(): String {
        val manager = application.packageManager
        val info: PackageInfo
        var strVersion = ""
        try {
            info = manager.getPackageInfo(application.packageName, 0)
            strVersion = info.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return strVersion
    }
}
