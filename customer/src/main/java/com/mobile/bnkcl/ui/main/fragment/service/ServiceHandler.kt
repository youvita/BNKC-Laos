package com.mobile.bnkcl.ui.main.fragment.service

import android.content.Intent
import android.view.View
import com.mobile.bnkcl.ui.notification.NotificationActivity
import com.mobile.bnkcl.ui.setting.SettingActivity

class ServiceHandler {

    fun goToNotification(view: View){
        val intent = Intent(view.context, NotificationActivity::class.java)
        view.context.startActivity(intent)
    }

    fun goToSettings(view: View){
        val intent = Intent(view.context, SettingActivity::class.java)
        view.context.startActivity(intent)
    }
}