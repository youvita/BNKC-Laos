package com.mobile.bnkcl.ui.user

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.bnkc.library.data.type.AppLogin
import com.bnkc.library.data.type.ErrorCode
import com.bnkc.library.data.type.RunTimeDataStore
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.app.Constants.ANIMATE_LEFT
import com.bnkc.sourcemodule.base.BaseActivity
import com.bnkc.sourcemodule.dialog.AlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.response.code.CodesData
import com.mobile.bnkcl.data.response.user.ProfileData
import com.mobile.bnkcl.databinding.ActivityAccountInformationBinding
import com.mobile.bnkcl.ui.dialog.LogOutDialog
import com.mobile.bnkcl.ui.home.HomeActivity
import com.mobile.bnkcl.ui.pinview.PinCodeActivity
import com.mobile.bnkcl.ui.user.edit.EditAccountInfoActivity
import com.mobile.bnkcl.utilities.FormatUtil
import com.mobile.bnkcl.utilities.UtilAnimation
import com.mobile.bnkcl.utilities.UtilsGlide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountInformationActivity : BaseActivity<ActivityAccountInformationBinding>(),
    View.OnClickListener {

//    @Inject
//    lateinit var systemDialog: SystemDialog

    private val viewModel: AccountInformationViewModel by viewModels()
    private var profileData: ProfileData? = ProfileData()
    private val logOutDialog = LogOutDialog()
    private var jobTypeList: ArrayList<CodesData>? = ArrayList()
    private var isUpdateProfile: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        setStatusBarColor(ContextCompat.getColor(this, R.color.color_f5f7fc))
        setAnimateType(ANIMATE_LEFT)
        super.onCreate(savedInstanceState)

        initView()
        initLiveData()
        handleError()

        if (!sharedPrefer.getPrefer(Constants.USER_ID).isNullOrEmpty()) {

            viewModel.getJobTypeCodes()

            logOutDialog.onConfirmClickedListener {
                viewModel.logout()
                showLoading(true)
            }
        }
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
                        binding.tvOccupation.text = it.codes[i].title
                    }
                }
            }
        }

        viewModel.accountInformationLiveData.observe(this) {
            binding.profile = it
            profileData = it

            binding.tvPhoneNumber.text = FormatUtil.getTel(it.phoneNumber!!)
            setAddress(profileData!!)
        }

        viewModel.logoutLiveData.observe(this) {
            RunTimeDataStore.LoginToken.value = ""
            sharedPrefer.remove(Constants.USER_ID)
            sharedPrefer.remove(Constants.CUST_NO)
            sharedPrefer.remove(Constants.IMAGE_BITMAP)
            sharedPrefer.remove("name")

            val intent = Intent(this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            AppLogin.PIN.code = "N"
        }
    }

    private fun initView() {

        Glide.with(this)
            .load(R.drawable.loading)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(DrawableImageViewTarget(binding.ivLoading))

        UtilsGlide.loadCircle(this, binding.ivProfile, binding.ivLoading)

        if (intent != null) {
            profileData = intent.getSerializableExtra("ACCOUNT_INFO") as ProfileData?
            binding.profile = profileData

            binding.tvPhoneNumber.text =
                (if (!profileData?.phoneNumber.isNullOrEmpty()) FormatUtil.getTel(profileData?.phoneNumber!!) else "").toString()
            setAddress(profileData!!)
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

    private fun setAddress(profileData: ProfileData) {
        if (null != profileData.address) {
            var moreInfo = ""
            var state = ""
            var district = ""
            var village = ""

            if (null != profileData.address!!.more_info) moreInfo =
                profileData.address!!.more_info!!
            if (null != profileData.address!!.state?.alias1) state =
                profileData.address!!.state?.alias1!!
            if (null != profileData.address!!.district?.alias1) district =
                profileData.address!!.district?.alias1!!
            if (null != profileData.address!!.village?.alias1) village =
                profileData.address!!.village?.alias1!!
            if (state.isEmpty()) binding.tvAddress.text =
                getString(R.string.not_available) else {
                val address = "$moreInfo, $village, $district, $state"
                binding.tvAddress.text = address
            }
        }

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
                    resultLauncher.launch(intent)
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

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra("IS_UPDATE_PROFILE", isUpdateProfile)
        if (isUpdateProfile)
            setResult(RESULT_OK, intent)
        finish()
    }

    /**
     * replace for deprecated onActivityResult
     */
    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result.data != null) {
                    isUpdateProfile = result.data!!.getBooleanExtra("IS_UPDATE_PROFILE", false)
                    if (isUpdateProfile) {
                        Glide.with(this)
                            .load(R.drawable.loading)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(DrawableImageViewTarget(binding.ivLoading))

                        UtilsGlide.loadCircle(
                            this@AccountInformationActivity,
                            binding.ivProfile,
                            binding.ivLoading
                        )
                        viewModel.getAccountInformation()
                        showLoading(true)
                    }
                }
            }
        }

    /**
     * catch error
     */
    private fun handleError() {
        viewModel.handleError.observe(this) {
            val error = getErrorMessage(it)
            alertDialog = AlertDialog.newInstance(
                error.icon!!,
                error.code!!,
                error.message!!,
                error.button!!
            )
            alertDialog.show(supportFragmentManager, alertDialog.tag)
            alertDialog.onConfirmClicked {
                // session expired
                if (error.code == ErrorCode.UNAUTHORIZED) {
                    RunTimeDataStore.LoginToken.value = ""
                    startActivity(Intent(this, PinCodeActivity::class.java))
                    finish()
                }
            }
        }
    }

}