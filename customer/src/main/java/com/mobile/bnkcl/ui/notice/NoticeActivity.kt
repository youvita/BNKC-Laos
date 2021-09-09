package com.mobile.bnkcl.ui.notice

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.viewModels
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.base.BaseActivity
import com.mobile.bnkcl.BuildConfig
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.request.notice.NoticeRequest
import com.mobile.bnkcl.databinding.ActivityNoticeBinding
import com.mobile.bnkcl.ui.adapter.NoticeAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NoticeActivity : BaseActivity<ActivityNoticeBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_notice
    private val noticeViewModel:NoticeViewModel by viewModels()

    private lateinit var noticeRequest: NoticeRequest
    @Inject
    lateinit var noticeAdapter: NoticeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



//        noticeRequest = NoticeRequest()

//        getNoticeData()

        initView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initView() {

        binding.toolbarLeftButton.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })

        val webSettings = binding.wbNotice.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.setAppCacheEnabled(true)
        webSettings.loadsImagesAutomatically = true
        webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        binding.wbNotice.webViewClient = object : WebViewClient(){
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url!!)
                return true
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                view!!.loadUrl("javascript:window.HTMLOUT.processHTML('<body>'+document.getElementsByTagName('html')[0].innerHTML+'</body>');")
            }
        }

        val header = mutableMapOf<String, String>()
        header["Authorization"] = "Bearer ${sharedPrefer.getPrefer(Constants.KEY_TOKEN)}"
        header["Accept-Language"] = if (sharedPrefer.getPrefer(Constants.LANGUAGE).isNullOrEmpty()) "en" else sharedPrefer.getPrefer(Constants.LANGUAGE)!!

        binding.wbNotice.loadUrl(BuildConfig.BASE_URL + Constants.WB_NOTICES, header)
    }

    private fun getNoticeData() {
        try {
            noticeViewModel.getNoticeData(noticeRequest)
            noticeViewModel.notice.observe(this){
                if (it != null) {
                    Log.d("zimah", "getPreSignUpData: ${it.notices}")
                }
            }

        }catch (e: Exception){
            e.printStackTrace()
        }
    }

}