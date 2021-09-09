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
import com.bnkc.sourcemodule.dialog.LoadingDialog
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.response.code.CodesData
import com.mobile.bnkcl.data.response.dashboard.LeaseApplicationData
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
    lateinit var bannerAdapter: BannerAdapter

    @Inject
    lateinit var itemOffsetDecoration: CardOffsetDecoration

    private var LRS001: Int = 0
    private var LRS002: Int = 0
    private var LRS003: Int = 0
    private var leaseAdapter: LeaseAdapter? = null
    private var leaseDialog1: ApplicationDialog? = null
    private var leaseDialog2: ApplicationDialog? = null
    private var leaseDialog3: ApplicationDialog? = null
    private val viewModel: PageViewModel by viewModels()
    private var pageBinding: FragmentMyPageBinding? = null
    private val mListener: MyLoanCardClickedListener? = null
    private var contractList: ArrayList<String>? = ArrayList()
    private var leaseLayoutManager: CardModeLayoutManager? = null
    private var bannerLayoutManager: CardModeLayoutManager? = null
    private var leaseResultList: List<LeaseApplicationData>? = null
    private var leaseScreeningList: List<LeaseApplicationData>? = null
    private var leaseApplicationList: List<LeaseApplicationData>? = null
    private var leaseData: ArrayList<MyLeasesData>? = ArrayList()
    private var productTypeList: ArrayList<CodesData>? = ArrayList()
    private var requestTypeList: ArrayList<CodesData>? = ArrayList()
    private var progressTypeList: ArrayList<CodesData>? = ArrayList()
    private var banners =
        listOf(R.drawable.banner_1, R.drawable.banner_2, R.drawable.banner_3, R.drawable.banner_4)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        pageBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_my_page, container, false)
        if (!sharedPrefer.getPrefer(Constants.USER_ID).isNullOrEmpty()) {
            viewModel.getProductTypeCodes()
            viewModel.getLeaseProgressCodes()
        }

        initView()
        initLiveData()

        return pageBinding!!.root
    }

    private fun initLiveData() {
        viewModel.dashboardLiveData.observe(requireActivity()) {
            LRS001 = it.countApplication!!
            LRS002 = it.countScreening!!
            LRS003 = it.countResult!!

            setUpDashboard(LRS001, LRS002, LRS003, it.myLeases!!.size)

            it.myLeases.add(MyLeasesData())

            leaseAdapter = LeaseAdapter(this)
            leaseAdapter!!.setProductTypeList(productTypeList!!)
            leaseAdapter!!.clearItemList()
            leaseAdapter!!.addItemList(it.myLeases)

            pageBinding!!.rvLease.adapter = leaseAdapter
            pageBinding?.rvLease?.removeItemDecoration(itemOffsetDecoration)
            pageBinding?.rvLease?.addItemDecoration(itemOffsetDecoration)
            cardRecyclerView.attachToRecyclerView(pageBinding?.rvLease)
            cardRecyclerView.setScale(1f)

            leaseData!!.addAll(it.myLeases)
            setUpLeaseIndicator()

            setUpBanner()
            viewModel.getLeaseRequestCodes()
        }

        viewModel.productCodesLiveData.observe(requireActivity()) {
            productTypeList = it.codes
            viewModel.getDashboard()
        }

        viewModel.progressCodesLiveData.observe(requireActivity()) {
            progressTypeList = it.codes
        }

        viewModel.requestCodesLiveData.observe(requireActivity()) {
            requestTypeList = it.codes

            if (LRS001 > 0) {
                viewModel.getLeaseApplication(it.codes!![0].code!!)
            }

            if (LRS002 > 0) {
                viewModel.getLeaseScreening(it.codes!![1].code!!)
            }

            if (LRS003 > 0) {
                viewModel.getLeaseResult(it.codes!![2].code!!)
            }
        }

        viewModel.leaseApplicationLiveData.observe(requireActivity()) {

            leaseApplicationList = it.leaseApplications
            pageBinding!!.requestMenu.llMenu1.setOnClickListener(this)

        }

        viewModel.leaseScreeningLiveData.observe(requireActivity()) {

            leaseScreeningList = it.leaseApplications
            pageBinding!!.requestMenu.llMenu2.setOnClickListener(this)

        }

        viewModel.leaseResultLiveData.observe(requireActivity()) {

            leaseResultList = it.leaseApplications
            pageBinding!!.requestMenu.llMenu3.setOnClickListener(this)

        }
    }

    private fun setUpLeaseIndicator() {

        addIndicator(
            pageBinding!!.llLeaseIndicator,
            leaseAdapter!!.itemCount,
            0
        )

        leaseLayoutManager =
            pageBinding?.rvLease?.layoutManager as CardModeLayoutManager
        pageBinding?.rvLease?.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                addIndicator(
                    pageBinding!!.llLeaseIndicator,
                    leaseAdapter!!.itemCount,
                    leaseLayoutManager!!.findLastVisibleItemPosition()
                )

            }
        })

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.ll_menu1 -> {
                leaseDialog1 = ApplicationDialog(leaseApplicationList!!, productTypeList!!, 1)
                leaseDialog1?.show(requireActivity().supportFragmentManager, leaseDialog1?.tag)
            }
            R.id.ll_menu2 -> {
                leaseDialog2 = ApplicationDialog(leaseScreeningList!!, productTypeList!!, 2)
                leaseDialog2?.show(requireActivity().supportFragmentManager, leaseDialog2?.tag)
            }
            R.id.ll_menu3 -> {
                leaseDialog3 = ApplicationDialog(leaseResultList!!, productTypeList!!, 3)
                leaseDialog3?.show(requireActivity().supportFragmentManager, leaseDialog3?.tag)
            }
        }
    }

    private fun initView() {

        pageBinding!!.btnNotification.setOnClickListener {
            startActivity(Intent(activity, AlarmActivity::class.java))
        }
    }

    private fun setUpDashboard(MLR001: Int, MLR002: Int, MLR003: Int, MLR004: Int) {

        pageBinding!!.requestMenu.llMenu1.isEnabled = MLR001 > 0
        pageBinding!!.requestMenu.tvMenuTitle1.text = getString(R.string.menu_application).plus(
            "\n"
        ).plus(this.LRS001.toString())
        pageBinding!!.requestMenu.llMenu2.isEnabled = MLR002 > 0
        pageBinding!!.requestMenu.tvMenuTitle2.text = getString(R.string.menu_screening).plus(
            "\n"
        ).plus(this.LRS002.toString())
        pageBinding!!.requestMenu.llMenu3.isEnabled = MLR003 > 0
        pageBinding!!.requestMenu.tvMenuTitle3.text = getString(R.string.menu_result).plus(
            "\n"
        ).plus(this.LRS003.toString())

        pageBinding!!.tvLeaseInUseCnt.isEnabled = MLR004 != 0
        pageBinding!!.tvLeaseInUseCnt.visibility = View.VISIBLE
        pageBinding!!.tvLeaseInUseCnt.text = java.lang.String.valueOf(MLR004)

    }

    private fun setUpBanner() {

        bannerAdapter.clearItemList()
        bannerAdapter.addItemList(banners)
        pageBinding?.rvBanner?.adapter = bannerAdapter

        pageBinding?.rvBanner?.removeItemDecoration(itemOffsetDecoration)
        pageBinding?.rvBanner?.addItemDecoration(itemOffsetDecoration)
        cardRecyclerView.attachToRecyclerView(pageBinding?.rvBanner)
        cardRecyclerView.setScale(1f)

        addIndicator(
            pageBinding!!.llBannerIndicator,
            bannerAdapter.itemCount,
            0
        )

        bannerLayoutManager = pageBinding?.rvBanner?.layoutManager as CardModeLayoutManager
        pageBinding?.rvBanner?.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                addIndicator(
                    pageBinding!!.llBannerIndicator,
                    bannerAdapter.itemCount,
                    bannerLayoutManager!!.findLastVisibleItemPosition()
                )

            }
        })

    }

    interface MyLoanCardClickedListener {
        fun onAddNewLoanClicked()
    }

    override fun onBillPaymentClicked(contractNo: String?, position: Int) {
        val intent = Intent(requireActivity(), BillPaymentActivity::class.java)
        intent.putExtra("CONTRACT_NO", contractNo)
        intent.putExtra("TOTAL_PAYMENT", leaseData!![position].totalPayment)
        startActivity(intent)
    }

    override fun onManagementClicked(contractNo: String?, position: Int) {
        val intent = Intent(requireActivity(), LeaseManagementActivity::class.java)
        intent.putExtra("CONTRACT_NO", contractNo)
        for (i in 0 until leaseData!!.size - 1) {
            contractList!!.add(leaseData!![i].contractNo!!)
        }
        intent.putExtra("CONTRACT_NO_RECORD", contractList)
        startActivity(intent)
    }

    override fun onAddNewLeaseClicked() {
        mListener?.onAddNewLoanClicked()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_my_page
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