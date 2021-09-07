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
import androidx.recyclerview.widget.RecyclerView
import com.bnkc.library.custom.cardview.CardModeLayoutManager
import com.bnkc.library.custom.cardview.CardOffsetDecoration
import com.bnkc.library.custom.cardview.CardRecyclerView
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.base.BaseFragment
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.response.dashboard.MyLeasesData
import com.mobile.bnkcl.databinding.FragmentMyPageBinding
import com.mobile.bnkcl.ui.adapter.BannerAdapter
import com.mobile.bnkcl.ui.adapter.LeaseAdapter
import com.mobile.bnkcl.ui.alarm.AlarmActivity
import com.mobile.bnkcl.ui.dialog.ApplicationDialog
import com.mobile.bnkcl.ui.management.LeaseManagementActivity
import com.mobile.bnkcl.ui.management.bill.BillPaymentActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class PageFragment : BaseFragment<FragmentMyPageBinding>(),
    LeaseAdapter.LeaseItemClickedListener, View.OnClickListener {

    @Inject
    lateinit var cardRecyclerView: CardRecyclerView

    @Inject
    lateinit var cardRecyclerVieww: CardRecyclerView

    @Inject
    lateinit var mBannerAdapter: BannerAdapter

    @Inject
    lateinit var itemOffsetDecoration: CardOffsetDecoration

    @Inject
    lateinit var itemOffsetDecorationn: CardOffsetDecoration

    private var mLeaseAdapter: LeaseAdapter = LeaseAdapter(this)
    private var myLeaseBinding: FragmentMyPageBinding? = null
    private var cardModeLayoutManager: CardModeLayoutManager? = null
    private var mLeaseData: ArrayList<MyLeasesData>? = java.util.ArrayList<MyLeasesData>()
    private var mContractNoRecord: ArrayList<String>? = null
    private val mListener: MyLoanCardClickedListener? = null
    private var MLR001: Int = 0
    private var MLR002: Int = 0
    private var MLR003: Int = 0
    private var bannerArray =
        listOf(R.drawable.banner_1, R.drawable.banner_2, R.drawable.banner_3, R.drawable.banner_4)
    private var leaseDialog: ApplicationDialog? = null
    private val pageViewModel: PageViewModel by viewModels()
    private var index: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        myLeaseBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_my_page, container, false)
        if (!sharedPrefer.getPrefer(Constants.USER_ID).isNullOrEmpty()) {
            pageViewModel.getCodes("LEASE_PROGRESS_STATUS")
            pageViewModel.getDashboard()
        }

        initView()
        initLiveData()

        return myLeaseBinding!!.root
    }

    private fun initLiveData() {
        pageViewModel.dashboardLiveData.observe(requireActivity()) {
            MLR001 = it.countApplication!!
            MLR002 = it.countScreening!!
            MLR003 = it.countResult!!

            setUpDashboard(MLR001, MLR002, MLR003, it.myLeases!!.size)

            it.myLeases.add(MyLeasesData())
            mLeaseAdapter.clearItemList()
            mLeaseAdapter.addItemList(it.myLeases)

            myLeaseBinding!!.leaseRecyclerview.adapter = mLeaseAdapter
            myLeaseBinding?.leaseRecyclerview?.removeItemDecoration(itemOffsetDecoration)
            myLeaseBinding?.leaseRecyclerview?.addItemDecoration(itemOffsetDecoration)
            cardRecyclerView.attachToRecyclerView(myLeaseBinding?.leaseRecyclerview)
            cardRecyclerView.setScale(1f)

            mLeaseData!!.addAll(it.myLeases)
            setUpLeaseIndicator()

            setUpBanner()
        }

        pageViewModel.leaseApplicationLiveData.observe(requireActivity()) {
            when (index) {
                1 -> {
                    leaseDialog = ApplicationDialog(it.leaseApplications!!, 1)
                }
                2 -> {
                    leaseDialog = ApplicationDialog(it.leaseApplications!!, 2)
                }
                3 -> {
                    leaseDialog = ApplicationDialog(it.leaseApplications!!, 4)
                }
            }
            leaseDialog?.show(requireActivity().supportFragmentManager, leaseDialog?.tag)
        }
    }

    override fun onClick(v: View?) {
        if (!sharedPrefer.getPrefer(Constants.USER_ID).isNullOrEmpty()) {
            when (v!!.id) {
                R.id.ll_menu1 -> {
                    pageViewModel.getLeaseApplication("Application")
                    index = 1
                }
                R.id.ll_menu2 -> {
                    pageViewModel.getLeaseApplication("Screening")
                    index = 2
                }
                R.id.ll_menu3 -> {
                    pageViewModel.getLeaseApplication("Result")
                    index = 3
                }
            }
        }
    }


    private fun initView() {
        myLeaseBinding!!.requestMenu.llMenu1.setOnClickListener(this)
        myLeaseBinding!!.requestMenu.llMenu2.setOnClickListener(this)
        myLeaseBinding!!.requestMenu.llMenu3.setOnClickListener(this)
        myLeaseBinding!!.btnNotification.setOnClickListener {
            startActivity(Intent(activity, AlarmActivity::class.java))
        }
    }

    private fun setUpLeaseIndicator() {

        addIndicator(
            myLeaseBinding!!.llLeaseIndicator,
            mLeaseAdapter.itemCount,
            0
        )

        cardModeLayoutManager =
            myLeaseBinding?.leaseRecyclerview?.layoutManager as CardModeLayoutManager
        myLeaseBinding?.leaseRecyclerview?.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                addIndicator(
                    myLeaseBinding!!.llLeaseIndicator,
                    mLeaseAdapter.itemCount,
                    cardModeLayoutManager!!.findLastVisibleItemPosition()
                )

            }
        })

    }

    private fun setUpDashboard(MLR001: Int, MLR002: Int, MLR003: Int, MLR004: Int) {
        myLeaseBinding!!.requestMenu.llMenu1.isSelected = this.MLR001 > 0
        myLeaseBinding!!.requestMenu.llMenu1.isEnabled = MLR001 > 0
        myLeaseBinding!!.requestMenu.tvMenuTitle1.text = getString(R.string.menu_application).plus(
            "\n"
        ).plus(this.MLR001.toString())
        myLeaseBinding!!.requestMenu.llMenu2.isEnabled = MLR002 > 0
        myLeaseBinding!!.requestMenu.tvMenuTitle2.text = getString(R.string.menu_screening).plus(
            "\n"
        ).plus(this.MLR002.toString())
        myLeaseBinding!!.requestMenu.llMenu3.isEnabled = MLR003 > 0
        myLeaseBinding!!.requestMenu.tvMenuTitle3.text = getString(R.string.menu_result).plus(
            "\n"
        ).plus(this.MLR003.toString())

        myLeaseBinding!!.tvLeaseInUseCnt.isEnabled = MLR004 != 0
        myLeaseBinding!!.tvLeaseInUseCnt.visibility = View.VISIBLE
        myLeaseBinding!!.tvLeaseInUseCnt.text = java.lang.String.valueOf(MLR004)

    }

    interface MyLoanCardClickedListener {
        fun onAddNewLoanClicked()
    }

    override fun onBillPaymentClicked(contractNo: String?, position: Int) {
        val intent = Intent(requireActivity(), BillPaymentActivity::class.java)
        intent.putExtra("CONTRACT_NO", contractNo)
        intent.putExtra("TOTAL_PAYMENT", mLeaseData!![position].totalPayment)
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

    override fun onAddNewLeaseClicked() {
        mListener?.onAddNewLoanClicked()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_my_page
    }

    private fun setUpBanner() {

        mBannerAdapter.clearItemList()
        mBannerAdapter.addItemList(bannerArray)
        myLeaseBinding?.newLayout?.bannerRecyclerView?.adapter = mBannerAdapter

        myLeaseBinding?.newLayout?.bannerRecyclerView?.removeItemDecoration(itemOffsetDecorationn)
        myLeaseBinding?.newLayout?.bannerRecyclerView?.addItemDecoration(itemOffsetDecorationn)
        cardRecyclerVieww.attachToRecyclerView(myLeaseBinding?.newLayout?.bannerRecyclerView)
        cardRecyclerVieww.setScale(1f)

        addIndicator(
            myLeaseBinding!!.newLayout.llBannerIndicator,
            mBannerAdapter.itemCount,
            0
        )

        cardModeLayoutManager =
            myLeaseBinding?.newLayout?.bannerRecyclerView?.layoutManager as CardModeLayoutManager
        myLeaseBinding?.newLayout?.bannerRecyclerView?.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                addIndicator(
                    myLeaseBinding!!.newLayout.llBannerIndicator,
                    mBannerAdapter.itemCount,
                    cardModeLayoutManager!!.findLastVisibleItemPosition()
                )

            }
        })

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