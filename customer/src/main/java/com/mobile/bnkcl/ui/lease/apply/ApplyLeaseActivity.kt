package com.mobile.bnkcl.ui.lease.apply

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.bnkc.library.data.type.ErrorCode
import com.bnkc.library.data.type.RunTimeDataStore
import com.bnkc.library.util.Constants
import com.bnkc.sourcemodule.base.BaseActivity
import com.bnkc.sourcemodule.dialog.ListChoiceDialog
import com.bnkc.sourcemodule.dialog.AlertDialog
import com.bnkc.sourcemodule.dialog.TwoButtonDialog
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.response.code.CodesData
import com.mobile.bnkcl.data.response.code.ProductResponseObj
import com.mobile.bnkcl.data.response.lease.ItemResponseObject
import com.mobile.bnkcl.data.response.user.ProfileData
import com.mobile.bnkcl.databinding.ActivityApplyLeaseBinding
import com.mobile.bnkcl.ui.pinview.PinCodeActivity
import com.mobile.bnkcl.ui.success.ResultActivity
import com.mobile.bnkcl.ui.user.edit.EditAccountInfoActivity
import com.mobile.bnkcl.util.FormatUtil
import com.mobile.bnkcl.util.Utils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.max

@AndroidEntryPoint
class ApplyLeaseActivity : BaseActivity<ActivityApplyLeaseBinding>() {

    private val viewModel : ApplyLeaseViewModel by viewModels()
    private var selectedItem = -1
    private var selectedTypeItem = -1
    private var selectedBrandItem = -1
    private var selectedModelItem = -1
    private var repaymentSelected = -1
    @Inject
    lateinit var listChoiceDialog: ListChoiceDialog

    private lateinit var productCodes : ArrayList<ItemResponseObject>
    private lateinit var repaymentCodes : ArrayList<ItemResponseObject>
    private var typeCodes : ArrayList<ProductResponseObj>? = ArrayList()
    private var brandCodes : List<ProductResponseObj>? = ArrayList()
    private var modelCodes : ArrayList<ProductResponseObj>? = ArrayList()

    @Inject lateinit var twoButtonDialog : TwoButtonDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        setStatusBarColor(ContextCompat.getColor(this, R.color.color_f5f7fc))
        setAnimateType(com.bnkc.sourcemodule.app.Constants.ANIMATE_LEFT)
        super.onCreate(savedInstanceState)
        binding.applyViewModel = viewModel
        Utils.setHideKeyboard(this, binding.root)
        showLoading(true)
        viewModel.getUserProfile()
        viewModel.getJobTypeCodes()
        viewModel.reqLeaseItemCode(Constants.PRODUCT_TYPE)
        viewModel.reqRepaymentCode(Constants.REPAYMENT_TERM)
        viewModel.reqBrandCode(Constants.BRAND_NAME)

        initView()
        initEvent()
        observeViewModel()
        handleError()
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

    private fun setUpUserInfo(profileData : ProfileData?){
        binding.tvUserName.text         = profileData?.name
        binding.tvIdentificationNm.text = profileData?.identificationNumber
        binding.tvDateOfBirth.text      = profileData?.dateOfBirth
        binding.tvPhoneNumber.text      = FormatUtil.getTelFormat(profileData?.phoneNumber!! ,3)
        binding.tvAddress.text          = profileData.etcDetailedAddress
    }

    private var jobTypeList: ArrayList<CodesData>? = ArrayList()
    private val REQ_CODE = 1001
    private var profileData: ProfileData? = null
    private fun observeViewModel(){
        viewModel.jobTypesLiveData.observe(this) {
            jobTypeList = it.codes
        }
        viewModel.userProfileLiveData.observe(this) {
            profileData = it
            setUpUserInfo(profileData)
        }
        viewModel.productTypeLiveData.observe(this, {
            productCodes = it.codes!!
            viewModel.setUpProductTypeData(productCodes)

        })
        viewModel.repaymentLiveData.observe(this, {
            repaymentCodes = it.codes!!
            viewModel.setUpRepaymentData(repaymentCodes)
        })

        viewModel.typeLiveData.observe(this, {
            typeCodes = (it.products as ArrayList<ProductResponseObj>?)!!
            viewModel.setUpTypeData(typeCodes)
        })
        viewModel.brandLiveData.observe(this, {
            brandCodes = it.products!!
            viewModel.setUpBrandData(brandCodes)
        })
        viewModel.modelLiveData.observe(this, {
            modelCodes = (it.products as ArrayList<ProductResponseObj>?)!!
            viewModel.setUpModelData(modelCodes)
        })

        viewModel.applyLeaseLiveData.observe(this, {
            navigateResult(it.lease_application_id != null && it.lease_application_id!!.isNotEmpty())
        })

        viewModel.actionLiveData.observe(this, {
            when (it) {
                "apply_lease" -> {

                    twoButtonDialog = TwoButtonDialog.newInstance(
                        R.drawable.ic_badge_inform,
                        getString(R.string.apply_lease),
                        getString(R.string.apply_lease_smg),
                        getString(R.string.edit_cancel),
                        getString(R.string.apply)
                    )
                    twoButtonDialog.onConfirmClickedListener {
                        showLoading(true)
                        viewModel.applyLease()
                    }
                    twoButtonDialog.show(this.supportFragmentManager, twoButtonDialog.tag)

                }
                "product_type" -> {
                    listChoiceDialog = ListChoiceDialog.newInstance(
                        R.drawable.ic_badge_general,
                        getString(R.string.lease_product_type),
                        viewModel.productTypes!!,
                        selectedItem
                    )

                    listChoiceDialog.setOnItemListener = { pos: Int ->
                        selectedItem = pos
                        binding.tvProType.text = productCodes[pos].title
                        viewModel.applyLeaseRequest.product_type = productCodes[pos].code

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
                        if(viewModel.applyLeaseRequest.etc_status!!){
                            binding.btnSubmit.isEnable(
                                productCodes[pos].code!!,
                                binding.edEtcBrand.text.toString(),
                                binding.edEtcModel.text.toString(),
                                binding.edEtcType.text.toString(),
                                reqAmt!!,
                                proPrice!!,
                                term!!.toString()
                            )
                        }else {
                            binding.btnSubmit.isEnable(
                                productCodes[pos].code!!,
                                binding.tvNameBrand.tag.toString(),
                                binding.tvNameType.tag.toString(),
                                binding.tvNameModel.tag.toString(),
                                reqAmt!!,
                                proPrice!!,
                                term!!.toString()
                            )
                        }
                    }
                    listChoiceDialog.isCancelable = true
                    listChoiceDialog.show(supportFragmentManager, listChoiceDialog.tag)
                }
                "brand" -> {
                    if (brandCodes != null && brandCodes!!.isNotEmpty()) {
                        listChoiceDialog = ListChoiceDialog.newInstance(
                            R.drawable.ic_badge_general,
                            getString(R.string.name_of_brand),
                            viewModel.brandData!!,
                            selectedBrandItem
                        )

                        listChoiceDialog.setOnItemListener = { pos: Int ->
                            selectedBrandItem = pos
                            binding.tvNameBrand.text = brandCodes?.get(pos)!!.name
                            binding.tvNameBrand.tag = brandCodes?.get(pos)!!.erpCode
                            viewModel.applyLeaseRequest.name_of_brand = brandCodes!![pos].erpCode

                            viewModel.reqTypeCode(
                                Constants.TYPE_NAME,
                                brandCodes!![pos].id.toString()
                            )
                            removeToDefault(2)
                            val proType = if (viewModel.applyLeaseRequest.product_type != null) {
                                viewModel.applyLeaseRequest.product_type
                            } else {
                                ""
                            }
                            val proPrice = if (viewModel.applyLeaseRequest.product_price != null) {
                                viewModel.applyLeaseRequest.product_price
                            } else {
                                ""
                            }
                            val reqAmt = if (viewModel.applyLeaseRequest.request_amount != null) {
                                viewModel.applyLeaseRequest.request_amount
                            } else {
                                ""
                            }
                            val term = if (viewModel.applyLeaseRequest.repayment_term != null) {
                                viewModel.applyLeaseRequest.repayment_term
                            } else {
                                ""
                            }

                            binding.btnSubmit.isEnable(
                                proType!!,
                                binding.tvNameBrand.tag.toString(),
                                binding.tvNameType.tag.toString(),
                                binding.tvNameModel.tag.toString(),
                                reqAmt!!,
                                proPrice!!,
                                term!!.toString()
                            )

                        }
                        listChoiceDialog.isCancelable = true
                        listChoiceDialog.show(supportFragmentManager, listChoiceDialog.tag)
                    }
                }
                "type" ->{
                    if (typeCodes != null && typeCodes!!.isNotEmpty()) {
                        listChoiceDialog = ListChoiceDialog.newInstance(
                            R.drawable.ic_badge_general,
                            getString(R.string.name_of_type),
                            viewModel.typeData!!,
                            selectedTypeItem
                        )

                        listChoiceDialog.setOnItemListener = { pos: Int ->
                            selectedTypeItem = pos
                            binding.tvNameType.text = typeCodes?.get(pos)!!.name
                            binding.tvNameType.tag = typeCodes?.get(pos)!!.erpCode
                            viewModel.applyLeaseRequest.name_of_type = typeCodes!![pos].erpCode

                            viewModel.reqModelCode(
                                Constants.MODEL_NAME,
                                typeCodes!![pos].id.toString()
                            )
                            removeToDefault(1)
                            val proType = if (viewModel.applyLeaseRequest.product_type != null) {
                                viewModel.applyLeaseRequest.product_type
                            } else {
                                ""
                            }
                            val proPrice = if (viewModel.applyLeaseRequest.product_price != null) {
                                viewModel.applyLeaseRequest.product_price
                            } else {
                                ""
                            }
                            val reqAmt = if (viewModel.applyLeaseRequest.request_amount != null) {
                                viewModel.applyLeaseRequest.request_amount
                            } else {
                                ""
                            }
                            val term = if (viewModel.applyLeaseRequest.repayment_term != null) {
                                viewModel.applyLeaseRequest.repayment_term
                            } else {
                                ""
                            }
                            binding.btnSubmit.isEnable(
                                proType!!,
                                binding.tvNameBrand.tag.toString(),
                                binding.tvNameType.tag.toString(),
                                binding.tvNameModel.tag.toString(),
                                reqAmt!!,
                                proPrice!!,
                                term!!.toString()
                            )

                        }
                        listChoiceDialog.isCancelable = true
                        listChoiceDialog.show(supportFragmentManager, listChoiceDialog.tag)
                    }
                }
                "model" -> {
                    if (modelCodes != null && modelCodes!!.isNotEmpty()) {
                        listChoiceDialog = ListChoiceDialog.newInstance(
                            R.drawable.ic_badge_general,
                            getString(R.string.name_of_model),
                            viewModel.modelData!!,
                            selectedModelItem
                        )

                        listChoiceDialog.setOnItemListener = { pos: Int ->
                            selectedModelItem = pos

                            binding.tvNameModel.text = modelCodes?.get(pos)!!.name
                            binding.tvNameModel.tag = modelCodes!![pos].erpCode
                            viewModel.applyLeaseRequest.name_of_model = modelCodes!![pos].erpCode

                            val proType = if (viewModel.applyLeaseRequest.product_type != null) {
                                viewModel.applyLeaseRequest.product_type
                            } else {
                                ""
                            }
                            val proPrice = if (viewModel.applyLeaseRequest.product_price != null) {
                                viewModel.applyLeaseRequest.product_price
                            } else {
                                ""
                            }
                            val reqAmt = if (viewModel.applyLeaseRequest.request_amount != null) {
                                viewModel.applyLeaseRequest.request_amount
                            } else {
                                ""
                            }
                            val term = if (viewModel.applyLeaseRequest.repayment_term != null) {
                                viewModel.applyLeaseRequest.repayment_term
                            } else {
                                ""
                            }
                            binding.btnSubmit.isEnable(
                                proType!!,
                                binding.tvNameBrand.tag.toString(),
                                binding.tvNameType.tag.toString(),
                                binding.tvNameModel.tag.toString(),
                                reqAmt!!,
                                proPrice!!,
                                term!!.toString()
                            )
                        }
                        listChoiceDialog.isCancelable = true
                        listChoiceDialog.show(supportFragmentManager, listChoiceDialog.tag)
                    }
                }

                "repayment_term" -> {
                    listChoiceDialog = ListChoiceDialog.newInstance(
                        R.drawable.ic_badge_general,
                        getString(R.string.repayment_term),
                        viewModel.repaymentData!!,
                        repaymentSelected
                    )

                    listChoiceDialog.setOnItemListener = { pos: Int ->
                        repaymentSelected = pos
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

                        if(viewModel.applyLeaseRequest.etc_status!!){
                            binding.btnSubmit.isEnable(
                                proType!!,
                                binding.edEtcBrand.text.toString(),
                                binding.edEtcModel.text.toString(),
                                binding.edEtcType.text.toString(),
                                reqAmt!!,
                                proPrice!!,
                                repaymentCodes[pos].code.toString()
                            )
                        }else {
                            binding.btnSubmit.isEnable(
                                proType!!,
                                binding.tvNameBrand.tag.toString(),
                                binding.tvNameType.tag.toString(),
                                binding.tvNameModel.tag.toString(),
                                reqAmt!!,
                                proPrice!!,
                                repaymentCodes[pos].code.toString()
                            )
                        }

                    }
                    listChoiceDialog.isCancelable = true
                    listChoiceDialog.show(supportFragmentManager, listChoiceDialog.tag)
                }
                "edit_info" -> {
                    val intent = Intent(this, EditAccountInfoActivity::class.java)
                    intent.putExtra("ACCOUNT_INFO", profileData)
                    intent.putExtra("JOB_TYPE", jobTypeList)
                    launchSomeActivity.launch(intent)
                }
            }
        })
    }

    private fun removeToDefault(n: Int) {
        when (n) {
            1 -> {
                //set default text before select item
                viewModel.applyLeaseRequest.name_of_model = ""
                //reset selected index in list
                selectedModelItem = -1
                //Previous list
                modelCodes?.clear()
                binding.tvNameModel.isEnabled = true
                binding.tvNameModel.tag = ""
                binding.tvNameModel.text = ""
            }
            2 -> {
                //set default text before select item
                viewModel.applyLeaseRequest.name_of_type = ""
                viewModel.applyLeaseRequest.name_of_model = ""
                binding.tvNameType.tag = ""
                binding.tvNameModel.tag = ""
                binding.tvNameType.text = ""
                binding.tvNameModel.text = ""
                //reset selected index in list
                typeCodes?.clear()
                modelCodes?.clear()
                binding.tvNameType.isEnabled = true
                binding.tvNameModel.isEnabled = false
//                communeId = 0
                //reset selected index in list
                selectedBrandItem = -1
                selectedModelItem = -1
            }
        }
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
                val term = if (viewModel.applyLeaseRequest.repayment_term != null){
                    viewModel.applyLeaseRequest.repayment_term
                }else{
                    ""
                }
                if(viewModel.applyLeaseRequest.etc_status!!){
                    binding.btnSubmit.isEnable(
                        proType!!,
                        binding.edEtcBrand.text.toString(),
                        binding.edEtcModel.text.toString(),
                        binding.edEtcType.text.toString(),
                        reqAmt!!,
                        s.toString(),
                        term!!.toString()
                    )
                }else {
                    binding.btnSubmit.isEnable(
                        proType!!,
                        binding.tvNameBrand.tag.toString(),
                        binding.tvNameType.tag.toString(),
                        binding.tvNameModel.tag.toString(),
                        reqAmt!!,
                        s.toString(),
                        term!!.toString()
                    )
                }

            }

            override fun afterTextChanged(s: Editable) {
                viewModel.applyLeaseRequest.product_price = if (s.toString().isNotEmpty()){
                    "LAK ".plus(s.toString().replace(",", ""))
                }else{
                    ""
                }
                if (binding.edReqAmt.text.toString().isNotEmpty()) {
                    val advancePayment =
                        viewModel.applyLeaseRequest.product_price!!.split(" ")[1].toInt() - viewModel.applyLeaseRequest.request_amount!!.split(" ")[1].toInt()
                    binding.tvAdvancePayment.text =
                        advancePayment.toString()
                            .plus(" ${getString(R.string.kip)}")
                }
            }
        })

        binding.edReqAmt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                viewModel.applyLeaseRequest.request_amount = s.toString().replace(",", "")
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
                if(viewModel.applyLeaseRequest.etc_status!!){
                    binding.btnSubmit.isEnable(
                        proType!!,
                        binding.edEtcBrand.text.toString(),
                        binding.edEtcModel.text.toString(),
                        binding.edEtcType.text.toString(),
                        s.toString(),
                        proPrice!!,
                        term!!.toString()
                    )
                }else {
                    binding.btnSubmit.isEnable(
                        proType!!,
                        binding.tvNameBrand.tag.toString(),
                        binding.tvNameType.tag.toString(),
                        binding.tvNameModel.tag.toString(),
                        s.toString(),
                        proPrice!!,
                        term!!.toString()
                    )
                }

            }

            override fun afterTextChanged(s: Editable) {
                viewModel.applyLeaseRequest.request_amount = if (s.toString().isNotEmpty()){
                    "LAK ".plus(s.toString().replace(",", ""))
                }else{
                    ""
                }
                if (binding.edProPrice.text.toString().isNotEmpty()) {
                    val advancePayment =
                        viewModel.applyLeaseRequest.product_price!!.split(" ")[1].toInt() - viewModel.applyLeaseRequest.request_amount!!.split(" ")[1].toInt()
                    binding.tvAdvancePayment.text =
                        advancePayment.toString()
                            .plus(" ${getString(R.string.kip)}")
                }
            }
        })
        binding.cbEtc.setOnCheckedChangeListener { _, p1 ->
            enableEctInputFields(p1)
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

    private fun enableEctInputFields(enableEctInput : Boolean){
        viewModel.applyLeaseRequest.etc_status = enableEctInput
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
        if (enableEctInput){
            binding.edEtcBrand.isEnabled = true
            binding.edEtcType.isEnabled = true
            binding.edEtcModel.isEnabled = true
            viewModel.applyLeaseRequest.etc_name_of_brand = binding.edEtcBrand.text.toString()
            viewModel.applyLeaseRequest.etc_name_of_type = binding.edEtcType.text.toString()
            viewModel.applyLeaseRequest.etc_name_of_model = binding.edEtcModel.text.toString()

            binding.tvNameBrand.isEnabled = false
            binding.tvNameType.isEnabled = false
            binding.tvNameModel.isEnabled = false
            viewModel.applyLeaseRequest.name_of_brand = ""
            viewModel.applyLeaseRequest.name_of_type = ""
            viewModel.applyLeaseRequest.name_of_model = ""

            binding.btnSubmit.isEnable(
                proType!!,
                binding.edEtcBrand.text.toString(),
                binding.edEtcModel.text.toString(),
                binding.edEtcType.text.toString(),
                reqAmt!!,
                proPrice!!,
                term!!.toString()
            )
        }else{
            binding.tvNameBrand.isEnabled = true
            binding.tvNameType.isEnabled = binding.tvNameBrand.isEnabled
            binding.tvNameModel.isEnabled =  binding.tvNameType.isEnabled && binding.tvNameType.tag.toString().isNotEmpty()

            viewModel.applyLeaseRequest.name_of_brand = binding.tvNameBrand.tag.toString()
            viewModel.applyLeaseRequest.name_of_type = binding.tvNameType.tag.toString()
            viewModel.applyLeaseRequest.name_of_model = binding.tvNameModel.tag.toString()

            binding.edEtcBrand.isEnabled = false
            binding.edEtcType.isEnabled = false
            binding.edEtcModel.isEnabled = false
            viewModel.applyLeaseRequest.etc_name_of_brand = ""
            viewModel.applyLeaseRequest.etc_name_of_type = ""
            viewModel.applyLeaseRequest.etc_name_of_model = ""
            binding.btnSubmit.isEnable(
                proType!!,
                viewModel.applyLeaseRequest.name_of_brand!!,
                viewModel.applyLeaseRequest.name_of_type!!,
                viewModel.applyLeaseRequest.name_of_model!!,
                reqAmt!!,
                proPrice!!,
                term!!.toString()
            )
        }
    }

    private var isUpdateProfile: Boolean = false
    var launchSomeActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == REQ_CODE) {
            val data: Intent? = result.data
            if (data != null) {
                isUpdateProfile = data.getBooleanExtra("IS_UPDATE_PROFILE", false)
                if (isUpdateProfile) {
                    showLoading(true)
                    viewModel.getUserProfile()
                }
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_apply_lease
    }

    class NumberTextWatcherForThousand internal constructor(private var editText: EditText) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) {
            val value = s.toString()
            try {
                editText.removeTextChangedListener(this)
                val fixedValue: String = FormatUtil.getDecimalFormattedString(value, false).toString()
                val preSelection = editText.selectionEnd
                s.replace(0, value.length, fixedValue)
                preSelection + fixedValue.length - value.length
                editText.setSelection(max(s.length, 0))
                editText.addTextChangedListener(this)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * catch error
     */
    private fun handleError() {
        viewModel.handleError.observe(this) {
            val error = getErrorMessage(it)
            alertDialog = AlertDialog.newInstance(error.icon!!, error.code!!, error.message!!, error.button!!)
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