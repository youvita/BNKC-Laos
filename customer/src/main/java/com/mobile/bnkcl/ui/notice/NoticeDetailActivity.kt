package com.mobile.bnkcl.ui.notice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bnkc.sourcemodule.base.BaseActivity
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityNoticeDetailBinding


class NoticeDetailActivity : BaseActivity<ActivityNoticeDetailBinding>() {

    override fun getLayoutId(): Int= R.layout.activity_notice_detail
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}