package com.mobile.bnkcl.ui.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.base.BaseActivity
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.response.code.CodesData
import com.mobile.bnkcl.data.response.user.ProfileData
import com.mobile.bnkcl.databinding.ActivityAccountInformationBinding
import com.mobile.bnkcl.ui.dialog.LogOutDialog
import com.mobile.bnkcl.ui.home.HomeActivity
import com.mobile.bnkcl.ui.pinview.PinCodeActivity
import com.mobile.bnkcl.ui.user.edit.EditAccountInfoActivity
import com.mobile.bnkcl.utilities.UtilAnimation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountInformationActivity : BaseActivity<ActivityAccountInformationBinding>(),
    View.OnClickListener {

    private val viewModel: AccountInformationViewModel by viewModels()
    private var profileData: ProfileData? = ProfileData()
    private val logOutDialog = LogOutDialog()
    private var jobTypeList: ArrayList<CodesData>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initDisposable()
        initLiveData()

        if (!sharedPrefer.getPrefer(Constants.USER_ID).isNullOrEmpty()) {

            viewModel.getJobTypeCodes()

            logOutDialog.onConfirmClickedListener {
                viewModel.logout()
                showLoading()
            }
        }
    }

    private fun initDisposable() {
        disposable = RxJava.listen(RxEvent.SessionExpired::class.java).subscribe {
            errorSessionDialog(it.title, it.message).onConfirmClicked {
                sharedPrefer.putPrefer(Constants.KEY_TOKEN, "")
                startActivity(Intent(this, PinCodeActivity::class.java))
            }
        }

//        disposable = RxJava.listen(RxEvent.ServerError::class.java).subscribe {
//            errorDialog(it.code, it.title, it.message)
//        }
    }

    private fun initLiveData() {

        viewModel.jobTypesLiveData.observe(this) {
            jobTypeList = it.codes
            binding.tvTitleToolbar02.setOnClickListener(this)

            for (i in 0 until jobTypeList!!.size) {
                if (profileData!!.jobType.equals(it.codes!![i].code)) {
                    if (profileData!!.jobType.isNullOrEmpty()) {
                        binding.tvOccupation.text = resources.getString(R.string.not_available)
                    } else {
                        binding.tvOccupation.text = it.codes!![i].title
                    }
                }
            }

            successListener()
        }

        viewModel.accountInformationLiveData.observe(this) {
            binding.profile = it
            profileData = it
        }

        viewModel.logoutLiveData.observe(this) {
            sharedPrefer.remove(Constants.KEY_TOKEN)
            sharedPrefer.remove(Constants.USER_ID)

            val intent = Intent(this@AccountInformationActivity, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }

    private fun initView() {

        if (intent != null) {
            profileData = intent.getSerializableExtra("ACCOUNT_INFO") as ProfileData?
            binding.profile = profileData
        }

        binding.tvGender.text = ""
        binding.llWrapAdditionalInfo.visibility = View.GONE

        binding.llViewMorePersonal.setOnClickListener(this)
        binding.llMoreLeaseInfo.setOnClickListener(this)
        binding.llAdditionalInfo.setOnClickListener(this)
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

                    val intent = Intent(this, EditAccountInfoActivity::class.java)
                    intent.putExtra("ACCOUNT_INFO", profileData)
                    intent.putExtra("JOB_TYPE", jobTypeList)
                    startActivity(intent)
                }
                R.id.btn_logout -> {
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