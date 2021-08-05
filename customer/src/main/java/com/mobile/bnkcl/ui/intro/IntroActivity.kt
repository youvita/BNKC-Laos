package com.mobile.bnkcl.ui.intro

import android.annotation.SuppressLint
import android.view.animation.AnimationUtils
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityIntroBinding
import com.bnkc.sourcemodule.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.Disposable

@AndroidEntryPoint
class IntroActivity : BaseActivity<ActivityIntroBinding>() {

    private val introViewModel: IntroViewModel by viewModels()

    private var introDisposable: Disposable? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initAppVersion()

        initImageLoadingRotate()

        introDisposable = RxJava.listen(RxEvent.MGSuccess::class.java).subscribe() {
            Log.d("nng", "result: $it")
        }

        getMGData()


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
        binding.tvAppVersion.text =
            "Version " + this.packageManager.getPackageInfo(this.packageName, 0).versionName
    }

    private fun getMGData() {
        introViewModel.getMGData()
        introViewModel.mgData.observe(this) {
            Log.d("nng", "MG: $it")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        introDisposable?.dispose()
        introDisposable = null
    }
}
