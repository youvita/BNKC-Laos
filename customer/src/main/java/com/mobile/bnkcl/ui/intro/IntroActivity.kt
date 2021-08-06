package com.mobile.bnkcl.ui.intro

import android.view.animation.AnimationUtils
import android.os.Bundle
import androidx.activity.viewModels
import com.bnkc.sourcemodule.app.Constants
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityIntroBinding
import com.bnkc.sourcemodule.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroActivity : BaseActivity<ActivityIntroBinding>() {

    private val introViewModel: IntroViewModel by viewModels()

    override fun getLayoutId(): Int = R.layout.activity_intro

    override fun onCreate(savedInstanceState: Bundle?) {
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
        introViewModel.getMGData()
        introViewModel.mgData.observe(this) {
            if (it != null)
            sharedPrefer.putPrefer(Constants.KEY_BASE_URL, it.cStartUrl!!)
        }
    }
}
