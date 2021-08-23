package com.mobile.bnkcl.ui.alarm

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.base.BaseActivity
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.response.alarm.AlarmItem
import com.mobile.bnkcl.databinding.ActivityNotificationBinding
import com.mobile.bnkcl.ui.adapter.AlarmAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmActivity : BaseActivity<ActivityNotificationBinding>() {

    private val alarmViewModel: AlarmViewModel by viewModels()

    @Inject
    lateinit var adapter: AlarmAdapter

    override fun getLayoutId(): Int = R.layout.activity_notification

    override fun onCreate(savedInstanceState: Bundle?) {
        setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
        super.onCreate(savedInstanceState)

        initView()

        if (!sharedPrefer.getPrefer(Constants.USER_ID).isNullOrEmpty()) {
            binding.lvNotifiation.adapter = adapter
            alarmViewModel.alarmListLiveData.observe(this) {
                if (binding.swipeRefreshNotification.isRefreshing) {
                    binding.swipeRefreshNotification.isRefreshing = false
                }
                binding.lvNotifiation.removeAllViews()
                val list: MutableList<AlarmItem> = it.alarms
//                val list: MutableList<AlarmItem> = mutableListOf() // test empty

                checkUIChangeEmptyData(list)
                if (list.isNotEmpty()) {
                    adapter.addItemList(list)
                }
            }
            alarmViewModel.getAlarmList()
        }
    }

    private fun initView() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.swipeRefreshNotification.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorAccent))
        binding.swipeRefreshNotification.setOnRefreshListener {
            resetStartPage()
        }

        binding.lvNotifiation.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!binding.swipeRefreshNotification.isRefreshing) {
                    val layoutManager: LinearLayoutManager =
                        binding.lvNotifiation.layoutManager as LinearLayoutManager
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()
//                    val lastVisible = layoutManager.findLastVisibleItemPosition()
                    if (dy > 0) {
                        if (!alarmViewModel.isLastPage()) {
                            if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                                ++alarmViewModel.pageNo
                                alarmViewModel.getAlarmList()
                            }
                        }
                    }
                }
            }
        })
    }

    private fun resetStartPage() {
        alarmViewModel.pageNo = 0
        alarmViewModel.getAlarmList()
    }

    private fun checkUIChangeEmptyData(list: List<AlarmItem>) {
        if (list.isEmpty()) {
            binding.lvNotifiation.visibility = View.GONE
            binding.llNoData.visibility = View.VISIBLE
        } else {
            binding.lvNotifiation.visibility = View.VISIBLE
            binding.llNoData.visibility = View.GONE
        }
    }


}