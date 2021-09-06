package com.mobile.bnkcl.ui.lease.apply

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.bnkc.sourcemodule.base.BaseActivity
import com.bnkc.sourcemodule.dialog.ListChoiceDialog
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.request.findoffice.BranchRequest
import com.mobile.bnkcl.data.request.lease.apply.ApplyLeaseRequest
import com.mobile.bnkcl.databinding.ActivityApplyLeaseBinding
import com.mobile.bnkcl.ui.success.ResultActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ApplyLeaseActivity : BaseActivity<ActivityApplyLeaseBinding>() {

    val viewModel : ApplyLeaseViewModel by viewModels()
    private var selectedItem = 0;
    @Inject
    lateinit var listChoiceDialog: ListChoiceDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        setStatusBarColor(ContextCompat.getColor(this, R.color.color_f5f7fc))
        super.onCreate(savedInstanceState)
        binding.applyViewModel = viewModel

        initView()
        observeViewModel()
    }

    private fun observeViewModel(){
        viewModel.applyLeaseLiveData.observe(this, {
            Log.d(">>>>>", "observeViewModel: $it")
            navigateResult(true)
        })

        viewModel.actionLiveData.observe(this, {
            when(it){
                "apply_lease" -> {
                    showLoading()
                    viewModel.applyLeaseRequest = ApplyLeaseRequest(
                        "LAT001",
                        false,
                        "13 pro max",
                        "ios",
                        "iphone",
                        "100 USD",
                        "1000 USD",
                        "12"
                    )
                    viewModel.applyLease()
                }
                "product_type" -> {
                    listChoiceDialog = ListChoiceDialog.newInstance(
                        R.drawable.ic_badge_error,
                        getString(R.string.area),
                        viewModel.setUpProductTypeData(),
                        selectedItem
                    )

                    listChoiceDialog.setOnItemListener = {
                            pos : Int ->
                        selectedItem = pos
                        binding.tvProType.text = viewModel.setUpProductTypeData()[pos]
//                        viewModel.branchRequest = BranchRequest(objects!![p].id.toString(), 1, 10, "")
//                        viewModel.reqBranchList()
                    }
                    listChoiceDialog.isCancelable = true
                    listChoiceDialog.show(supportFragmentManager, listChoiceDialog.tag)
                }
                "repayment_term" -> {
                    listChoiceDialog = ListChoiceDialog.newInstance(
                        R.drawable.ic_badge_error,
                        getString(R.string.area),
                        viewModel.setUpRepaymentTermData(),
                        selectedItem
                    )

                    listChoiceDialog.setOnItemListener = {
                            pos : Int ->
                        selectedItem = pos
                        binding.tvProType.text = viewModel.setUpRepaymentTermData()[pos]
//                        viewModel.branchRequest = BranchRequest(objects!![p].id.toString(), 1, 10, "")
//                        viewModel.reqBranchList()
                    }
                    listChoiceDialog.isCancelable = true
                    listChoiceDialog.show(supportFragmentManager, listChoiceDialog.tag)
                }
            }
        })
    }

    private fun navigateResult(result: Boolean) {
        val intent = Intent (this, ResultActivity::class.java)
        intent.putExtra("from", ApplyLeaseActivity::class.java.simpleName)
        intent.putExtra("result", result)
        startActivity(intent)
    }

    private fun initView(){
        binding.include.colToolbar.title = "Apply Lease"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_apply_lease
    }
}