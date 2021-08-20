package com.mobile.bnkcl.ui.alarm

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.base.BaseActivity
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.request.alarm.AlarmRequest
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
        super.onCreate(savedInstanceState)

        initView()

        if (!sharedPrefer.getPrefer(Constants.USER_ID).isNullOrEmpty()) {
            binding.lvNotifiation.adapter = adapter
            alarmViewModel.alarmListLiveData.observe(this) {
                var list: MutableList<AlarmItem> = mutableListOf()
                for (i in 0..10) {
                    list.add(i,AlarmItem(i, "test$i", "content$i", "2021.08.20", 'n', 'n'))
                }
                adapter.addItemList(list)
            }
            alarmViewModel.alarmRequest = AlarmRequest(1, 10, "")
            alarmViewModel.getAlarmList()
        }
    }

    fun initView() {
        binding.ivBack.setOnClickListener{
            onBackPressedDispatcher
        }
    }


}