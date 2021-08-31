package com.mobile.bnkcl.ui.lease.service

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.bnkc.sourcemodule.base.BaseActivity
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityLeaseServiceBinding
import com.mobile.bnkcl.ui.lease.ApplyLeaseActivity
import com.mobile.bnkcl.ui.lease.calculate.LeaseCalculateActivity
import com.mobile.bnkcl.utilities.UtilAnimation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LeaseServiceActivity : BaseActivity<ActivityLeaseServiceBinding>() {

    val viewModel : LeaseServiceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setStatusBarColor(ContextCompat.getColor(this, R.color.color_f5f7fc))
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
                    startActivity(Intent(this, ApplyLeaseActivity::class.java))
                }
                else -> {

                }
            }
        })
    }

    private fun initView(){
        binding.include.colToolbar.title = "Lease Service"
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

        binding.btnLoanCalculator.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(this, LeaseCalculateActivity::class.java)
            )
        })

        binding.btnApplyLoan.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, ApplyLeaseActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.putExtra("type", 1)
            startActivity(intent)
        })
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_lease_service
    }
}