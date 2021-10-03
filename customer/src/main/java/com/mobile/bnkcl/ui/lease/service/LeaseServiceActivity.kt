package com.mobile.bnkcl.ui.lease.service

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.bnkc.library.data.type.AppLogin
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.base.BaseActivity
import com.bnkc.sourcemodule.dialog.AlertDialog
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityLeaseServiceBinding
import com.mobile.bnkcl.ui.lease.apply.ApplyLeaseActivity
import com.mobile.bnkcl.ui.lease.calculate.LeaseCalculateActivity
import com.mobile.bnkcl.ui.otp.OtpActivity
import com.mobile.bnkcl.ui.pinview.PinCodeActivity
import com.mobile.bnkcl.utilities.UtilAnimation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LeaseServiceActivity : BaseActivity<ActivityLeaseServiceBinding>() {

    val viewModel : LeaseServiceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setStatusBarColor(ContextCompat.getColor(this, R.color.color_f5f7fc))
        setAnimateType(Constants.ANIMATE_LEFT)
        super.onCreate(savedInstanceState)
        binding.leaseServiceViewModel = viewModel

        observeViewModel()
        initView()
        initEvent()
    }

    private fun observeViewModel(){
        viewModel.actLiveData.observe(this, {
            when (it) {
                LeaseCalculateActivity::class.simpleName -> {
                    startActivity(Intent(this, LeaseCalculateActivity::class.java))
                }
                ApplyLeaseActivity::class.simpleName -> {
                    // check login status
                    Log.d(">>>>>>>", "Apply :: " + AppLogin.PIN_TYPE)
                    if (AppLogin.PIN.code == "N"){
                        if (sharedPrefer.getPrefer(Constants.USER_ID).isNullOrEmpty()){
                            val intent = Intent(this, OtpActivity::class.java)
                            intent.putExtra("ACTION_TAG", "LOGIN")
                            intent.putExtra("from", LeaseServiceActivity::class.java.simpleName)
                            startActivity(intent)
                        }else{
                            val intent = Intent(this, PinCodeActivity::class.java)
                            intent.putExtra("pin_action", "login")
                            intent.putExtra("from", LeaseServiceActivity::class.java.simpleName)
                            intent.putExtra("username", sharedPrefer.getPrefer(Constants.USER_ID))
                            startActivity(intent)
                        }
                    }else{
                        startActivity(Intent(this, ApplyLeaseActivity::class.java))
                    }

                }
                else -> {

                }
            }
        })

        viewModel.handleError.observe(this) {
            val error = getErrorMessage(it)
            alertDialog = AlertDialog.newInstance(error.icon!!, error.code!!, error.message!!, error.button!!)
            alertDialog.show(supportFragmentManager, alertDialog.tag)
        }

    }

//    override fun onResume() {
//        super.onResume()
//        Log.d(">>>>>>>", "onResume :: " + AppLogin.InterceptIntent.code)
//        if (AppLogin.InterceptIntent.code == "Y"){
//            startActivity(Intent(this, ApplyLeaseActivity::class.java))
//            AppLogin.InterceptIntent.code = "N"
//        }
//    }

    private fun initView(){
        binding.include.colToolbar.title = getString(R.string.lease_service)
        binding.include.toolbarLeftButton.setOnClickListener {
            finish()
        }
    }

    private fun initEvent(){
        binding.llProContainer.setOnClickListener(View.OnClickListener {
            val matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                binding.llProContainer.width,
                View.MeasureSpec.EXACTLY
            )
            val wrapContentMeasureSpec =
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            binding.llProductIntro.measure(matchParentMeasureSpec, wrapContentMeasureSpec)
            if (binding.llProductIntro.visibility == View.VISIBLE) {
                UtilAnimation.collapse(
                    binding.llProductIntro,
                    binding.llProductIntro.measuredHeight,
                    300
                )
                binding.ivState.setImageResource(R.drawable.ic_unfold_ico)
            } else {
                UtilAnimation.expand(
                    binding.llProductIntro,
                    binding.llProductIntro.measuredHeight,
                    300
                )
                binding.ivState.setImageResource(R.drawable.ic_fold_ico)
            }
        })

        binding.lytFaqs.llFaqs.setOnClickListener(View.OnClickListener {
            val matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                binding.lytFaqs.llFaqs.width,
                View.MeasureSpec.EXACTLY
            )
            val wrapContentMeasureSpec =
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            binding.lytFaqs.llFaqsDes.measure(
                matchParentMeasureSpec,
                wrapContentMeasureSpec
            )
            if (binding.lytFaqs.llFaqsDes.visibility == View.VISIBLE) {
                UtilAnimation.collapse(
                    binding.lytFaqs.llFaqsDes,
                    binding.lytFaqs.llFaqsDes.measuredHeight,
                    300
                )
                binding.lytFaqs.ivFaqs.setImageResource(R.drawable.ic_unfold_ico)
            } else {
                UtilAnimation.expand(
                    binding.lytFaqs.llFaqsDes,
                    binding.lytFaqs.llFaqsDes.measuredHeight,
                    300
                )
                binding.lytFaqs.ivFaqs.setImageResource(R.drawable.ic_fold_ico)
            }
        })

        binding.lytLoanProcess.llLoanProcess.setOnClickListener(View.OnClickListener {
            val matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                binding.lytLoanProcess.llLoanProcess.width,
                View.MeasureSpec.EXACTLY
            )
            val wrapContentMeasureSpec =
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            binding.lytLoanProcess.llProcessDes.measure(
                matchParentMeasureSpec,
                wrapContentMeasureSpec
            )
            if (binding.lytLoanProcess.llProcessDes.visibility == View.VISIBLE) {
                UtilAnimation.collapse(
                    binding.lytLoanProcess.llProcessDes,
                    binding.lytLoanProcess.llProcessDes.measuredHeight,
                    300
                )
                binding.lytLoanProcess.ivProcess.setImageResource(R.drawable.ic_unfold_ico)
            } else {
                UtilAnimation.expand(
                    binding.lytLoanProcess.llProcessDes,
                    binding.lytLoanProcess.llProcessDes.measuredHeight,
                    300
                )
                binding.lytLoanProcess.ivProcess.setImageResource(R.drawable.ic_fold_ico)
            }
        })
        binding.lytDocument.llRequirementDocument.setOnClickListener(View.OnClickListener {
            val matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                binding.lytDocument.llRequirementDocument.width,
                View.MeasureSpec.EXACTLY
            )
            val wrapContentMeasureSpec =
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            binding.lytDocument.llDocumentDes.measure(
                matchParentMeasureSpec,
                wrapContentMeasureSpec
            )
            if (binding.lytDocument.llDocumentDes.visibility == View.VISIBLE) {
                UtilAnimation.collapse(
                    binding.lytDocument.llDocumentDes,
                    binding.lytDocument.llDocumentDes.measuredHeight,
                    300
                )
                binding.lytDocument.ivDoc.setImageResource(R.drawable.ic_unfold_ico)
            } else {
                UtilAnimation.expand(
                    binding.lytDocument.llDocumentDes,
                    binding.lytDocument.llDocumentDes.measuredHeight,
                    300
                )
                binding.lytDocument.ivDoc.setImageResource(R.drawable.ic_fold_ico)
            }
        })

        binding.btnLeaseCalculator.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(this, LeaseCalculateActivity::class.java)
            )
        })

//        binding.btnApplyNow.setOnClickListener(View.OnClickListener {
//            val intent = Intent(this, ApplyLeaseActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            intent.putExtra("type", 1)
//            startActivity(intent)
//        })
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_lease_service
    }
}