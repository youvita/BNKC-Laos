package com.mobile.bnkcl.ui.alarm

import android.os.Bundle
import androidx.activity.viewModels
import com.bnkc.sourcemodule.base.BaseActivity
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.request.alarm.AlarmRequest
import com.mobile.bnkcl.databinding.ActivityNotificationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlarmActivity : BaseActivity<ActivityNotificationBinding>() {

    private val alarmViewModel: AlarmViewModel by viewModels()

    override fun getLayoutId(): Int = R.layout.activity_notification

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        alarmViewModel.alarmListLiveData.observe(this) {

        }
        alarmViewModel.alarmRequest = AlarmRequest(1, 10, "")
        alarmViewModel.getAlarmList()
    }


}