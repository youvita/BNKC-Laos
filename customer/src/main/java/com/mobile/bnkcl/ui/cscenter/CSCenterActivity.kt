package com.mobile.bnkcl.ui.cscenter

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.viewModels
import com.bnkc.sourcemodule.base.BaseActivity
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.request.cscenter.ClaimDataRequest
import com.mobile.bnkcl.data.response.cscenter.ClaimItems
import com.mobile.bnkcl.databinding.ActivityCSCenterBinding
import com.mobile.bnkcl.ui.adapter.cscenter.AskQuestionAdapter
import com.mobile.bnkcl.ui.cscenter.viewmodel.CSCenterViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CSCenterActivity : BaseActivity<ActivityCSCenterBinding>(), View.OnClickListener {

    override fun getLayoutId(): Int= R.layout.activity_c_s_center

    private val csCenterViewModel : CSCenterViewModel by viewModels()
    private lateinit var claimDataRequest : ClaimDataRequest
    private lateinit var claimItemsList : ArrayList<ClaimItems>
    private var PAGE = 0
    private var isSending = false
    @Inject
    lateinit var adapter : AskQuestionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        claimDataRequest = ClaimDataRequest()
        claimItemsList = ArrayList()

        adapter = AskQuestionAdapter()

        if (intent != null) {
            if (intent.getIntExtra("tab_index", 0) !== 0) {
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top)
                PAGE = 0
                claimItemsList.clear()
                getClaimData(PAGE, true)
                visibleAskBnk()
            } else {
                overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom)
                visibleWebView()
            }
        }

        binding.tvFaq.setOnClickListener(this)
        binding.tvAskBnk.setOnClickListener(this)
        binding.btnAskBnk.setOnClickListener(this)
        binding.nsvAsk.smoothScrollBy(0, 0)

        binding.swipeRefreshAskBnkc.setColorSchemeColors(resources.getColor(R.color.colorAccent))
        binding.swipeRefreshAskBnkc.setOnRefreshListener {
            PAGE = 0
            claimItemsList.clear()
            getClaimData(PAGE, false)
            visibleAskBnk()
            binding.swipeRefreshAskBnkc.isRefreshing = false
        }
    }

    private fun getClaimData(page_no: Int, loading: Boolean){
        try {
            csCenterViewModel.getClaimData(claimDataRequest)

            binding.rcQuestion.adapter = adapter

            csCenterViewModel.claimLiveData.observe(this){
                for (i in 0 until it.claims!!.size){

                }
                adapter.addItemList(it.claims)
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun visibleWebView() {
        binding.swipeRefreshAskBnkc.isRefreshing = false
        binding.swipeRefreshAskBnkc.isEnabled = false
        binding.llWrapAskBnk.visibility = View.GONE
        binding.wbFaq.visibility = View.VISIBLE
        binding.llWrapBtn.visibility = View.GONE
        binding.tvFaq.background = getDrawable(R.drawable.round_solid_d7191f_8)
        binding.tvAskBnk.background = getDrawable(R.drawable.round_solid_ffeeee)
        binding.tvFaq.setTextColor(resources.getColor(R.color.color_ffffff))
        binding.tvAskBnk.setTextColor(resources.getColor(R.color.color_d7191f))

    }

    private fun visibleAskBnk() {
        binding.nsvAsk.smoothScrollBy(0, 0)
        binding.swipeRefreshAskBnkc.isEnabled = true
        binding.llWrapAskBnk.visibility = View.VISIBLE
        binding.wbFaq.visibility = View.GONE
        binding.llWrapBtn.visibility = View.VISIBLE
        binding.tvFaq.background = getDrawable(R.drawable.round_solid_ffeeee)
        binding.tvAskBnk.background = getDrawable(R.drawable.round_solid_d7191f_8)
        binding.tvFaq.setTextColor(resources.getColor(R.color.color_d7191f))
        binding.tvAskBnk.setTextColor(resources.getColor(R.color.color_ffffff))
        binding.rcQuestion.isNestedScrollingEnabled = false
        binding.nsvAsk.viewTreeObserver.addOnScrollChangedListener(ViewTreeObserver.OnScrollChangedListener {
            val view = binding.nsvAsk.getChildAt(binding.nsvAsk.childCount - 1) as View
            val diff = view.bottom - (binding.nsvAsk.height + binding.nsvAsk
                    .scrollY)
            if (diff == 0) {
                if (!csCenterViewModel.isLastPage()) {
                    if (isSending) return@OnScrollChangedListener
                    ++PAGE
                    getClaimData(PAGE, true)
                    isSending = true
                }
            }
        })
    }


    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tv_ask_bnk -> {
                PAGE = 0
                claimItemsList.clear()
                getClaimData(PAGE, true)
                visibleAskBnk()
                binding.tvFaq.isClickable = true
                binding.tvAskBnk.isClickable = false

            }
            R.id.tv_faq -> {
                visibleWebView()
                binding.tvFaq.isClickable = false
                binding.tvAskBnk.isClickable = true

            }
            R.id.btn_ask_bnk -> {
                val intent = Intent(this, AskbnkcActivity::class.java)
                startActivity(intent)
            }
        }
    }
}