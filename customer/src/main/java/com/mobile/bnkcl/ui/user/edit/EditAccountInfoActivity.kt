package com.mobile.bnkcl.ui.user.edit

import android.os.Bundle
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityEditAccountInfoBinding
import com.bnkc.sourcemodule.base.BaseActivity

class EditAccountInfoActivity : BaseActivity<ActivityEditAccountInfoBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initActionBar()

        initDisablePersonalInfo()
    }

    private fun initDisablePersonalInfo() {
        val res = resources.getIdentifier("round_solid_f3f6f7_8", "drawable", this.packageName)
        binding.edtName.setBackgroundResource(res)
        binding.llDob.setBackgroundResource(res)
        binding.llIdNumber.setBackgroundResource(res)

        binding.edtName.isEnabled = false
        binding.edtDob.isEnabled = false
        binding.llIdNumber.isEnabled = false
    }

    private fun initActionBar() {
        val actionBar = binding.actionBar
        actionBar.setBackgroundActionBar(resources.getColor(R.color.white))
        actionBar.setToolbarTitleWithActionBack(
            R.drawable.ic_nav_close_dark_btn,
            "Edit Account Information"
        )
        actionBar.setOnMenuLeftClick { onBackPressed() }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_edit_account_info
    }
}