package com.mobile.bnkcl.ui.user

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.bnkc.library.data.type.RunTimeDataStore
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.app.Constants.ANIMATE_LEFT
import com.bnkc.sourcemodule.base.BaseActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.response.code.CodesData
import com.mobile.bnkcl.data.response.user.ProfileData
import com.mobile.bnkcl.databinding.ActivityAccountInformationBinding
import com.mobile.bnkcl.ui.dialog.LogOutDialog
import com.mobile.bnkcl.ui.intro.IntroActivity
import com.mobile.bnkcl.ui.pinview.PinCodeActivity
import com.mobile.bnkcl.ui.user.edit.EditAccountInfoActivity
import com.mobile.bnkcl.utilities.UtilAnimation
import com.mobile.bnkcl.utilities.UtilsGlide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountInformationActivity : BaseActivity<ActivityAccountInformationBinding>(),
    View.OnClickListener {

    private val viewModel: AccountInformationViewModel by viewModels()
    private var profileData: ProfileData? = ProfileData()
    private val logOutDialog = LogOutDialog()
    private var jobTypeList: ArrayList<CodesData>? = ArrayList()
    private val REQUEST_CODE = 1001
    private var isUpdateProfile: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        setStatusBarColor(ContextCompat.getColor(this, R.color.color_f5f7fc))
        setAnimateType(ANIMATE_LEFT)
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
                RunTimeDataStore.LoginToken.value = ""
                startActivity(Intent(this, PinCodeActivity::class.java))
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
            successListener()
        }

        viewModel.logoutLiveData.observe(this) {
            RunTimeDataStore.LoginToken.value = ""
            sharedPrefer.remove(Constants.USER_ID)
            successListener()

            val intent = Intent(this, IntroActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            Runtime.getRuntime().exit(0)
        }
    }

    private fun initView() {

        Glide.with(this)
            .load(R.drawable.rotate_loading_image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into<DrawableImageViewTarget>(DrawableImageViewTarget(binding.ivLoading))
        val rotation = AnimationUtils.loadAnimation(this, R.anim.rotate_circle_loading)
        rotation.fillAfter = true
        binding.ivLoading.startAnimation(rotation)
        UtilsGlide.loadCircle(this, binding.ivProfile, binding.ivLoading)

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
                    startActivityForResult(intent, REQUEST_CODE)
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
        val intentBack = Intent()
        intentBack.putExtra("IS_UPDATE_PROFILE", isUpdateProfile)
        setResult(REQUEST_CODE, intentBack)
        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_CANCELED) return
        if (requestCode == REQUEST_CODE) {
            if (data != null) {
                isUpdateProfile = data.getBooleanExtra("IS_UPDATE_PROFILE", false)
                if (isUpdateProfile) {
                    Glide.with(this)
                        .load(R.drawable.rotate_loading_image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into<DrawableImageViewTarget>(DrawableImageViewTarget(binding.ivLoading))
                    val rotation = AnimationUtils.loadAnimation(this, R.anim.rotate_circle_loading)
                    rotation.fillAfter = true
                    binding.ivLoading.startAnimation(rotation)
                    UtilsGlide.loadCircle(
                        this@AccountInformationActivity,
                        binding.ivProfile,
                        binding.ivLoading
                    )
                    viewModel.getAccountInformation()
                    showLoading()
                }
            }
        }
    }
}