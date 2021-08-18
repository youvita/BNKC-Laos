package com.mobile.bnkcl.ui.intro

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.lifecycle.observe
import com.bnkc.library.data.type.ErrorCode
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.base.BaseActivity
import com.bnkc.sourcemodule.dialog.ConfirmDialog
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityIntroBinding
import com.mobile.bnkcl.ui.home.HomeActivity
import com.mobile.bnkcl.ui.pinview.PinCodeActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class IntroActivity : BaseActivity<ActivityIntroBinding>() {

    private val introViewModel: IntroViewModel by viewModels()

    override fun getLayoutId(): Int = R.layout.activity_intro

    @Inject
    lateinit var confirmDialog: ConfirmDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        setStatusBarColor(resources.getColor(R.color.color_ffffff))
//        setStatusBarTransparent(this, false)
        super.onCreate(savedInstanceState)

        initAppVersion()

        initImageLoadingRotate()

        getMGData()
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
                                getString(R.string.dlg_01),
                                getString(R.string.dlg_02),
                                getString(R.string.dlg_03)
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
                                startApp()
                            }
                            confirmDialog.isCancelable = false
                            confirmDialog.show(supportFragmentManager, confirmDialog.tag)
                        } else {
                            startApp()
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
        }catch (e: Exception) {
            e.printStackTrace()
            // Catch some error when MG down service
            val title   = getString(R.string.comm_error)
            val message = getString(R.string.comm_error_during_process)
            RxJava.publish(RxEvent.ServerError(ErrorCode.SERVICE_ERROR, title, message))
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
        if (user!!.isEmpty()) {
            startActivity(Intent(this, HomeActivity::class.java))
        } else {
//            val extrasLogin = ExtrasLogin(this)
//            extrasLogin.Param.setToolbar(getString(R.string.pin_01))
//            extrasLogin.Param.setOwner("login")
//            extrasLogin.Param.setLabel(getString(R.string.pin_04))

            val intent = Intent(this, PinCodeActivity::class.java)
            intent.putExtra("username", user)
            intent.putExtra("pin_action", "login")
//            intent.putExtras(extrasLogin.bundle!!)
            startActivity(intent)
        }
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
