package com.mobile.bnkcl.ui.main.fragment.mypage

import android.os.Bundle
import android.text.Html
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.bnkc.sourcemodule.base.BaseFragment
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.MyLoan
import com.mobile.bnkcl.databinding.FragmentMyPageBinding
import java.util.*

class PageFragment : BaseFragment<FragmentMyPageBinding>(), LoanViewPagerAdapter.LoanPagerClickedListener{

    private var myLoanViewModel: PageViewModel? = null
    private var myLoanBinding: FragmentMyPageBinding? = null
    private var mLoanAdapter: LoanViewPagerAdapter? = null
    private var mBannerAdapter: BannerAdapter? = null
    private val mListener: MyLoanCardClickedListener? = null
    private val myLoanResObjs: MyLoan? = null
    private var mLoanItem: ArrayList<MyLoan>? = null
    private val mainPager: ViewPager? = null
    var callback: OnPageChangeCallback? = null
    var positionIndicator = 0
    var MLR001 = 0
    var MLR002:Int = 0
    var MLR003:Int = 0
    var MLR004 /*for record in each loan progress */:Int = 0
    var bannerArray: ArrayList<Int>? = null
    var timer: Timer? = null
    var page = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//
////        myLoanBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_page, container, false)
////
////        mLoanItem = ArrayList<MyLoan>()
////        mLoanAdapter = LoanViewPagerAdapter(this)
////        myLoanBinding!!.loanViewPager.adapter = mLoanAdapter
////        myLoanBinding!!.loanViewPager.offscreenPageLimit = 3
////        callback = object : OnPageChangeCallback() {
////            override fun onPageScrolled(
////                position: Int,
////                positionOffset: Float,
////                positionOffsetPixels: Int
////            ) {
////            }
////
////            override fun onPageSelected(position: Int) {
////                addIndicator(myLoanBinding!!.llLoanIndicator, mLoanAdapter!!.getItemCount(), position)
////                positionIndicator = position
////            }
////
////            override fun onPageScrollStateChanged(state: Int) {}
////        }
//////        setUpBanner()
//////        initLiveData()
//////        myLoanTabSelected()
////
////        return myLoanBinding!!.root;
//    }

    private fun addIndicator(
        container: LinearLayout,
        length: Int,
        position: Int
    ) {
        container.removeAllViews()
        for (i in 0 until length) {
            val view = View(requireActivity().applicationContext)
            val size = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                7f,
                resources.displayMetrics
            )
            val margin = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                5f,
                resources.displayMetrics
            )
            val marginTop = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                15f,
                resources.displayMetrics
            )
            val marginBottom = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                21f,
                resources.displayMetrics
            )
            val params =
                LinearLayout.LayoutParams(size.toInt(), size.toInt())
            params.setMargins(0, marginTop.toInt(), margin.toInt(), marginBottom.toInt())
            view.layoutParams = params
            view.background =
                if (i == position) resources.getDrawable(R.drawable.circle_000000) else resources.getDrawable(
                    R.drawable.circle_cfd8dc
                )
            val textView = TextView(requireActivity().applicationContext)
            textView.text = Html.fromHtml("&#8226;")
            textView.textSize = 25f
            textView.background =
                if (i == position) resources.getDrawable(R.drawable.circle_000000) else resources.getDrawable(
                    R.drawable.circle_cfd8dc
                )
            container.addView(view)
        }
        container.postInvalidate()
    }

    fun setUpBanner() {
        // TODO :: test banner
        /*for testing banner ;*/
        bannerArray = ArrayList()
        bannerArray!!.add(R.drawable.ic_brand_bnkc_lao_leasing)
        bannerArray!!.add(R.drawable.ic_badge_failed_b_char)
        bannerArray!!.add(R.drawable.ic_badge_success_g_char)
        bannerArray!!.add(R.drawable.ic_badge_key_dark)
        bannerArray!!.add(R.drawable.ic_brand_bnkc_lao_leasing)
        mBannerAdapter = BannerAdapter(requireActivity().applicationContext, bannerArray!!)
        myLoanBinding!!.bannerViewPager.setAdapter(mBannerAdapter)
        myLoanBinding!!.bannerViewPager.setOffscreenPageLimit(5)
        myLoanBinding!!.bannerViewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                page = position
                addIndicator(myLoanBinding!!.llBannerIndicator, mBannerAdapter!!.getCount(), position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        pageSwitcher(3)
    }

    fun pageSwitcher(seconds: Int) {
        timer = Timer() // At this line a new Thread will be created
//        timer!!.scheduleAtFixedRate(bannerTask, 0, seconds * 1000.toLong()) // delay
    }

    interface MyLoanCardClickedListener {
        fun onAddNewLoanClicked()
    }


    override fun onBillPaymentClicked(contractNo: String?, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onManagementClicked(contractNo: String?, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onAddNewLoanClicked() {
        TODO("Not yet implemented")
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_my_page
    }

//    private val bannerTask = AutoScrollTask()

//    class AutoScrollTask : TimerTask() {
//        override fun run() {
//            if (getActivity(context) == null) {
//                return
//            }
//            // As the TimerTask run on a separate thread from UI thread we have
//            // to call runOnUiThread to do work on UI thread.
//            getActivity(context).runOnUiThread(Runnable {
//                if (page > bannerArray.size - 1) {
//                    page = 0
//                }
//                myLoanBinding.bannerViewPager.setCurrentItem(page)
//                page++
//            })
//        }
//    }

}