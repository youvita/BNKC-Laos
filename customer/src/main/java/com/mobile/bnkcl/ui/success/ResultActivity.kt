package com.mobile.bnkcl.ui.success

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bnkc.sourcemodule.base.BaseActivity
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityResultBinding


class ResultActivity : BaseActivity<ActivityResultBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_result
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}