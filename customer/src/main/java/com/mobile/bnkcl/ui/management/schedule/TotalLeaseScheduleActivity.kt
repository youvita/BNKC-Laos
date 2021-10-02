package com.mobile.bnkcl.ui.management.schedule

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.bnkc.library.data.type.ErrorCode
import com.bnkc.library.data.type.RunTimeDataStore
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.app.Constants.ANIMATE_LEFT
import com.bnkc.sourcemodule.base.BaseActivity
import com.bnkc.sourcemodule.dialog.SystemDialog
import com.bnkc.sourcemodule.util.FormatUtils
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.request.lease.total_schedule.TotalLeaseScheduleRequest
import com.mobile.bnkcl.data.response.lease.total_lease_schedules.TotalLeaseScheduleData
import com.mobile.bnkcl.databinding.ActivityTotalLeaseScheduleBinding
import com.mobile.bnkcl.ui.adapter.TotalLeaseScheduleAdapter
import com.mobile.bnkcl.ui.dialog.SortDialog
import com.mobile.bnkcl.ui.pinview.PinCodeActivity
import com.mobile.bnkcl.utilities.Utils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class TotalLeaseScheduleActivity : BaseActivity<ActivityTotalLeaseScheduleBinding>(),
    View.OnClickListener {

    @Inject
    lateinit var totalLeaseScheduleAdapter: TotalLeaseScheduleAdapter

    private val viewModel: TotalLeaseScheduleViewModel by viewModels()
    private var CONTRACT_NO: String? = null
    private var sortCode: String? = "asc"
    private var totalLeaseScheduleRequest: TotalLeaseScheduleRequest = TotalLeaseScheduleRequest()

    override fun onCreate(savedInstanceState: Bundle?) {
        setStatusBarColor(ContextCompat.getColor(this, R.color.color_f5f7fc))
        setAnimateType(ANIMATE_LEFT)
        super.onCreate(savedInstanceState)

        initToolbar()
        initView()
        initLiveData()
        handleError()

        if (!sharedPrefer.getPrefer(Constants.USER_ID).isNullOrEmpty()) {
            viewModel.getTotalLeaseSchedule(totalLeaseScheduleRequest)
            showLoading(true)
        }
    }

    private fun initView() {

        if (intent != null) {
            CONTRACT_NO = intent.getStringExtra("CONTRACT_NO") as String
            totalLeaseScheduleRequest.contract_no = CONTRACT_NO
            totalLeaseScheduleRequest.payment_date_dir = "asc"
        }

        binding.segmentButton.setOnPositionChangedListener {
            if (it == 0) {
                loadWebViewContent(true)
            } else {
                loadWebViewContent(false)
            }
        }

        binding.webView.setInitialScale(1)
        val webSettings = binding.webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true
        webSettings.builtInZoomControls = true
        binding.webView.overScrollMode = View.OVER_SCROLL_NEVER
        binding.webView.webChromeClient = object : WebChromeClient() {}
        binding.webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                val header = mutableMapOf<String, String>()
                header["Authorization"] = "Bearer " + RunTimeDataStore.LoginToken.value
                header["Accept-Language"] = if (sharedPrefer.getPrefer(Constants.LANGUAGE).isNullOrEmpty()) "en" else sharedPrefer.getPrefer(
                    Constants.LANGUAGE
                )!!
                header["Cookie"] = RunTimeDataStore.JsessionId.value
                view?.loadUrl(url!!, header)
                return true
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                val width = binding.webView.width
                val height = binding.webView.height
                if (width < height) {
                    val vc = binding.webView.layoutParams
                    vc.width = height
                    vc.height = width
                    binding.webView.requestLayout()
                    binding.webView.layoutParams = vc
                }

                var cookies = CookieManager.getInstance().getCookie(url)
                Log.d("nng", "Cookies:>>>> $cookies")
            }

            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest?
            ): WebResourceResponse? {
                request!!.requestHeaders
//                request.requestHeaders["authorization"] = "Bearer " + RunTimeDataStore.LoginToken.value
//                request.requestHeaders["Accept-Language"] = if (sharedPrefer.getPrefer(Constants.LANGUAGE).isNullOrEmpty()) "en" else sharedPrefer.getPrefer(
//                    Constants.LANGUAGE
//                )!!
//                request.requestHeaders["cookie"] = RunTimeDataStore.JsessionId.value
                return null
            }
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (0 != applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) {
                WebView.setWebContentsDebuggingEnabled(true)
            }
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            val cookieManager = CookieManager.getInstance()
            cookieManager.setAcceptCookie(true)
            cookieManager.setAcceptThirdPartyCookies(binding.webView, true)

        }
//        webSettings.domStorageEnabled = true
//        webSettings.setAppCacheEnabled(false)
//        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE
//        webSettings.loadsImagesAutomatically = true
//        webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_total_lease_schedule
    }

    private fun initLiveData() {
        viewModel.totalLeaseScheduleLiveData.observe(this) {
            initAdapter(it.totalLeaseScheduleData!!)
            binding.tvTotalInterest.text =
                FormatUtils.getNumberFormatWithoutCurrency(this, it.totalInterest!!)
            binding.tvTotalPrincipal.text =
                FormatUtils.getNumberFormatWithoutCurrency(this, it.totalPrincipal!!)
        }
    }

    private fun initToolbar() {
        binding.collToolbar.setExpandedTitleTypeface(Utils.getTypeFace(this, 3))
        binding.collToolbar.setCollapsedTitleTypeface(Utils.getTypeFace(this, 3))
        binding.toolbarLeftButton.setOnClickListener(this)
        binding.tvSort.setOnClickListener(this)
    }

    private fun loadWebViewContent(hide: Boolean) {

        binding.llWebContainer.visibility = if (hide) View.GONE else View.VISIBLE
        binding.totalLeaseScheduleRecyclerview.visibility = if (hide) View.VISIBLE else View.GONE
        binding.llTotalInfo.visibility = if (hide) View.VISIBLE else View.GONE
        if (!hide) binding.appBar.setExpanded(hide)

        if (binding.llWebContainer.visibility == View.VISIBLE) {
//            val url = RunTimeDataStore.BaseUrl.value + "/mobile/views/my-lease/$CONTRACT_NO/schedules?lang=lo&category=customer"
            val url = RunTimeDataStore.BaseUrl.value + "/mobile/views/my-lease/$CONTRACT_NO/schedules?category=customer"

//            val cookieManager = CookieManager.getInstance()
//            cookieManager.setAcceptCookie(true)
//            cookieManager.setAcceptThirdPartyCookies(binding.webView, true)

            val header = mutableMapOf<String, String>()
            header["Authorization"] = "Bearer " + RunTimeDataStore.LoginToken.value
            header["Accept-Language"] = if (sharedPrefer.getPrefer(Constants.LANGUAGE).isNullOrEmpty()) "en" else sharedPrefer.getPrefer(
                Constants.LANGUAGE
            )!!

//            val cookieList = cookieManager.getCookie(url).split(";")
//            cookieList.forEach{ item ->
//                cookieManager.setCookie(RunTimeDataStore.BaseUrl.value, item)
//            }
            header["Cookie"] = RunTimeDataStore.JsessionId.value

            Log.d("nng","Authorization:>>>Bearer ${RunTimeDataStore.LoginToken.value}")
            Log.d("nng","cookie:>>>${RunTimeDataStore.JsessionId.value}")

            binding.webView.loadUrl(url, header)
        }
    }

    private fun initAdapter(totalLeaseScheduleList: List<TotalLeaseScheduleData>) {
        binding.totalLeaseScheduleRecyclerview.adapter = totalLeaseScheduleAdapter
        totalLeaseScheduleAdapter.clearItemList()
        totalLeaseScheduleAdapter.addItemList(totalLeaseScheduleList)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.toolbar_left_button -> {
                finish()
            }
            R.id.tv_sort -> {
                val sortDialog = SortDialog(sortCode!!)
                sortDialog.show(supportFragmentManager, sortDialog.tag)
                sortDialog.onDismissListener {
                    if (sortDialog.sortCode != null) {
                        totalLeaseScheduleRequest.payment_date_dir = sortDialog.sortCode
                        sortCode = sortDialog.sortCode

                        if (!sharedPrefer.getPrefer(Constants.USER_ID).isNullOrEmpty()) {
                            viewModel.getTotalLeaseSchedule(totalLeaseScheduleRequest)
                            showLoading(true)
                        }

                        if (sortDialog.sortCode.toString() != "asc") {
                            binding.tvSort.text = getString(R.string.schedule_newest)
                        } else {
                            binding.tvSort.text = getString(R.string.schedule_oldest)
                        }
                    }
                }
            }
        }
    }

    /**
     * catch error
     */
    private fun handleError() {
        viewModel.handleError.observe(this) {
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