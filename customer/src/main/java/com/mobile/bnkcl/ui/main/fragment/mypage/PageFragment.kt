package com.mobile.bnkcl.ui.main.fragment.mypage

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.bnkc.library.custom.cardview.CardOffsetDecoration
import com.bnkc.library.custom.cardview.CardRecyclerView
import com.bnkc.sourcemodule.base.BaseFragment
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.response.dashboard.MyLeasesData
import com.mobile.bnkcl.data.response.dashboard.SummaryData
import com.mobile.bnkcl.databinding.FragmentMyPageBinding
import com.mobile.bnkcl.ui.adapter.BannerAdapter
import com.mobile.bnkcl.ui.adapter.LeaseViewPagerAdapter
import com.mobile.bnkcl.ui.alarm.AlarmActivity
import com.mobile.bnkcl.ui.bill.BillPaymentActivity
import com.mobile.bnkcl.ui.dialog.ApplicationDialog
import com.mobile.bnkcl.ui.management.LeaseManagementActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PageFragment : BaseFragment<FragmentMyPageBinding>(),
    LeaseViewPagerAdapter.LoanPagerClickedListener, View.OnClickListener {

    @Inject
    lateinit var cardRecyclerView: CardRecyclerView

    @Inject
    lateinit var mBannerAdapter: BannerAdapter

    @Inject
    lateinit var itemOffsetDecoration: CardOffsetDecoration

    private var myLoanBinding: FragmentMyPageBinding? = null
    private var mLeaseAdapter: LeaseViewPagerAdapter? = null
//    private var mBannerAdapter: BannerAdapter? = null
    private var mLeaseData: ArrayList<MyLeasesData>? = null
    private var mContractNoRecord: ArrayList<String>? = null
    private val mListener: MyLoanCardClickedListener? = null
    private var callback: OnPageChangeCallback? = null
    private var positionIndicator = 0
    private var MLR001: Int = 0
    private var MLR002: Int = 0
    private var MLR003: Int = 0
    private var MLR004: Int = 0
    private var bannerArray: ArrayList<Int>? = null
    private var page = 0
    private var loadingDialog: ApplicationDialog? = null
    private val pageViewModel: PageViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        myLoanBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_my_page, container, false)

        mLeaseAdapter = LeaseViewPagerAdapter(this)
        mLeaseData = java.util.ArrayList<MyLeasesData>()
        myLoanBinding!!.leaseViewPager.adapter = mLeaseAdapter
        myLoanBinding!!.leaseViewPager.offscreenPageLimit = 3

        pageViewModel.getDashboard()
        pageViewModel.dashboardLiveData.observe(requireActivity()) {
            MLR001 = it.summary?.countApplication!!
            MLR002 = it.summary.countScreening!!
            MLR003 = it.summary.countResult!!
            mLeaseAdapter!!.addData(it.myLeases!!)
            mLeaseData!!.addAll(it.myLeases)
            setUpLeaseIndicator()
            setUpDashboard(it.summary)
        }

        initClickListener()
        setUpBanner()

        return myLoanBinding!!.root
    }

    private fun initClickListener() {
        myLoanBinding!!.requestMenu.llMenu1.setOnClickListener(this)
        myLoanBinding!!.btnNotification.setOnClickListener{
            startActivity(Intent(activity, AlarmActivity::class.java))
        }
    }

    private fun setUpLeaseIndicator() {
        addIndicator(myLoanBinding!!.llLeaseIndicator, mLeaseData!!.size, 0)

        callback = object : OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                addIndicator(myLoanBinding!!.llLeaseIndicator, mLeaseAdapter!!.itemCount, position)
                positionIndicator = position
            }

            override fun onPageScrollStateChanged(state: Int) {}
        }

        myLoanBinding!!.leaseViewPager.registerOnPageChangeCallback(callback as OnPageChangeCallback)
        myLoanBinding!!.leaseViewPager.post {

            addIndicator(
                myLoanBinding!!.llLeaseIndicator,
                mLeaseAdapter!!.itemCount,
                positionIndicator
            )
        }
    }

    private fun setUpDashboard(summary: SummaryData) {
        myLoanBinding!!.requestMenu.llMenu1.isSelected = MLR001 > 0
        myLoanBinding!!.requestMenu.llMenu1.isEnabled = summary.countApplication!! > 0
        myLoanBinding!!.requestMenu.tvMenuTitle1.text = getString(R.string.my_loan_menu_001).plus(
            "\n"
        ).plus(MLR001.toString())
        myLoanBinding!!.requestMenu.llMenu2.isEnabled = summary.countScreening!! > 0
        myLoanBinding!!.requestMenu.tvMenuTitle2.text = getString(R.string.my_loan_menu_002).plus(
            "\n"
        ).plus(MLR002.toString())
        myLoanBinding!!.requestMenu.llMenu3.isEnabled = summary.countResult!! > 0
        myLoanBinding!!.requestMenu.tvMenuTitle3.text = getString(R.string.my_loan_menu_003).plus(
            "\n"
        ).plus(MLR003.toString())

//        myLoanBinding!!.tvLeaseInUseCnt.isEnabled = summary.count_activated != 0
//        myLoanBinding!!.tvLeaseInUseCnt.text = java.lang.String.valueOf(summary.count_activated)

    }

    override fun onClick(v: View?) {

        when (v!!.id) {
            R.id.ll_menu1 -> {
                loadingDialog = ApplicationDialog()
                loadingDialog?.show(requireActivity().supportFragmentManager, loadingDialog?.tag)
            }
        }
    }

    interface MyLoanCardClickedListener {
        fun onAddNewLoanClicked()
    }

    override fun onBillPaymentClicked(contractNo: String?, position: Int) {
        val intent = Intent(requireActivity(), BillPaymentActivity::class.java)
        intent.putExtra("CONTRACT_NO", contractNo)
        intent.putExtra("TOTAL_PAYMENT", mLeaseData!![position].contractNo)
        startActivity(intent)
    }

    override fun onManagementClicked(contractNo: String?, position: Int) {
        val intent = Intent(requireActivity(), LeaseManagementActivity::class.java)
        intent.putExtra("CONTRACT_NO", contractNo)
        mContractNoRecord = ArrayList()
        for (i in 0 until mLeaseData!!.size - 1) {
            mContractNoRecord!!.add(mLeaseData!![i].contractNo!!)
        }
        intent.putExtra("CONTRACT_NO_RECORD", mContractNoRecord)
        startActivity(intent)
    }

    override fun onAddNewLoanClicked() {
        mListener?.onAddNewLoanClicked()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_my_page
    }

    private fun setUpBanner() {
        bannerArray = ArrayList()
        bannerArray!!.add(R.drawable.banner_1)
        bannerArray!!.add(R.drawable.banner_2)
        bannerArray!!.add(R.drawable.banner_3)
        bannerArray!!.add(R.drawable.banner_4)

        myLoanBinding?.bannerViewPager?.adapter = mBannerAdapter
        myLoanBinding?.bannerViewPager?.removeItemDecoration(itemOffsetDecoration)
        myLoanBinding?.bannerViewPager?.addItemDecoration(itemOffsetDecoration)
        cardRecyclerView.attachToRecyclerView(myLoanBinding?.bannerViewPager)
        cardRecyclerView.setScale(1f)

//        mBannerAdapter = BannerAdapter(requireActivity().applicationContext, bannerArray!!)
//        myLoanBinding!!.bannerViewPager.adapter = mBannerAdapter
//        myLoanBinding!!.bannerViewPager.offscreenPageLimit = 4
//        addIndicator(
//            myLoanBinding!!.llBannerIndicator,
//            mBannerAdapter!!.count,
//            0
//        )

//        myLoanBinding!!.bannerViewPager.addOnPageChangeListener(object : OnPageChangeListener {
//            override fun onPageScrolled(
//                position: Int,
//                positionOffset: Float,
//                positionOffsetPixels: Int
//            ) {
//                myLoanBinding!!.bannerViewPager.currentItem = position
//            }
//
//            override fun onPageSelected(position: Int) {
//                page = position
//
//                addIndicator(
//                    myLoanBinding!!.llBannerIndicator,
//                    mBannerAdapter!!.count,
//                    position
//                )
//            }
//
//            override fun onPageScrollStateChanged(state: Int) {}
//        })

    }

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
                if (i == position) ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.circle_000000, null
                ) else ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.circle_cfd8dc, null
                )
            val textView = TextView(requireActivity().applicationContext)
            textView.text = Html.fromHtml("&#8226;")
            textView.textSize = 25f
            textView.background =
                if (i == position) ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.circle_000000,
                    null
                ) else ResourcesCompat.getDrawable(
                    resources, R.drawable.circle_cfd8dc, null
                )
            container.addView(view)
        }
        container.postInvalidate()
    }
}