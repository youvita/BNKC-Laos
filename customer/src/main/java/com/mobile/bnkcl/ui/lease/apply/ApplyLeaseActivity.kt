package com.mobile.bnkcl.ui.lease.apply

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.RadioGroup
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.bnkc.library.data.type.RunTimeDataStore
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.library.util.Constants
import com.bnkc.sourcemodule.base.BaseActivity
import com.bnkc.sourcemodule.dialog.ListChoiceDialog
import com.bnkc.sourcemodule.dialog.TwoButtonDialog
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.response.lease.ItemResponseObject
import com.mobile.bnkcl.data.request.lease.apply.ApplyLeaseRequest
import com.mobile.bnkcl.databinding.ActivityApplyLeaseBinding
import com.mobile.bnkcl.ui.dialog.LogOutDialog
import com.mobile.bnkcl.ui.pinview.PinCodeActivity
import com.mobile.bnkcl.ui.success.ResultActivity
import com.mobile.bnkcl.utilities.FormatUtil
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ApplyLeaseActivity : BaseActivity<ActivityApplyLeaseBinding>() {

    val viewModel : ApplyLeaseViewModel by viewModels()
    private var selectedItem = -1
    var repaymentSelected = -1;
    @Inject
    lateinit var listChoiceDialog: ListChoiceDialog
    lateinit var itemResponses : ArrayList<ItemResponseObject>
    lateinit var repaymentCodes : ArrayList<ItemResponseObject>

    @Inject lateinit var twoButtonDialog : TwoButtonDialog

    private var disposableError: Disposable? = null
    private fun checkError(){
        //Session expired
        disposableError = RxJava.listen(RxEvent.SessionExpired::class.java).subscribe{
            errorSessionDialog(it.title, it.message).onConfirmClicked {
                RunTimeDataStore.LoginToken.value = ""//clear token when session expired
                startActivity(Intent(this, PinCodeActivity::class.java))
            }
        }
    }

    //$dnf repoquery -q clang :: commnd

    override fun onCreate(savedInstanceState: Bundle?) {
        setStatusBarColor(ContextCompat.getColor(this, R.color.color_f5f7fc))
        setAnimateType(com.bnkc.sourcemodule.app.Constants.ANIMATE_LEFT)
        super.onCreate(savedInstanceState)
        binding.applyViewModel = viewModel
        checkError()

        viewModel.reqLeaseItemCode(Constants.PRODUCT_TYPE)
        viewModel.reqRepaymentCode(Constants.REPAYMENT_TERM)

        initView()
        initEvent()
        observeViewModel()
    }

    private fun initEvent(){
        binding.btnSubmit.setOnClickListener {
            if (binding.btnSubmit.isActive()){
                viewModel.applyLeaseClicked()
            }
        }
        binding.include.toolbarLeftButton.setOnClickListener {
            finish()
        }
    }

    private fun observeViewModel(){
        viewModel.productTypeLiveData.observe(this, {
            itemResponses = it.codes!!
            viewModel.setUpProductTypeData(itemResponses)

        })
        viewModel.repaymentLiveData.observe(this, {
            repaymentCodes = it.codes!!
            viewModel.setUpRepaymentData(repaymentCodes)
        })
        viewModel.applyLeaseLiveData.observe(this, {
            if (it.lease_application_id != null && it.lease_application_id!!.isNotEmpty()) {
                navigateResult(true)
            }

        })

        viewModel.actionLiveData.observe(this, {
            when (it) {
                "apply_lease" -> {
                    twoButtonDialog.onConfirmClickedListener {
                        showLoading()
                        viewModel.applyLease()
                    }
                    twoButtonDialog.show(this.supportFragmentManager, twoButtonDialog.tag)

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
                        viewModel.applyLeaseRequest.product_type = itemResponses[pos].code

                        val proPrice = if (viewModel.applyLeaseRequest.product_price != null){
                            viewModel.applyLeaseRequest.product_price
                        }else{
                            ""
                        }
                        val reqAmt = if (viewModel.applyLeaseRequest.request_amount != null){
                            viewModel.applyLeaseRequest.request_amount
                        }else{
                            ""
                        }
                        val term = if (viewModel.applyLeaseRequest.repayment_term != null){
                            viewModel.applyLeaseRequest.repayment_term
                        }else{
                            ""
                        }
                        binding.btnSubmit.isEnable(
                            itemResponses[pos].code!!,
                            binding.edNameBrand.text.toString(),
                            binding.edNameModel.text.toString(),
                            binding.edNameType.text.toString(),
                            binding.edEtcBrand.text.toString(),
                            binding.edEtcModel.text.toString(),
                            binding.edEtcType.text.toString(),
                            reqAmt!!,
                            proPrice!!,
                            term!!.toString()
                        )
                    }
                    listChoiceDialog.isCancelable = true
                    listChoiceDialog.show(supportFragmentManager, listChoiceDialog.tag)
                }
                "repayment_term" -> {
                    listChoiceDialog = ListChoiceDialog.newInstance(
                        R.drawable.ic_badge_error,
                        getString(R.string.repayment_term),
                        viewModel.repaymentData!!,
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
                        binding.tvRepaymentTerm.text = repaymentCodes[pos].title
                        viewModel.applyLeaseRequest.repayment_term =
                            repaymentCodes[pos].code!!.toInt()

                        val proType = if (viewModel.applyLeaseRequest.product_type != null){
                            viewModel.applyLeaseRequest.product_type
                        }else{
                            ""
                        }
                        val proPrice = if (viewModel.applyLeaseRequest.product_price != null){
                            viewModel.applyLeaseRequest.product_price
                        }else{
                            ""
                        }
                        val reqAmt = if (viewModel.applyLeaseRequest.request_amount != null){
                            viewModel.applyLeaseRequest.request_amount
                        }else{
                            ""
                        }
                        binding.btnSubmit.isEnable(
                            proType!!,
                            binding.edNameBrand.text.toString(),
                            binding.edNameModel.text.toString(),
                            binding.edNameType.text.toString(),
                            binding.edEtcBrand.text.toString(),
                            binding.edEtcModel.text.toString(),
                            binding.edEtcType.text.toString(),
                            reqAmt!!,
                            proPrice!!,
                            repaymentCodes[pos].code.toString()
                        )

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
        viewModel.applyLeaseRequest.etc_status = binding.cbEtc.isChecked
        binding.edProPrice.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val proType = if (viewModel.applyLeaseRequest.product_type != null){
                    viewModel.applyLeaseRequest.product_type
                }else{
                    ""
                }
                val reqAmt = if (viewModel.applyLeaseRequest.request_amount != null){
                    viewModel.applyLeaseRequest.request_amount
                }else{
                    ""
                }
                var term = if (viewModel.applyLeaseRequest.repayment_term != null){
                    viewModel.applyLeaseRequest.repayment_term
                }else{
                    ""
                }
                binding.btnSubmit.isEnable(
                    proType!!,
                    binding.edNameBrand.text.toString(),
                    binding.edNameModel.text.toString(),
                    binding.edNameType.text.toString(),
                    binding.edEtcBrand.text.toString(),
                    binding.edEtcModel.text.toString(),
                    binding.edEtcType.text.toString(),
                    reqAmt!!,
                    s.toString(),
                    term!!.toString()
                )
            }

            override fun afterTextChanged(s: Editable) {
                viewModel.applyLeaseRequest.product_price = if (s.toString().isNotEmpty()){
                    "LAK ".plus(s.toString())
                }else{
                    ""
                }
            }
        })

        binding.edReqAmt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val proType = if (viewModel.applyLeaseRequest.product_type != null){
                    viewModel.applyLeaseRequest.product_type
                }else{
                    ""
                }
                val proPrice = if (viewModel.applyLeaseRequest.product_price != null){
                    viewModel.applyLeaseRequest.product_price
                }else{
                    ""
                }
                val term = if (viewModel.applyLeaseRequest.repayment_term != null){
                    viewModel.applyLeaseRequest.repayment_term
                }else{
                    ""
                }
                binding.btnSubmit.isEnable(
                    proType!!,
                    binding.edNameBrand.text.toString(),
                    binding.edNameModel.text.toString(),
                    binding.edNameType.text.toString(),
                    binding.edEtcBrand.text.toString(),
                    binding.edEtcModel.text.toString(),
                    binding.edEtcType.text.toString(),
                    s.toString(),
                    proPrice!!,
                    term!!.toString()
                )
            }

            override fun afterTextChanged(s: Editable) {
                viewModel.applyLeaseRequest.request_amount = if (s.toString().isNotEmpty()){
                    "LAK ".plus(s.toString())
                }else{
                    ""
                }
            }
        })

        binding.edNameBrand.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val proType = if (viewModel.applyLeaseRequest.product_type != null){
                    viewModel.applyLeaseRequest.product_type
                }else{
                    ""
                }
                val reqAmt = if (viewModel.applyLeaseRequest.request_amount != null){
                    viewModel.applyLeaseRequest.request_amount
                }else{
                    ""
                }
                val term = if (viewModel.applyLeaseRequest.repayment_term != null){
                    viewModel.applyLeaseRequest.repayment_term
                }else{
                    ""
                }
                val proPrice = if (viewModel.applyLeaseRequest.product_price != null){
                    viewModel.applyLeaseRequest.product_price
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
                    proPrice!!,
                    term!!.toString()
                )
            }

            override fun afterTextChanged(s: Editable) {
                viewModel.applyLeaseRequest.name_of_brand = s.toString()
            }
        })

        binding.edNameModel.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val proType = if (viewModel.applyLeaseRequest.product_type != null){
                    viewModel.applyLeaseRequest.product_type
                }else{
                    ""
                }
                val reqAmt = if (viewModel.applyLeaseRequest.request_amount != null){
                    viewModel.applyLeaseRequest.request_amount
                }else{
                    ""
                }
                val term = if (viewModel.applyLeaseRequest.repayment_term != null){
                    viewModel.applyLeaseRequest.repayment_term
                }else{
                    ""
                }
                val proPrice = if (viewModel.applyLeaseRequest.product_price != null){
                    viewModel.applyLeaseRequest.product_price
                }else{
                    ""
                }
                binding.btnSubmit.isEnable(
                    proType!!,
                    binding.edNameBrand.text.toString(),
                    s.toString(),
                    binding.edNameType.text.toString(),
                    binding.edEtcBrand.text.toString(),
                    binding.edEtcModel.text.toString(),
                    binding.edEtcType.text.toString(),
                    reqAmt!!,
                    proPrice!!,
                    term!!.toString()
                )
            }

            override fun afterTextChanged(s: Editable) {
                viewModel.applyLeaseRequest.name_of_model = s.toString()
            }
        })

        binding.edNameType.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val proType = if (viewModel.applyLeaseRequest.product_type != null){
                    viewModel.applyLeaseRequest.product_type
                }else{
                    ""
                }
                val reqAmt = if (viewModel.applyLeaseRequest.request_amount != null){
                    viewModel.applyLeaseRequest.request_amount
                }else{
                    ""
                }
                val term = if (viewModel.applyLeaseRequest.repayment_term != null){
                    viewModel.applyLeaseRequest.repayment_term
                }else{
                    ""
                }
                val proPrice = if (viewModel.applyLeaseRequest.product_price != null){
                    viewModel.applyLeaseRequest.product_price
                }else{
                    ""
                }
                binding.btnSubmit.isEnable(
                    proType!!,
                    binding.edNameBrand.text.toString(),
                    binding.edNameModel.text.toString(),
                    s.toString(),
                    binding.edEtcBrand.text.toString(),
                    binding.edEtcModel.text.toString(),
                    binding.edEtcType.text.toString(),
                    reqAmt!!,
                    proPrice!!,
                    term!!.toString()
                )
            }

            override fun afterTextChanged(s: Editable) {
                viewModel.applyLeaseRequest.name_of_type = s.toString()
            }
        })

        binding.cbEtc.setOnCheckedChangeListener { p0, p1 ->
            viewModel.applyLeaseRequest.etc_status = p1
        }

        binding.edEtcBrand.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val proType = if (viewModel.applyLeaseRequest.product_type != null){
                    viewModel.applyLeaseRequest.product_type
                }else{
                    ""
                }
                val reqAmt = if (viewModel.applyLeaseRequest.request_amount != null){
                    viewModel.applyLeaseRequest.request_amount
                }else{
                    ""
                }
                val term = if (viewModel.applyLeaseRequest.repayment_term != null){
                    viewModel.applyLeaseRequest.repayment_term
                }else{
                    ""
                }
                val proPrice = if (viewModel.applyLeaseRequest.product_price != null){
                    viewModel.applyLeaseRequest.product_price
                }else{
                    ""
                }
                binding.btnSubmit.isEnable(
                    proType!!,
                    binding.edNameBrand.text.toString(),
                    binding.edNameModel.text.toString(),
                    binding.edNameType.text.toString(),
                    s.toString(),
                    binding.edEtcModel.text.toString(),
                    binding.edEtcType.text.toString(),
                    reqAmt!!,
                    proPrice!!,
                    term!!.toString()
                )
            }

            override fun afterTextChanged(s: Editable) {
                viewModel.applyLeaseRequest.etc_name_of_brand = s.toString()
            }
        })

        binding.edEtcModel.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val proType = if (viewModel.applyLeaseRequest.product_type != null){
                    viewModel.applyLeaseRequest.product_type
                }else{
                    ""
                }
                val reqAmt = if (viewModel.applyLeaseRequest.request_amount != null){
                    viewModel.applyLeaseRequest.request_amount
                }else{
                    ""
                }
                val term = if (viewModel.applyLeaseRequest.repayment_term != null){
                    viewModel.applyLeaseRequest.repayment_term
                }else{
                    ""
                }
                val proPrice = if (viewModel.applyLeaseRequest.product_price != null){
                    viewModel.applyLeaseRequest.product_price
                }else{
                    ""
                }
                binding.btnSubmit.isEnable(
                    proType!!,
                    binding.edNameBrand.text.toString(),
                    binding.edNameModel.text.toString(),
                    binding.edNameType.text.toString(),
                    binding.edEtcBrand.text.toString(),
                    s.toString(),
                    binding.edEtcType.text.toString(),
                    reqAmt!!,
                    proPrice!!,
                    term!!.toString()
                )
            }

            override fun afterTextChanged(s: Editable) {
                viewModel.applyLeaseRequest.etc_name_of_model = s.toString()
            }
        })

        binding.edEtcType.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val proType = if (viewModel.applyLeaseRequest.product_type != null){
                    viewModel.applyLeaseRequest.product_type
                }else{
                    ""
                }
                val reqAmt = if (viewModel.applyLeaseRequest.request_amount != null){
                    viewModel.applyLeaseRequest.request_amount
                }else{
                    ""
                }
                val term = if (viewModel.applyLeaseRequest.repayment_term != null){
                    viewModel.applyLeaseRequest.repayment_term
                }else{
                    ""
                }
                val proPrice = if (viewModel.applyLeaseRequest.product_price != null){
                    viewModel.applyLeaseRequest.product_price
                }else{
                    ""
                }
                binding.btnSubmit.isEnable(
                    proType!!,
                    binding.edNameBrand.text.toString(),
                    binding.edNameModel.text.toString(),
                    binding.edNameType.text.toString(),
                    binding.edEtcBrand.text.toString(),
                    binding.edEtcModel.text.toString(),
                    s.toString(),
                    reqAmt!!,
                    proPrice!!,
                    term!!.toString()
                )
            }

            override fun afterTextChanged(s: Editable) {
                viewModel.applyLeaseRequest.etc_name_of_type = s.toString()
            }
        })

        binding.edProPrice.addTextChangedListener(NumberTextWatcherForThousand(binding.edProPrice))
        binding.edReqAmt.addTextChangedListener(NumberTextWatcherForThousand(binding.edReqAmt))

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