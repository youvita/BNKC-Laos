package com.mobile.bnkcl.fcm

import android.app.ActivityManager
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import com.bnkc.library.prefer.CredentialSharedPrefer
import com.bnkc.library.prefer.TempDataClass
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.util.ComUtil
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject


@ServiceScoped
class PushMessageService() : FirebaseMessagingService() {

    companion object {
        const val NOTIFICATION_ID = 1010
        var badge = 0
    }

    @Inject
    lateinit var sharedPrefer: CredentialSharedPrefer

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        try {
            val notificationHelper = NotificationHelper(this)
            val recvIntent = notificationHelper.getBundleRemoteMessage(remoteMessage)
            val context = applicationContext

            // 푸시알림
            val allowNotificationYN: String? = sharedPrefer.getPrefer(Constants.Push.PUSH_ALARM)
            Log.d("push:: ", allowNotificationYN!!)
            if ((allowNotificationYN == "N") or allowNotificationYN.isEmpty()) {
                //+ 알림 off
                return
            }
            badge += badge
            ComUtil.setBadge(context, badge)
            getTopActivity()
            val wakeUpIntent = Intent(context, PushEmptyActivity::class.java)
            wakeUpIntent.putExtras(recvIntent)
            val contentTitle = remoteMessage.notification!!.title
            val contentBody = remoteMessage.notification!!.body
            Log.d(
                "push",
                "title  :: " + contentTitle + " == " + contentBody + remoteMessage.data["notification"]
            )
//            TempDataClass.ActionType.code = remoteMessage.data[Constants.HandlePush.ACTION_TYPE]!!
//            TempDataClass.ActionId.code = remoteMessage.data[Constants.HandlePush.ACTION_ID]!!
//            TempDataClass.ActionUrl.code = remoteMessage.data[Constants.HandlePush.ACTION_URL]!!

            // KitKat에서 PendingIntent.FLAG_CANCEL_CURRENT 플래그 추가해야 함. (https://code.google.com/p/android/issues/detail?id=61850)
            val pi = PendingIntent.getActivity(context, 0, wakeUpIntent, 0)
            val builder = notificationHelper.getNotification(contentTitle, contentBody, pi)
            builder.setAutoCancel(true)
            notificationHelper.notify(NOTIFICATION_ID, builder)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    /**
     * Re-Set FCM Token if can't generated Token in first process
     * 2021.07.13 @Sakary
     * @param token
     */
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("kosign", "onNewToken:: $token")
        val mPushID: String? = sharedPrefer.getPrefer(Constants.HandlePush.PUSH_ID)
        if ("" == mPushID || mPushID != token) {
            sharedPrefer.putPrefer(Constants.HandlePush.PUSH_ID, token)
        }
    }

    @Throws(java.lang.Exception::class)
    private fun getTopActivity(): String? {
        val am = this.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val list = am.getRunningTasks(5)
        return if (list[0].numRunning > 0) list[0].topActivity!!.className else ""
    }

}