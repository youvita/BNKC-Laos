package com.mobile.bnkcl.ui.intro

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.animation.AnimationUtils
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityIntroBinding
import com.mobile.bnkcl.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroActivity : BaseActivity<ActivityIntroBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initAppVersion()

        initImageLoadingRotate()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_intro
    }

    private fun initImageLoadingRotate() {
        val rotation = AnimationUtils.loadAnimation(this, R.anim.rotate_splash_loading)
        rotation.fillAfter = true

        binding.ivLoading.startAnimation(rotation)
    }

    @SuppressLint("SetTextI18n")
    private fun initAppVersion() {
        binding.tvAppVersion.text = "Version " + this.packageManager.getPackageInfo(this.packageName, 0).versionName
    }
}