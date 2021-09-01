package com.mobile.bnkcl.ui.cscenter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bnkc.sourcemodule.base.BaseActivity
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.request.cscenter.ClaimDataRequest
import com.mobile.bnkcl.data.response.cscenter.ClaimItems
import com.mobile.bnkcl.data.response.faq.FaqItemsRes
import com.mobile.bnkcl.databinding.ActivityCSCenterBinding
import com.mobile.bnkcl.ui.adapter.FaqsAdapter
import com.mobile.bnkcl.ui.adapter.cscenter.AskQuestionAdapter
import com.mobile.bnkcl.ui.cscenter.viewmodel.CSCenterViewModel
import com.mobile.bnkcl.ui.cscenter.viewmodel.FaqsViewModel
import com.mobile.bnkcl.utilities.Utils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CSCenterActivity : BaseActivity<ActivityCSCenterBinding>(), View.OnClickListener {

    private val csCenterViewModel : CSCenterViewModel by viewModels()
    private val faqsViewModel:FaqsViewModel by viewModels()

    private lateinit var claimDataRequest : ClaimDataRequest
    private lateinit var claimItemsList : ArrayList<ClaimItems>
    private lateinit var faqItemsList : ArrayList<FaqItemsRes>
    private lateinit var collapseToolBarLayout : CollapsingToolbarLayout
    private var PAGE = 0
    private var isSending = false
    @Inject
    lateinit var adapter : AskQuestionAdapter

    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: LinearLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        claimDataRequest = ClaimDataRequest()
        claimItemsList = ArrayList()
        collapseToolBarLayout =binding.colToolbar

        adapter = AskQuestionAdapter()


        binding.toolbarLeftButton.setOnClickListener(this)

        initToolbar()
        initButton()
        observeClaimData()

        if (intent != null) {
            if (intent.getIntExtra("tab_index", 0) !== 0) {
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top)
                PAGE = 0
                claimItemsList.clear()
                getClaimData(PAGE, true)
                visibleAskBnk()
            } else {
                overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom)
                PAGE = 0
                visibleFaqs()
            }
        }

        binding.tvFaq.setOnClickListener(this)
        binding.tvAskBnk.setOnClickListener(this)
        binding.btnAskBnk.setOnClickListener(this)
        binding.nsvAsk.smoothScrollBy(0, 0)

        binding.swipeRefreshAskBnkc.setColorSchemeColors(ContextCompat.getColor(this,R.color.colorAccent))
        binding.swipeRefreshAskBnkc.setOnRefreshListener {
            PAGE = 0
            claimItemsList.clear()
            getClaimData(PAGE, false)
            visibleAskBnk()
            binding.swipeRefreshAskBnkc.isRefreshing = false
        }
    }

    private fun initToolbar(){
        collapseToolBarLayout.title = this.getString(R.string.cs_01)
        collapseToolBarLayout.setExpandedTitleTypeface(Utils.getTypeFace(this, 3))
        collapseToolBarLayout.setCollapsedTitleTypeface(Utils.getTypeFace(this, 3))

    }
    private fun getClaimData(page_no: Int, loading: Boolean){
            csCenterViewModel.getClaimData(page_no, loading)
    }

    private fun observeClaimData(){

        csCenterViewModel.claimLiveData.observe(this){
            Log.d(">>>","$it")
            adapter.addItemList(it.claims)
            isSending = false
        }
    }
    private fun initButton(){
        binding.btnAskBnk.setLabelButton(this.getString(R.string.cs_02))
        binding.btnAskBnk.setButtonBackGround(R.drawable.round_solid_d7191f_8)
        binding.btnAskBnk.setCheckButtonTextColor(true)
    }

    private fun visibleFaqs() {
//        binding.swipeRefreshAskBnkc.isRefreshing = false
//        binding.swipeRefreshAskBnkc.isEnabled = false
//        binding.llWrapAskBnk.visibility = View.GONE
        binding.tvTitle.text = this.getString(R.string.faqs_ask_question)
        binding.llWrapBtn.visibility = View.GONE
        binding.tvFaq.background = getDrawable(R.drawable.round_solid_d7191f_8)
        binding.tvAskBnk.background = getDrawable(R.drawable.round_solid_ffeeee)
        binding.tvFaq.setTextColor(ContextCompat.getColor(this, R.color.color_ffffff))
        binding.tvAskBnk.setTextColor(ContextCompat.getColor(this, R.color.color_d7191f))
        binding.rcQuestion.visibility = View.GONE
//        binding.rcQuestion.adapter = faqAdapter

    }

    private fun visibleAskBnk() {
        binding.nsvAsk.smoothScrollBy(0, 0)
//        binding.swipeRefreshAskBnkc.isEnabled = true
//        binding.llWrapAskBnk.visibility = View.VISIBLE
        binding.tvTitle.text = this.getString(R.string.center_ask_bnkc)
        binding.llWrapBtn.visibility = View.VISIBLE
        binding.tvFaq.background = getDrawable(R.drawable.round_solid_ffeeee)
        binding.tvAskBnk.background = getDrawable(R.drawable.round_solid_d7191f_8)
        binding.tvFaq.setTextColor(ContextCompat.getColor(this, R.color.color_d7191f))
        binding.tvAskBnk.setTextColor(ContextCompat.getColor(this, R.color.color_ffffff))
        binding.rcQuestion.visibility = View.VISIBLE
        binding.rcQuestion.adapter = adapter

        binding.rcQuestion.isNestedScrollingEnabled = false
        binding.nsvAsk.viewTreeObserver.addOnScrollChangedListener(ViewTreeObserver.OnScrollChangedListener {

                val view = binding.nsvAsk.getChildAt(binding.nsvAsk.childCount - 1) as View
                val diff = view.bottom - (binding.nsvAsk.height + binding.nsvAsk.scrollY)

                if (diff == 0) {
                    if (!csCenterViewModel.isLastPage()) {
                        if(isSending) return@OnScrollChangedListener

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

                visibleFaqs()
                binding.tvFaq.isClickable = false
                binding.tvAskBnk.isClickable = true

            }
            R.id.btn_ask_bnk -> {
                val intent = Intent(this, AskBNKCActivity::class.java)
                startActivity(intent)
            }
            R.id.toolbar_left_button -> onBackPressed()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_c_s_center
    }
}