package com.mobile.bnkcl.ui.signup

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.base.BaseActivity
import com.mobile.bnkcl.BuildConfig
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityTermsAndConditionsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TermsAndConditionsActivity : BaseActivity<ActivityTermsAndConditionsBinding>() {

    lateinit var url: String

    override fun getLayoutId(): Int = R.layout.activity_terms_and_conditions
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()
        initView()

    }

    private fun initView() {
        binding.ivBack.setOnClickListener{
            onBackPressed()
        }

        val websettings = binding.webview.settings
        websettings.javaScriptEnabled = true
        websettings.domStorageEnabled = true
        websettings.setAppCacheEnabled(false)
        websettings.cacheMode = WebSettings.LOAD_NO_CACHE
        websettings.loadsImagesAutomatically = true
        websettings.loadWithOverviewMode = true
        websettings.useWideViewPort = true
        websettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

        val header = mutableMapOf<String, String>()
        Log.d(">>>", "initView: "+ url)
//        header.put("Authorization", "Bearer ${sharedPrefer.getPrefer(Constants.LOGIN_TOKEN)}")
        header["Accept-Language"] = if(sharedPrefer.getPrefer(Constants.LANGUAGE).isNullOrEmpty()) "en" else sharedPrefer.getPrefer(Constants.LANGUAGE)!!

        binding.webview.loadUrl(sharedPrefer.getPrefer(Constants.KEY_START_URL) + url, header)
        binding.webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                view?.loadUrl(url)
                return true
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }
        }
    }

    private fun init() {
        if (intent != null) {

            if (intent.hasExtra(Constants.WEB_URL)) {
                url = intent.getStringExtra(Constants.WEB_URL)!!
            }

            if (intent.hasExtra(Constants.WEB_TITLE)) {
                binding.tvTitleToolbar.text = intent.getStringExtra(Constants.WEB_TITLE)
            }
        }
    }

}