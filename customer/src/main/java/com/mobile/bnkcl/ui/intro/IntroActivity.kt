package com.mobile.bnkcl.ui.intro

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityIntroBinding
import com.mobile.bnkcl.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.Disposable

@AndroidEntryPoint
class IntroActivity : BaseActivity<ActivityIntroBinding>() {

    private val introViewModel: IntroViewModel by viewModels()

    private var introDisposable: Disposable? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        introDisposable = RxJava.listen(RxEvent.MGSuccess::class.java).subscribe(){
            Log.d("nng", "result: $it")
        }

        getMGData()


    }

    override fun getLayoutId(): Int {
        return R.layout.activity_intro
    }

    private fun getMGData() {
        introViewModel.getMGData()
        introViewModel.mgData.observe(this){
            Log.d("nng", "MG: $it")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        introDisposable?.dispose()
        introDisposable = null
    }
}