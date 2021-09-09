package com.mobile.bnkcl.ui.management.schedule

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.viewModels
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.base.BaseActivity
import com.bnkc.sourcemodule.util.FormatUtils
import com.mobile.bnkcl.BuildConfig
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
        super.onCreate(savedInstanceState)

        initToolbar()
        initView()
        initDisposable()
        initLiveData()

        if (!sharedPrefer.getPrefer(Constants.USER_ID).isNullOrEmpty()) {
            viewModel.getTotalLeaseSchedule(totalLeaseScheduleRequest)
            showLoading()
        }
    }

    private fun initView() {

        if (intent != null) {
            CONTRACT_NO = intent.getStringExtra("CONTRACT_NO") as String
            Log.d(">>>", "initView: " + CONTRACT_NO)
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
        return R.layout.activity_total_lease_schedule
    }

    private fun initLiveData() {
        viewModel.totalLeaseScheduleLiveData.observe(this) {
            initAdapter(it.totalLeaseScheduleData!!)
            successListener()
            binding.tvTotalInterest.text = FormatUtils.getNumberFormat(this, it.totalInterest!!)
            binding.tvTotalPrincipal.text = FormatUtils.getNumberFormat(this, it.totalPrincipal!!)
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
            binding.webView.setInitialScale(1)
            val webSettings = binding.webView.settings
            webSettings.javaScriptEnabled = true
            webSettings.domStorageEnabled = true
            webSettings.setAppCacheEnabled(false)
            webSettings.cacheMode = WebSettings.LOAD_NO_CACHE
            webSettings.loadsImagesAutomatically = true
            webSettings.loadWithOverviewMode = true
            webSettings.useWideViewPort = true
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            val url = "/mobile/views/my-lease/$CONTRACT_NO/schedules?category=customer"

            val header = mutableMapOf<String, String>()
            header["Authorization"] = "Bearer " + sharedPrefer.getPrefer(Constants.KEY_TOKEN)
            header["Accept-Language"] = if (sharedPrefer.getPrefer(Constants.LANGUAGE)
                    .isNullOrEmpty()
            ) "lo" else sharedPrefer.getPrefer(Constants.LANGUAGE)!!

            binding.webView.loadUrl(BuildConfig.BASE_URL + url, header)
            binding.webView.webViewClient = object : WebViewClient() {
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
                            showLoading()
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
}