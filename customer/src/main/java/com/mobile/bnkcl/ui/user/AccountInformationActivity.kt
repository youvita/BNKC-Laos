package com.mobile.bnkcl.ui.user

import android.os.Bundle
import android.view.View
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityAccountInformationBinding
import com.bnkc.sourcemodule.base.BaseActivity
import com.mobile.bnkcl.utilities.UtilAnimation

class AccountInformationActivity : BaseActivity<ActivityAccountInformationBinding>(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setClickListeners()
    }

    private fun setClickListeners() {
        binding.llViewMorePersonal.setOnClickListener(this)
        binding.llMoreLeaseInfo.setOnClickListener(this)
        binding.llAdditionalInfo.setOnClickListener(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_account_information
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.ll_view_more_personal -> {
                if (binding.llWrapPersonalInfo.visibility == View.VISIBLE) {
                    UtilAnimation.collapse(binding.llWrapPersonalInfo, 300)
                    binding.ivMorePersonal.setImageResource(R.drawable.ic_unfold_ico)
                } else {
                    UtilAnimation.expand(binding.llWrapPersonalInfo, 300)
                    binding.ivMorePersonal.setImageResource(R.drawable.ic_fold_ico)
                }
            }
            R.id.ll_more_lease_info -> {
                if (binding.llWrapLeaseInfo.visibility == View.VISIBLE) {
                    UtilAnimation.collapse(binding.llWrapLeaseInfo, 300)
                    binding.ivMoreLeaseInfo.setImageResource(R.drawable.ic_unfold_ico)
                } else {
                    UtilAnimation.expand(binding.llWrapLeaseInfo, 300)
                    binding.ivMoreLeaseInfo.setImageResource(R.drawable.ic_fold_ico)
                }
            }
            R.id.ll_additional_info -> {
                if (binding.llWrapAdditionalInfo.visibility == View.VISIBLE) {
                    UtilAnimation.collapse(binding.llWrapAdditionalInfo, 300)
                    binding.ivMoreAdditional.setImageResource(R.drawable.ic_unfold_ico)
                } else {
                    UtilAnimation.expand(binding.llWrapAdditionalInfo, 300)
                    binding.ivMoreAdditional.setImageResource(R.drawable.ic_fold_ico)
                }
            }
        }
    }
}