package com.mobile.bnkcl.ui.notification

import android.os.Bundle
import androidx.activity.viewModels
import com.bnkc.sourcemodule.base.BaseActivity
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.request.alarm.AlarmRequest
import com.mobile.bnkcl.databinding.ActivityNotificationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationActivity : BaseActivity<ActivityNotificationBinding>() {

    private val notificationViewModel: NotificationViewModel by viewModels()

    override fun getLayoutId(): Int = R.layout.activity_notification

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        notificationViewModel.alarmListLiveData.observe(this) {

        }
        notificationViewModel.alarmRequest = AlarmRequest(1, 10, "ASC")
        notificationViewModel.getAlarmList()
    }


}