package com.mobile.bnkcl.ui.lease.apply

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.bnkc.library.util.Constants
import com.bnkc.sourcemodule.base.BaseActivity
import com.bnkc.sourcemodule.dialog.ListChoiceDialog
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.response.lease.ItemResponseObject
import com.mobile.bnkcl.databinding.ActivityApplyLeaseBinding
import com.mobile.bnkcl.ui.success.ResultActivity
import com.mobile.bnkcl.utilities.FormatUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ApplyLeaseActivity : BaseActivity<ActivityApplyLeaseBinding>() {

    val viewModel : ApplyLeaseViewModel by viewModels()
    private var selectedItem = -1
    var repaymentSelected = -1;
    @Inject
    lateinit var listChoiceDialog: ListChoiceDialog
    lateinit var itemResponses : ArrayList<ItemResponseObject>



    override fun onCreate(savedInstanceState: Bundle?) {
        setStatusBarColor(ContextCompat.getColor(this, R.color.color_f5f7fc))
        super.onCreate(savedInstanceState)
        binding.applyViewModel = viewModel

        viewModel.reqLeaseItemCode(Constants.PRODUCT_TYPE)

        initView()
        observeViewModel()
    }

    private fun observeViewModel(){
        viewModel.productTypeLiveData.observe(this, {
            Log.d(">>>>>", "observeViewModel: $it")
            itemResponses = it.codes!!
            viewModel.setUpProductTypeData(itemResponses)

        })
        viewModel.applyLeaseLiveData.observe(this, {
            Log.d(">>>>>", "observeViewModel: $it")
            if (it.lease_application_id != null && it.lease_application_id!!.isNotEmpty()) {
                navigateResult(true)
            }

        })

        viewModel.actionLiveData.observe(this, {
            when (it) {
                "apply_lease" -> {
                    showLoading()
//                    viewModel.applyLeaseRequest = ApplyLeaseRequest(
//                        "LAT002",
//                        true,
//                        "zxc",
//                        "qwe",
//                        "trew",
//                        "13 pro max",
//                        "ios",
//                        "iphone",
//                        "LAK 100.00",
//                        "LAK 1234.00",
//                        "12"
//                    )
                    viewModel.applyLease()
                }
                "product_type" -> {
                    listChoiceDialog = ListChoiceDialog.newInstance(
                        R.drawable.ic_badge_error,
                        getString(R.string.lease_product_type),
                        viewModel.areaNames!!,
                        selectedItem
                    )

                    listChoiceDialog.setOnItemListener = { pos: Int ->
                        selectedItem = pos
                        binding.tvProType.setTextColor(
                            ContextCompat.getColor(
                                this,
                                R.color.color_263238
                            )
                        )
                        binding.tvProType.text = itemResponses[pos].title
                        viewModel.applyLeaseRequest?.product_type = itemResponses[pos].code
//                        viewModel.branchRequest = BranchRequest(objects!![p].id.toString(), 1, 10, "")
//                        viewModel.reqBranchList()
                    }
                    listChoiceDialog.isCancelable = true
                    listChoiceDialog.show(supportFragmentManager, listChoiceDialog.tag)
                }
                "repayment_term" -> {
                    listChoiceDialog = ListChoiceDialog.newInstance(
                        R.drawable.ic_badge_error,
                        getString(R.string.repayment_term),
                        viewModel.setUpRepaymentTermData(),
                        repaymentSelected
                    )

                    listChoiceDialog.setOnItemListener = { pos: Int ->
                        repaymentSelected = pos
                        binding.tvRepaymentTerm.setTextColor(
                            ContextCompat.getColor(
                                this,
                                R.color.color_263238
                            )
                        )
                        binding.tvRepaymentTerm.text = viewModel.setUpRepaymentTermData()[pos]
                        viewModel.applyLeaseRequest?.repayment_term =
                            viewModel.setUpRepaymentTermData()[pos].split(
                                " "
                            )[0]
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
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("from", ApplyLeaseActivity::class.java.simpleName)
        intent.putExtra("result", result)
        startActivity(intent)
    }

    private fun initView(){
        binding.include.colToolbar.title = getString(R.string.apply_lease)
        binding.edProPrice.addTextChangedListener(NumberTextWatcherForThousand(binding.edProPrice))
        binding.edReqAmt.addTextChangedListener(NumberTextWatcherForThousand(binding.edReqAmt))

        binding.edNameBrand.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val proType = if (viewModel.applyLeaseRequest?.product_type!!.isNotEmpty()){
                    viewModel.applyLeaseRequest?.product_type
                }else{
                    ""
                }
                val reqAmt = if (viewModel.applyLeaseRequest?.request_amount!!.isNotEmpty()){
                    viewModel.applyLeaseRequest?.request_amount
                }else{
                    ""
                }
                val term = if (viewModel.applyLeaseRequest?.repayment_term!!.isNotEmpty()){
                    viewModel.applyLeaseRequest?.repayment_term
                }else{
                    ""
                }
                binding.btnSubmit.isEnable(
                    proType!!,
                    s.toString(),
                    binding.edNameModel.text.toString(),
                    binding.edNameType.text.toString(),
                    binding.edNameModel.text.toString(),
                    binding.edEtcBrand.text.toString(),
                    binding.edEtcModel.text.toString(),
                    binding.edEtcType.text.toString(),
                    reqAmt!!,
                    term!!
                )
            }

            override fun afterTextChanged(s: Editable) {}
        })

        binding.edNameModel.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val proType = if (viewModel.applyLeaseRequest?.product_type!!.isNotEmpty()){
                    viewModel.applyLeaseRequest?.product_type
                }else{
                    ""
                }
                val reqAmt = if (viewModel.applyLeaseRequest?.request_amount!!.isNotEmpty()){
                    viewModel.applyLeaseRequest?.request_amount
                }else{
                    ""
                }
                val term = if (viewModel.applyLeaseRequest?.repayment_term!!.isNotEmpty()){
                    viewModel.applyLeaseRequest?.repayment_term
                }else{
                    ""
                }
                binding.btnSubmit.isEnable(
                    proType!!,
                    binding.edNameBrand.text.toString(),
                    s.toString(),
                    binding.edNameType.text.toString(),
                    binding.edNameModel.text.toString(),
                    binding.edEtcBrand.text.toString(),
                    binding.edEtcModel.text.toString(),
                    binding.edEtcType.text.toString(),
                    reqAmt!!,
                    term!!
                )
            }

            override fun afterTextChanged(s: Editable) {}
        })

        binding.edNameType.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val proType = if (viewModel.applyLeaseRequest?.product_type!!.isNotEmpty()){
                    viewModel.applyLeaseRequest?.product_type
                }else{
                    ""
                }
                val reqAmt = if (viewModel.applyLeaseRequest?.request_amount!!.isNotEmpty()){
                    viewModel.applyLeaseRequest?.request_amount
                }else{
                    ""
                }
                val term = if (viewModel.applyLeaseRequest?.repayment_term!!.isNotEmpty()){
                    viewModel.applyLeaseRequest?.repayment_term
                }else{
                    ""
                }
                binding.btnSubmit.isEnable(
                    proType!!,
                    binding.edNameBrand.text.toString(),
                    binding.edNameModel.text.toString(),
                    s.toString(),
                    binding.edNameModel.text.toString(),
                    binding.edEtcBrand.text.toString(),
                    binding.edEtcModel.text.toString(),
                    binding.edEtcType.text.toString(),
                    reqAmt!!,
                    term!!
                )
            }

            override fun afterTextChanged(s: Editable) {}
        })

        binding.edEtcBrand.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val proType = if (viewModel.applyLeaseRequest?.product_type!!.isNotEmpty()){
                    viewModel.applyLeaseRequest?.product_type
                }else{
                    ""
                }
                val reqAmt = if (viewModel.applyLeaseRequest?.request_amount!!.isNotEmpty()){
                    viewModel.applyLeaseRequest?.request_amount
                }else{
                    ""
                }
                val term = if (viewModel.applyLeaseRequest?.repayment_term!!.isNotEmpty()){
                    viewModel.applyLeaseRequest?.repayment_term
                }else{
                    ""
                }
                binding.btnSubmit.isEnable(
                    proType!!,
                    binding.edNameBrand.text.toString(),
                    binding.edNameModel.text.toString(),
                    binding.edNameType.text.toString(),
                    binding.edNameModel.text.toString(),
                    s.toString(),
                    binding.edEtcModel.text.toString(),
                    binding.edEtcType.text.toString(),
                    reqAmt!!,
                    term!!
                )
            }

            override fun afterTextChanged(s: Editable) {}
        })

        binding.edEtcModel.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val proType = if (viewModel.applyLeaseRequest?.product_type!!.isNotEmpty()){
                    viewModel.applyLeaseRequest?.product_type
                }else{
                    ""
                }
                val reqAmt = if (viewModel.applyLeaseRequest?.request_amount!!.isNotEmpty()){
                    viewModel.applyLeaseRequest?.request_amount
                }else{
                    ""
                }
                val term = if (viewModel.applyLeaseRequest?.repayment_term!!.isNotEmpty()){
                    viewModel.applyLeaseRequest?.repayment_term
                }else{
                    ""
                }
                binding.btnSubmit.isEnable(
                    proType!!,
                    binding.edNameBrand.text.toString(),
                    binding.edNameModel.text.toString(),
                    binding.edNameType.text.toString(),
                    binding.edNameModel.text.toString(),
                    binding.edEtcBrand.text.toString(),
                    s.toString(),
                    binding.edEtcType.text.toString(),
                    reqAmt!!,
                    term!!
                )
            }

            override fun afterTextChanged(s: Editable) {}
        })

        binding.edEtcType.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val proType = if (viewModel.applyLeaseRequest?.product_type!!.isNotEmpty()){
                    viewModel.applyLeaseRequest?.product_type
                }else{
                    ""
                }
                val reqAmt = if (viewModel.applyLeaseRequest?.request_amount!!.isNotEmpty()){
                    viewModel.applyLeaseRequest?.request_amount
                }else{
                    ""
                }
                val term = if (viewModel.applyLeaseRequest?.repayment_term!!.isNotEmpty()){
                    viewModel.applyLeaseRequest?.repayment_term
                }else{
                    ""
                }
                binding.btnSubmit.isEnable(
                    proType!!,
                    binding.edNameBrand.text.toString(),
                    binding.edNameModel.text.toString(),
                    binding.edNameType.text.toString(),
                    binding.edNameModel.text.toString(),
                    binding.edEtcBrand.text.toString(),
                    binding.edEtcModel.text.toString(),
                    s.toString(),
                    reqAmt!!,
                    term!!
                )
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_apply_lease
    }

    class NumberTextWatcherForThousand internal constructor(var editText: EditText) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) {
            val value = s.toString()
            try {
                editText.removeTextChangedListener(this)
                val fixedValue: String = FormatUtil.getDecimalFormattedString(value, false).toString()
                val preSelection = editText.selectionEnd
                s.replace(0, value.length, fixedValue)
                val selection = preSelection + fixedValue.length - value.length
//            DevLog.devLog(">>>>>>>", ">>>>>>>>>>> :: " + s.length)
                editText.setSelection(Math.max(s.length, 0))
//            try {
//                if (editText === mBinding.edLoanAmount) { //_Auto generate of amount when user input amount in foreign currency and exchange rate
//                    viewModel.setLOAN_AMOUNT(s.toString().replace(",".toRegex(), ""))
//                } else if (editText === mBinding.edMonthlyIncome) {
//                    viewModel.setMONTHLY_INCOME(s.toString().replace(",".toRegex(), ""))
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
                editText.addTextChangedListener(this)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}