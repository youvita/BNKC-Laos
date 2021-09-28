package com.mobile.bnkcl.ui.main.fragment.mypage

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.bnkc.library.custom.cardview.CardModeLayoutManager
import com.bnkc.library.custom.cardview.CardOffsetDecoration
import com.bnkc.library.custom.cardview.CardRecyclerView
import com.bnkc.library.data.type.AppLogin
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.base.BaseFragment
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.response.code.CodesData
import com.mobile.bnkcl.data.response.dashboard.LeaseApplicationData
import com.mobile.bnkcl.data.response.dashboard.MyLeasesData
import com.mobile.bnkcl.databinding.FragmentMyPageBinding
import com.mobile.bnkcl.ui.adapter.BannerAdapter
import com.mobile.bnkcl.ui.adapter.LeaseAdapter
import com.mobile.bnkcl.ui.alarm.AlarmActivity
import com.mobile.bnkcl.ui.dialog.ApplicationDialog
import com.mobile.bnkcl.ui.lease.apply.ApplyLeaseActivity
import com.mobile.bnkcl.ui.lease.service.LeaseServiceActivity
import com.mobile.bnkcl.ui.management.LeaseManagementActivity
import com.mobile.bnkcl.ui.management.bill.BillPaymentActivity
import com.mobile.bnkcl.ui.otp.OtpActivity
import com.mobile.bnkcl.ui.pinview.PinCodeActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class PageFragment : BaseFragment<FragmentMyPageBinding>(),
    LeaseAdapter.LeaseItemClickedListener, View.OnClickListener {

    @Inject
    lateinit var bannerAdapter: BannerAdapter

    @Inject
    lateinit var leaseAdapter: LeaseAdapter

    @Inject
    lateinit var itemOffsetDecoration: CardOffsetDecoration

    private var LRS001: Int = 0
    private var LRS002: Int = 0
    private var LRS003: Int = 0
    private var currentIndex = 0
    private var lastIndex = 0
    private var timeCounter: Runnable? = null
    private var leaseDialog1: ApplicationDialog? = null
    private var leaseDialog2: ApplicationDialog? = null
    private var leaseDialog3: ApplicationDialog? = null
    private val viewModel: PageViewModel by viewModels()
    private val handler = Handler(Looper.getMainLooper())
    private var pageBinding: FragmentMyPageBinding? = null
    private var contractList: ArrayList<String>? = ArrayList()
    private var leaseData: ArrayList<MyLeasesData>? = ArrayList()
    private var productTypeList: ArrayList<CodesData>? = ArrayList()
    private var progressTypeList: ArrayList<CodesData>? = ArrayList()
    private var leaseLayoutManager: CardModeLayoutManager? = null
    private var bannerLayoutManager: CardModeLayoutManager? = null
    private var leaseResultList: List<LeaseApplicationData>? = null
    private var leaseScreeningList: List<LeaseApplicationData>? = null
    private var leaseApplicationList: List<LeaseApplicationData>? = null
    private var bannerList = listOf(
        R.drawable.banner_1, R.drawable.banner_2,
        R.drawable.banner_3, R.drawable.banner_4
    )
    private val emptyLeaseData: MyLeasesData = MyLeasesData("", "", "", "", "2021-12-12", "")
    private var leaseCardRecyclerView: CardRecyclerView = CardRecyclerView()
    private var bannerCardRecyclerView: CardRecyclerView = CardRecyclerView()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        pageBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_my_page,
            container, false
        )

        initView()
        initLiveData()

        return pageBinding!!.root
    }

    private fun requestData() {
        if (activity != null && isAdded) {
            viewModel.getProductCodes()
            viewModel.getLeaseProgressCodes()
            showLoading()
        }
    }

    override fun onResume() {
        super.onResume()
        if (AppLogin.PIN.code != "N") {
            requestData()
        }
    }

    private fun initView() {

        pageBinding!!.requestMenu.llMenu1.isEnabled = false
        pageBinding!!.requestMenu.llMenu2.isEnabled = false
        pageBinding!!.requestMenu.llMenu3.isEnabled = false
        pageBinding!!.tvLeaseInUseCnt.isEnabled = false

        pageBinding!!.requestMenu.tvMenuTitle1.text = getString(R.string.menu_application).plus(
            "\n"
        ).plus("0")
        pageBinding!!.requestMenu.tvMenuTitle2.text = getString(R.string.menu_screening).plus(
            "\n"
        ).plus("0")
        pageBinding!!.requestMenu.tvMenuTitle3.text = getString(R.string.menu_result).plus(
            "\n"
        ).plus("0")

        setUpBanner()
        leaseAdapter.setItemClickListener(this)

        leaseAdapter.clearItemList()
        leaseAdapter.addItemList(arrayListOf(emptyLeaseData))

        setUpLeaseAdapter()

        addIndicator(
            pageBinding!!.llEmptyLeaseIndicator,
            1,
            0
        )

        pageBinding!!.btnNotification.setOnClickListener {
            startActivity(Intent(activity, AlarmActivity::class.java))
        }
    }

    private fun initLiveData() {

        viewModel.productCodesLiveData.observe(requireActivity()) {

            if (null != it) {
                productTypeList?.clear()
                productTypeList = it.codes
                leaseAdapter.setProductTypeList(productTypeList!!)
                viewModel.getDashboard()
            }

        }

        viewModel.dashboardLiveData.observe(requireActivity()) {

            if (null != it) {
                LRS001 = it.countApplication!!
                LRS002 = it.countScreening!!
                LRS003 = it.countResult!!

                it.myLeases?.add(emptyLeaseData)

                leaseAdapter.clearItemList()
                leaseAdapter.addItemList(it.myLeases)

                setUpDashboard(LRS001, LRS002, LRS003, it.myLeases!!.size - 1)
                leaseData?.clear()
                leaseData!!.addAll(it.myLeases)
                setUpLeaseIndicator(leaseAdapter.itemCount)
                viewModel.getLeaseRequestCodes()
            }

        }

        viewModel.progressCodesLiveData.observe(requireActivity()) {

            if (null != it) {
                progressTypeList = it.codes
            }

        }

        viewModel.requestCodesLiveData.observe(requireActivity()) {

            if (null != it) {
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

        }

        viewModel.leaseApplicationLiveData.observe(requireActivity()) {

            if (null != it) {
                leaseApplicationList = it.leaseApplications
                pageBinding!!.requestMenu.llMenu1.setOnClickListener(this)
            }

        }

        viewModel.leaseScreeningLiveData.observe(requireActivity()) {

            if (null != it) {
                leaseScreeningList = it.leaseApplications
                pageBinding!!.requestMenu.llMenu2.setOnClickListener(this)
            }
        }

        viewModel.leaseResultLiveData.observe(requireActivity()) {

            if (null != it) {
                leaseResultList = it.leaseApplications
                pageBinding!!.requestMenu.llMenu3.setOnClickListener(this)
            }

        }
    }

    private fun setUpLeaseAdapter() {
        pageBinding!!.rvLease.adapter = leaseAdapter
        pageBinding?.rvLease?.removeItemDecoration(itemOffsetDecoration)
        pageBinding?.rvLease?.addItemDecoration(itemOffsetDecoration)
        leaseCardRecyclerView.attachToRecyclerView(pageBinding?.rvLease)
        leaseCardRecyclerView.setScale(1f)
    }

    private fun setUpLeaseIndicator(count: Int) {

        addIndicator(
            pageBinding!!.llLeaseIndicator,
            count,
            lastIndex
        )

        leaseLayoutManager =
            pageBinding?.rvLease?.layoutManager as CardModeLayoutManager
        pageBinding?.rvLease?.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                addIndicator(
                    pageBinding!!.llLeaseIndicator,
                    count,
                    leaseLayoutManager!!.findLastVisibleItemPosition()
                )

                lastIndex = leaseLayoutManager!!.findLastVisibleItemPosition()
            }
        })

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.ll_menu1 -> {
                leaseDialog1 = ApplicationDialog(
                    leaseApplicationList!!, productTypeList!!, progressTypeList!!, 1
                )
                leaseDialog1?.show(requireActivity().supportFragmentManager, leaseDialog1?.tag)
            }
            R.id.ll_menu2 -> {
                leaseDialog2 = ApplicationDialog(
                    leaseScreeningList!!, productTypeList!!, progressTypeList!!, 2
                )
                leaseDialog2?.show(requireActivity().supportFragmentManager, leaseDialog2?.tag)
            }
            R.id.ll_menu3 -> {
                leaseDialog3 =
                    ApplicationDialog(
                        leaseResultList!!, productTypeList!!, progressTypeList!!, 3
                    )
                leaseDialog3?.show(requireActivity().supportFragmentManager, leaseDialog3?.tag)
            }
        }
    }

    private fun setUpDashboard(MLR001: Int, MLR002: Int, MLR003: Int, count: Int) {

        pageBinding!!.requestMenu.llMenu1.isSelected = MLR001 > 0
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

        pageBinding!!.tvLeaseInUseCnt.isEnabled = count != 0
        pageBinding!!.tvLeaseInUseCnt.text = java.lang.String.valueOf(count)

        pageBinding!!.llEmptyLeaseIndicator.visibility =
            if (leaseAdapter.itemCount == 1) View.VISIBLE else View.GONE
    }

    private fun setUpBanner() {

        bannerAdapter.clearItemList()
        bannerAdapter.addItemList(bannerList)
        pageBinding?.rvBanner?.adapter = bannerAdapter

        pageBinding?.rvBanner?.removeItemDecoration(itemOffsetDecoration)
        pageBinding?.rvBanner?.addItemDecoration(itemOffsetDecoration)
        bannerCardRecyclerView.attachToRecyclerView(pageBinding?.rvBanner)
        bannerCardRecyclerView.setScale(1f)

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

        try {
            timeCounter = object : Runnable {
                override fun run() {
                    if (currentIndex + 1 > bannerAdapter.itemCount - 1) {
                        currentIndex = 0
                    } else {
                        currentIndex++
                    }
                    bannerLayoutManager!!.smoothScrollToPosition(
                        pageBinding!!.rvBanner,
                        RecyclerView.State(), currentIndex
                    )
                    handler.postDelayed(this, 3000)
                }
            }
            handler.postDelayed(timeCounter as Runnable, 3000)
        } catch (e: Exception) {
            e.printStackTrace()
        }
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
        contractList?.clear()
        for (i in 0 until leaseData!!.size - 1) {
            contractList!!.add(leaseData!![i].contractNo!!)
        }
        intent.putExtra("CONTRACT_NO_RECORD", contractList)
        intent.putExtra("PRODUCT_TYPE_LIST", productTypeList)
        startActivity(intent)
    }

    override fun onAddNewLeaseClicked() {
        if (AppLogin.PIN.code == "N") {
            if (sharedPrefer.getPrefer(Constants.USER_ID).isNullOrEmpty()) {
                val intent = Intent(requireActivity(), OtpActivity::class.java)
                startActivity(intent)
            } else {
                val loginIntent =
                    Intent(requireActivity(), PinCodeActivity::class.java)
                loginIntent.putExtra("pin_action", "login")
                loginIntent.putExtra(
                    "from",
                    LeaseServiceActivity::class.java.simpleName
                )
                loginIntent.putExtra(
                    "username",
                    sharedPrefer.getPrefer(Constants.USER_ID)
                )
                startActivity(loginIntent)
            }
        } else {
            startActivity(Intent(requireActivity(), ApplyLeaseActivity::class.java))
        }
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_my_page
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(timeCounter!!)
    }

    private fun addIndicator(
        container: LinearLayout,
        length: Int,
        position: Int
    ) {

        if (activity != null && isAdded) {
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    textView.text = Html.fromHtml("&#8226;", Html.FROM_HTML_MODE_LEGACY)
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        textView.text = Html.fromHtml("&#8226;", HtmlCompat.FROM_HTML_MODE_LEGACY)
                    }
                }
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
}