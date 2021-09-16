package com.mobile.bnkcl.ui.cscenter

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.bnkc.library.data.type.RunTimeDataStore
import com.bnkc.library.data.type.Status
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.app.Constants.CATEGORY
import com.bnkc.sourcemodule.app.Constants.CLAIM_ID
import com.bnkc.sourcemodule.base.BaseActivity
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityCSCenterBinding
import com.mobile.bnkcl.ui.adapter.AskQuestionAdapter
import com.mobile.bnkcl.ui.cscenter.viewmodel.CSCenterViewModel
import com.mobile.bnkcl.ui.pinview.PinCodeActivity
import com.mobile.bnkcl.utilities.Utils
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.Disposable
import javax.inject.Inject

@AndroidEntryPoint
class CSCenterActivity : BaseActivity<ActivityCSCenterBinding>() {

    @Inject
    lateinit var adapter : AskQuestionAdapter

    private val csCenterViewModel : CSCenterViewModel by viewModels()
    private var collapseToolBarLayout : CollapsingToolbarLayout? = null
    private var pageNumber = 0
    private var totalPage: Int? = 0
    private var networkState: Status? = null

    override fun getLayoutId(): Int= R.layout.activity_c_s_center
    private var signUpDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        collapseToolBarLayout = binding.colToolbar

        binding.action = this@CSCenterActivity

        checkError()
        initToolbar()
        observeData()

        if (intent != null) {
            if (intent.getIntExtra("tab_index", 0) !== 0) {
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top)
                pageNumber = 0
                getClaimData(pageNumber, true)
                visibleAskBnk()
            } else {
                overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom)
                visibleWebView()
            }
        }

        binding.nsvAsk.smoothScrollBy(0, 0)

        binding.swipeRefreshAskBnkc.setColorSchemeColors(ContextCompat.getColor(this,R.color.colorAccent))
        binding.swipeRefreshAskBnkc.setOnRefreshListener {
            clearState()
            pageNumber = 0
            getClaimData(pageNumber, false)
            visibleAskBnk()
            binding.swipeRefreshAskBnkc.isRefreshing = false
        }

        binding.segmentButton.setOnPositionChangedListener {
            if (it == 0){
                onFaqClick()
            }else{
                onAskClick()

            }
        }
    }
    private fun checkError(){
        //Session expired
        signUpDisposable = RxJava.listen(RxEvent.SessionExpired::class.java).subscribe{
            errorSessionDialog(it.title, it.message).onConfirmClicked {
                RunTimeDataStore.LoginToken.value = ""//clear token when session expired
                startActivity(Intent(this, PinCodeActivity::class.java))
            }
        }
    }

    private fun initToolbar(){
        collapseToolBarLayout?.title = this.getString(R.string.cs_01)
        collapseToolBarLayout?.setExpandedTitleTypeface(Utils.getTypeFace(this, 3))
        collapseToolBarLayout?.setCollapsedTitleTypeface(Utils.getTypeFace(this, 3))

    }

    private fun getClaimData(page_no: Int, loading: Boolean){
        csCenterViewModel.getClaimData(page_no, loading)
    }

    private fun observeData(){
        binding.rcQuestion.adapter = adapter
        csCenterViewModel.claimLiveData.observe(this){
            if (it != null) {
                totalPage = it.data?.total_pages
                networkState = it.status

                adapter.addItemList(it.data?.claims)
                adapter.setOnItemClickListener { id, category ->
                    val intent = Intent(this, AskBNKCDetailActivity::class.java)
                    intent.putExtra(CLAIM_ID, id)
                    intent.putExtra(CATEGORY, category)
                    startActivity(intent)
                }
            }
        }
    }

    private fun visibleWebView() {
        binding.swipeRefreshAskBnkc.isRefreshing = false
        binding.swipeRefreshAskBnkc.isEnabled = false
        binding.llWrapAskBnk.visibility = View.GONE
        binding.wbFaq.visibility = View.VISIBLE
        binding.llWrapBtn.visibility = View.GONE
        val webSettings = binding.wbFaq.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.setAppCacheEnabled(true)
        webSettings.loadsImagesAutomatically = true
        webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        binding.wbFaq.webViewClient = object : WebViewClient(){
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url!!)
                return true
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                view?.loadUrl("javascript:window.HTMLOUT.processHTML('<body>'+document.getElementsByTagName('html')[0].innerHTML+'</body>');")
            }
        }

        val header = mutableMapOf<String, String>()
        header["Accept-Language"] = if (sharedPrefer.getPrefer(Constants.LANGUAGE).isNullOrEmpty()) "en" else sharedPrefer.getPrefer(Constants.LANGUAGE)!!
        binding.wbFaq.loadUrl(RunTimeDataStore.BaseUrl.value + Constants.WB_FAQS, header)
    }

    private fun visibleAskBnk() {
        binding.nsvAsk.smoothScrollBy(0, 0)
        binding.swipeRefreshAskBnkc.isEnabled = true
        binding.llWrapAskBnk.visibility = View.VISIBLE
        binding.wbFaq.visibility = View.GONE
        binding.llWrapBtn.visibility = View.VISIBLE
        binding.rcQuestion.isNestedScrollingEnabled = false
        binding.nsvAsk.viewTreeObserver.addOnScrollChangedListener(ViewTreeObserver.OnScrollChangedListener {

                val view = binding.nsvAsk.getChildAt(binding.nsvAsk.childCount - 1) as View
                val diff = view.bottom - (binding.nsvAsk.height + binding.nsvAsk.scrollY)

                if (diff == 0) {
                    if (totalPage == pageNumber) return@OnScrollChangedListener

                    if (networkState != Status.LOADING) {
                        ++pageNumber
                        getClaimData(pageNumber, true)
                    }
                    networkState = Status.LOADING
                }
        })
    }


    /**
     * binding method ask bank tab
     */
    fun onAskClick() {
        clearState()
        getClaimData(pageNumber, true)
        visibleAskBnk()
        binding.tvFaq.isClickable = true
        binding.tvAskBnk.isClickable = false
        binding.btnAskBnk.setActive(true)
    }

    /**
     * binding method faq tab
     */
    fun onFaqClick() {
        visibleWebView()
        binding.tvFaq.isClickable = false
        binding.tvAskBnk.isClickable = true
    }

    /**
     * binding method ask bnk button
     */
    fun onAskBnkClick() {
        val intent = Intent(this, AskBNKCActivity::class.java)
        startActivity(intent)
    }

    /**
     * binding method toolbar button
     */
    fun onBackPress() {
        onBackPressed()
    }

    /**
     * clear status for ask bnkc tab
     */
    private fun clearState() {
        binding.rcQuestion.removeAllViews()
        adapter.clearItemList()
        totalPage  = 0
        pageNumber = 0
    }

    override fun onDestroy() {
        super.onDestroy()
        networkState = null
    }

}