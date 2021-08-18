package com.mobile.bnkcl.ui.notice

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.bnkc.sourcemodule.base.BaseActivity
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.request.notice.NoticeRequest
import com.mobile.bnkcl.databinding.ActivityNoticeBinding
import com.mobile.bnkcl.ui.adapter.NoticeAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NoticeActivity : BaseActivity<ActivityNoticeBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_notice
    private val noticeViewModel:NoticeViewModel by viewModels()

    private lateinit var noticeRequest: NoticeRequest
    @Inject
    lateinit var noticeAdapter: NoticeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noticeRequest = NoticeRequest()

        getNoticeData()
    }

    private fun getNoticeData() {
        try {
            noticeViewModel.getNoticeData(noticeRequest)
            noticeViewModel.notice.observe(this){
                if (it != null) {
                    Log.d("zimah", "getPreSignUpData: ${it.notices}")
                }
            }

        }catch (e: Exception){
            e.printStackTrace()
        }
    }

}