package com.mobile.bnkcl.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.media.AudioAttributes
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.RemoteMessage
import com.mobile.bnkcl.R

/**
 * @author chanyouvita
 * @since 2018. 06. 27.
 */
class NotificationHelper(base: Context?) : ContextWrapper(base) {

    private var notifManager: NotificationManager? = null
    private val channelId: String

    /*
     * init channel notification
     * @setShowBadge 알림 badge true:show, false: not show
     */
    private fun initChannelNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /*
             * vita: [2018-08-03] creating an audio attribute for oreo version
             */
            val audioAttributes: AudioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
            val mChannel =
                NotificationChannel(channelId, "Channel", NotificationManager.IMPORTANCE_DEFAULT)
            mChannel.setShowBadge(true)
            mChannel.setSound(mChannel.getSound(), audioAttributes)
            manager?.createNotificationChannel(mChannel)
        }
    }

    /*
     * get notification
     * @setSamllIcon 알림 아이콘 설정
     * @setContentTitle 알림 title 설정
     * @setContentText 알림 메시지(내용) 설정
     * @setContentIntent 선택 시 이동할 intent 설정
     * @setAutoCancel 알림을 선택하면 알림이 사라지도록 설정
     * @setVibrate 진동, 휴식, 진동, 휴식
     */
    fun getNotification(
        contentTitle: String?,
        contentText: String?,
        contentIntent: PendingIntent?
    ): NotificationCompat.Builder {
        val notiBuilder = NotificationCompat.Builder(getApplicationContext(), channelId)
            .setContentTitle(contentTitle)
            .setContentText(contentText)
            .setContentIntent(contentIntent)
            .setStyle(NotificationCompat.BigTextStyle().bigText(contentText))
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_VIBRATE or NotificationCompat.DEFAULT_SOUND)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notiBuilder.setSmallIcon(R.mipmap.ic_launcher)
            notiBuilder.color = getResources().getColor(R.color.colorPrimary)
        } else {
            notiBuilder.setSmallIcon(R.mipmap.ic_launcher)
        }
        return notiBuilder
    }

    /*
     * init notification manager
     */
    private val manager: NotificationManager?
        private get() {
            if (notifManager == null) {
                notifManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
            }
            return notifManager
        }

    /*
     * notification start service
     */
    fun notify(id: Int, notification: NotificationCompat.Builder) {
        manager?.notify(id, notification.build())
    }

    /*
     * Convert RemoteMessage of FCM to Bundle
     * @param remoteMessage
     * @return
     */
    fun getBundleRemoteMessage(remoteMessage: RemoteMessage): Bundle {
        val extras = Bundle()
        if (remoteMessage.getData().size > 0) {
            for ((key, value) in remoteMessage.getData().entries) {
                extras.putString(key, value)
            }
        }
        return extras
    }

    fun clearBadge() {
        try {
            if (notifManager == null) {
                return
            }
            notifManager?.cancelAll()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /*
     * init notification contractor
     */
    init {
        channelId = getPackageName()
        initChannelNotification()
    }
}