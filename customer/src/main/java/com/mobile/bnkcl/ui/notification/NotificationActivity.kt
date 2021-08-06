package com.mobile.bnkcl.ui.notification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bnkc.sourcemodule.base.BaseActivity
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityNotificationBinding


class NotificationActivity : BaseActivity<ActivityNotificationBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_notification
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


}