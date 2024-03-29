package com.mobile.bnkcl.ui.alarm

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnScrollChangedListener
import android.widget.AbsListView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bnkc.library.data.type.RunTimeDataStore
import com.bnkc.library.data.type.Status
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.base.BaseActivity
import com.bnkc.sourcemodule.dialog.SystemDialog
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.response.alarm.AlarmItem
import com.mobile.bnkcl.databinding.ActivityNotificationBinding
import com.mobile.bnkcl.ui.adapter.AlarmAdapter
import com.mobile.bnkcl.ui.pinview.PinCodeActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmActivity : BaseActivity<ActivityNotificationBinding>() {

    private val alarmViewModel: AlarmViewModel by viewModels()

    @Inject
    lateinit var systemDialog: SystemDialog

    @Inject
    lateinit var adapter: AlarmAdapter
    private var isSending = false

    override fun getLayoutId(): Int = R.layout.activity_notification

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarColor(ContextCompat.getColor(this, R.color.color_f5f7fc))

        initView()

        binding.lvNotifiation.adapter = adapter
        adapter.clearItemList()
        alarmViewModel.alarmListLiveData.observe(this) {
            if (binding.swipeRefreshNotification.isRefreshing) {
                binding.swipeRefreshNotification.isRefreshing = false
            }
            val list: MutableList<AlarmItem> = it.alarms

            if (list.isNotEmpty()) {
                adapter.addItemList(list)
            }
//            checkUIChangeEmptyData()
            isSending = false
        }
        alarmViewModel.getAlarmList()
            showLoading(true)
        isSending = true
    }

    private fun initView() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.swipeRefreshNotification.setColorSchemeColors(
            ContextCompat.getColor(
                this,
                R.color.colorAccent
            )
        )
        binding.swipeRefreshNotification.setOnRefreshListener {
            resetStartPage()
        }

        binding.nsvAlarm.viewTreeObserver.addOnScrollChangedListener {
            val view = binding.nsvAlarm.getChildAt(binding.nsvAlarm.childCount -1) as View
            val diff = view.bottom - (binding.nsvAlarm.height + binding.nsvAlarm.scrollY)

            if (diff == 0){

                if (!alarmViewModel.isLastPage()){
//                    if (alarmViewModel.pageNo==0) return@addOnScrollChangedListener
                    ++alarmViewModel.pageNo
                    alarmViewModel.getAlarmList()
                    showLoading(true)

                }

            }
        }

//        binding.lvNotifiation.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                if (!binding.swipeRefreshNotification.isRefreshing) {
//
//                    val visibleItemCount = layoutManager.childCount
//                    val totalItemCount = layoutManager.itemCount
//
//                    val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()
//
////                    val lastVisible = layoutManager.findLastVisibleItemPosition()
//                    if (dy > 0) {
//                        if (!alarmViewModel.isLastPage()) {
//                            if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
//                                if (isSending) return
//                                ++alarmViewModel.pageNo
//                                alarmViewModel.getAlarmList()
//                                showLoading(true)
//                                isSending = true
//                            }
//                        }
//                    }
//                }
//            }
//        })
    }

    override fun handleSessionExpired(icon: Int, title: String, message: String, button: String) {
        super.handleSessionExpired(icon, title, message, button)
        systemDialog = SystemDialog.newInstance(icon, title, message, button)
        systemDialog.show(supportFragmentManager, systemDialog.tag)
        systemDialog.onConfirmClicked {
            RunTimeDataStore.LoginToken.value = ""
            startActivity(Intent(this, PinCodeActivity::class.java))
        }
    }

    private fun resetStartPage() {
        binding.lvNotifiation.removeAllViews()
        adapter.clearItemList()

        alarmViewModel.pageNo = 0
        alarmViewModel.getAlarmList()
//        showLoading()
        isSending = true
    }

//    private fun checkUIChangeEmptyData() {
//        if (adapter.itemCount > 0) {
//            binding.lvNotifiation.visibility = View.VISIBLE
//            binding.llNoData.visibility = View.GONE
//        } else {
//            binding.lvNotifiation.visibility = View.GONE
//            binding.llNoData.visibility = View.VISIBLE
//        }
//    }


}