package com.mobile.bnkcl.ui.user.edit

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import com.bnkc.sourcemodule.base.BaseActivity
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.response.user.EditAccountInfoData
import com.mobile.bnkcl.data.response.user.ProfileData
import com.mobile.bnkcl.databinding.ActivityEditAccountInfoBinding
import com.mobile.bnkcl.ui.dialog.AlertEditInfoDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditAccountInfoActivity : BaseActivity<ActivityEditAccountInfoBinding>(),
    View.OnClickListener {

    private var profileData: ProfileData? = null
    private var isUpdate: Boolean? = null
    private val editAccountInfoViewModel: EditAccountInfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        profileData = ProfileData()

        initActionBar()
        initDisablePersonalInfo()

        if (intent != null) {
            profileData = intent.getSerializableExtra("ACCOUNT_INFO") as ProfileData?
            binding.profile = profileData
            Log.d(">>>", "onCreate: " + profileData!!.name)
        }



        binding.lytAddressInfo.tvJobType.text = profileData!!.job_type

        binding.btnCancel.setOnClickListener(this)
        binding.btnSave.setOnClickListener(this)
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
            getString(R.string.edit_acc_info)
        )
        isUpdate = true
        actionBar.setOnMenuLeftClick {
            if (isUpdate as Boolean) {
                alertNotice()
            } else {
                onBackPressed()
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_edit_account_info
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_cancel -> {
                alertNotice()
            }
            R.id.btn_save -> {
                val editAccountInfoData = EditAccountInfoData()
                editAccountInfoData.job_type = binding.lytAddressInfo.tvJobType.text.toString()
                editAccountInfoData.bank_name = binding.lytAddressInfo.edtBankName.text.toString()
                editAccountInfoData.account_number =
                    binding.lytAddressInfo.edtAccountNumber.text.toString()
                editAccountInfoViewModel.isUpdate(editAccountInfoData)

                editAccountInfoViewModel.editAccountInfoLiveData.observe(this) {

                }
            }
            R.id.cv_change_profile -> {
            }
            R.id.image_profile -> {

            }
        }
    }

    private fun alertNotice() {
        val alertEditInfoDialog = AlertEditInfoDialog()
        alertEditInfoDialog.onConfirmClickedListener { onBackPressed() }
        alertEditInfoDialog.show(supportFragmentManager, alertEditInfoDialog.tag)
    }

}