package com.mobile.bnkcl.ui.user

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.bnkc.sourcemodule.base.BaseActivity
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityAccountInformationBinding
import com.mobile.bnkcl.ui.dialog.LogOutDialog
import com.mobile.bnkcl.ui.user.edit.EditAccountInfoActivity
import com.mobile.bnkcl.utilities.UtilAnimation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountInformationActivity : BaseActivity<ActivityAccountInformationBinding>(), View.OnClickListener {

    private val accountInformationViewModel: AccountInformationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setClickListeners()

//        accountInformationViewModel.getAccountInformation()
//        accountInformationViewModel.accountInformationLiveData.observe(this) {
//            binding.profile = it
//        }
    }

    private fun setClickListeners() {
        binding.llViewMorePersonal.setOnClickListener(this)
        binding.llMoreLeaseInfo.setOnClickListener(this)
        binding.llAdditionalInfo.setOnClickListener(this)
        binding.tvTitleToolbar02.setOnClickListener(this)
        binding.btnLogout.setOnClickListener(this)
        binding.ivBack.setOnClickListener(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_account_information
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.iv_back -> {
                    onBackPressed()
                }
                R.id.tv_title_toolbar_02 -> {
                    startActivity(Intent(applicationContext, EditAccountInfoActivity::class.java))
                }
                R.id.btn_logout -> {
                    val logOutDialog = LogOutDialog()
                    logOutDialog.show(supportFragmentManager, logOutDialog.tag)
                }
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
}