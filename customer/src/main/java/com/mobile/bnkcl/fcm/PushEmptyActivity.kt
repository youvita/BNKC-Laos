package com.mobile.bnkcl.fcm

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.databinding.ViewDataBinding
import com.bnkc.library.data.type.RunTimeDataStore
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.base.BaseActivity
import com.bnkc.sourcemodule.util.ComUtil
import com.mobile.bnkcl.ui.cscenter.AskBNKCDetailActivity
import com.mobile.bnkcl.ui.intro.IntroActivity
import com.mobile.bnkcl.ui.notice.NoticeActivity

/**
 * @author chanyouvita
 * @since 2020. 02. 10
 */
class PushEmptyActivity : BaseActivity<ViewDataBinding>() {

    @RequiresApi(api = Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            val intent: Intent
            if (checkFirstRunning() > 1) {
                runOnUiThread(Runnable {
                    /**
                     * function to open Ask BNKC detail from push notification
                     * case action_type is equal "1" : ASK BNKC
                     * case action_type is equal "2" : Loan Consultation
                     * case action_type is equal "3" : Notice (WebView)
                     */
                    /**
                     * function to open Ask BNKC detail from push notification
                     * case action_type is equal "1" : ASK BNKC
                     * case action_type is equal "2" : Loan Consultation
                     * case action_type is equal "3" : Notice (WebView)
                     */
                    /**
                     * function to open Ask BNKC detail from push notification
                     * case action_type is equal "1" : ASK BNKC
                     * case action_type is equal "2" : Loan Consultation
                     * case action_type is equal "3" : Notice (WebView)
                     */
                    /**
                     * function to open Ask BNKC detail from push notification
                     * case action_type is equal "1" : ASK BNKC
                     * case action_type is equal "2" : Loan Consultation
                     * case action_type is equal "3" : Notice (WebView)
                     */
                    val actionType: String = RunTimeDataStore.ACTION_TYPE
                    if (actionType.isNotEmpty()) {
                        when (actionType) {
                            "1", "2" -> {
                                val actionId: String = RunTimeDataStore.ACTION_ID
                                if (actionId.isNotEmpty()) {
                                    val intent = Intent(
                                        this@PushEmptyActivity,
                                        AskBNKCDetailActivity::class.java
                                    )
                                    intent.putExtra("CLAIM_ID", actionId)
                                    /**
                                     * Type from Notification:
                                     * 1: Ask BNKC Question
                                     * 2: Loan Consultation
                                     * 3: Notice (Web)
                                     *
                                     * Claim Category in Detail:
                                     * 1: Question
                                     * 2: Comment
                                     * 3: Loan Consultation
                                     */
                                    /**
                                     * Type from Notification:
                                     * 1: Ask BNKC Question
                                     * 2: Loan Consultation
                                     * 3: Notice (Web)
                                     *
                                     * Claim Category in Detail:
                                     * 1: Question
                                     * 2: Comment
                                     * 3: Loan Consultation
                                     */
                                    /**
                                     * Type from Notification:
                                     * 1: Ask BNKC Question
                                     * 2: Loan Consultation
                                     * 3: Notice (Web)
                                     *
                                     * Claim Category in Detail:
                                     * 1: Question
                                     * 2: Comment
                                     * 3: Loan Consultation
                                     */
                                    /**
                                     * Type from Notification:
                                     * 1: Ask BNKC Question
                                     * 2: Loan Consultation
                                     * 3: Notice (Web)
                                     *
                                     * Claim Category in Detail:
                                     * 1: Question
                                     * 2: Comment
                                     * 3: Loan Consultation
                                     */
                                    intent.putExtra(
                                        "CATEGORY",
                                        if (actionType == "1") "1" else "3"
                                    )
                                    intent.putExtra("CATEGORY_NAME", "")
                                    startActivity(intent)
                                    RunTimeDataStore.ActionType.value = ""
                                    RunTimeDataStore.ActionId.value = ""
                                    RunTimeDataStore.ActionUrl.value = ""
                                }
                            }
                            "3" -> {
                                val actionUrl: String = RunTimeDataStore.ACTION_URL
                                if (actionUrl.isNotEmpty()) {
                                    val openNotice =
                                        Intent(this@PushEmptyActivity, NoticeActivity::class.java)
                                    openNotice.putExtra(Constants.HandlePush.ACTION_URL, actionUrl)
                                    startActivity(openNotice)
                                }
                            }
                            else -> {
                            }
                        }
                    }
                })
                finish()
            } else {
                ComUtil.setBadge(this, 0)

                // Intro 부터
                intent = Intent(this, IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("NewApi")
    private fun checkFirstRunning(): Int {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        var mRunningActivity = 0
        val tasks = activityManager.appTasks
        mRunningActivity = tasks[0].taskInfo.numActivities
        return mRunningActivity
    }

    override fun getLayoutId(): Int {
        return 0
    }
}