package com.mobile.bnkcl.ui.dialog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bnkc.sourcemodule.base.BaseActivity
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityDifferentPhoneNumberDialogBinding

class DifferentPhoneNumberDialog : BaseActivity<ActivityDifferentPhoneNumberDialogBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_different_phone_number_dialog
    }
}