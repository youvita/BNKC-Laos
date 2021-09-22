package com.mobile.bnkcl.ui.lease.apply

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.EditText
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.bnkc.library.data.type.RunTimeDataStore
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.library.util.Constants
import com.bnkc.sourcemodule.base.BaseActivity
import com.bnkc.sourcemodule.dialog.ListChoiceDialog
import com.bnkc.sourcemodule.dialog.SystemDialog
import com.bnkc.sourcemodule.dialog.TwoButtonDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.response.code.CodesData
import com.mobile.bnkcl.data.response.lease.ItemResponseObject
import com.mobile.bnkcl.data.response.user.ProfileData
import com.mobile.bnkcl.databinding.ActivityApplyLeaseBinding
import com.mobile.bnkcl.ui.pinview.PinCodeActivity
import com.mobile.bnkcl.ui.success.ResultActivity
import com.mobile.bnkcl.ui.user.edit.EditAccountInfoActivity
import com.mobile.bnkcl.utilities.FormatUtil
import com.mobile.bnkcl.utilities.UtilsGlide
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.Disposable
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

    @Inject
    lateinit var systemDialog: SystemDialog
    private lateinit var productCodes : ArrayList<ItemResponseObject>
    private lateinit var repaymentCodes : ArrayList<ItemResponseObject>
    private lateinit var typeCodes : ArrayList<ItemResponseObject>
    private lateinit var brandCodes : ArrayList<ItemResponseObject>
    private lateinit var modelCodes : ArrayList<ItemResponseObject>

    @Inject lateinit var twoButtonDialog : TwoButtonDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        setStatusBarColor(ContextCompat.getColor(this, R.color.color_f5f7fc))
        setAnimateType(com.bnkc.sourcemodule.app.Constants.ANIMATE_LEFT)
        super.onCreate(savedInstanceState)
        binding.applyViewModel = viewModel

        showLoading()
        viewModel.getUserProfile()

        viewModel.reqLeaseItemCode(Constants.PRODUCT_TYPE)
        viewModel.reqRepaymentCode(Constants.REPAYMENT_TERM)
        viewModel.reqBrandCode(Constants.BRAND_NAME)
        viewModel.reqTypeCode(Constants.TYPE_NAME)
        viewModel.reqModelCode(Constants.MODEL_NAME)

        initView()
        initEvent()
        observeViewModel()
    }

    override fun handleSessionExpired(icon: Int, title: String, message: String, button: String) {
        super.handleSessionExpired(icon, title, message, button)
        systemDialog = SystemDialog.newInstance(icon, title, message, button)
        systemDialog.show(supportFragmentManager, systemDialog.tag)
        systemDialog.onConfirmClicked {
            RunTimeDataStore.LoginToken.value = ""
            startActivity(Intent(this, PinCodeActivity::class.java))
        }
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
//            binding.tvTitleToolbar02.setOnClickListener(this)

//            for (i in 0 until jobTypeList!!.size) {
//                if (profileData!!.jobType.equals(it.codes!![i].code)) {
//                    if (profileData!!.jobType.isNullOrEmpty()) {
//                        binding.tvOccupation.text = resources.getString(R.string.not_available)
//                    } else {
//                        binding.tvOccupation.text = it.codes[i].title
//                    }
//                }
//            }
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
            typeCodes = it.codes!!
            viewModel.setUpTypeData(typeCodes)
        })
        viewModel.brandLiveData.observe(this, {
            brandCodes = it.codes!!
            viewModel.setUpBrandData(brandCodes)
        })
        viewModel.modelLiveData.observe(this, {
            modelCodes = it.codes!!
            viewModel.setUpModelData(modelCodes)
        })

        viewModel.applyLeaseLiveData.observe(this, {
            navigateResult(it.lease_application_id != null && it.lease_application_id!!.isNotEmpty())
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
                        viewModel.productTypes!!,
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
                                binding.tvNameBrand.text.toString(),
                                binding.tvNameModel.text.toString(),
                                binding.tvNameType.text.toString(),
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
                    listChoiceDialog = ListChoiceDialog.newInstance(
                        R.drawable.ic_badge_error,
                        getString(R.string.lease_product_type),
                        viewModel.brandData!!,
                        selectedBrandItem
                    )

                    listChoiceDialog.setOnItemListener = { pos: Int ->
                        selectedBrandItem = pos
                        binding.tvNameBrand.setTextColor(
                            ContextCompat.getColor(
                                this,
                                R.color.color_263238
                            )
                        )
                        binding.tvNameBrand.text = brandCodes[pos].title
                        binding.tvNameBrand.tag = brandCodes[pos].title
                        viewModel.applyLeaseRequest.name_of_brand = brandCodes[pos].code

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
                        val term = if (viewModel.applyLeaseRequest.repayment_term != null){
                            viewModel.applyLeaseRequest.repayment_term
                        }else{
                            ""
                        }
//                        if(viewModel.applyLeaseRequest.etc_status!!){
//                            binding.btnSubmit.isEnable(
//                                itemResponses[pos].code!!,
//                                binding.edEtcBrand.text.toString(),
//                                binding.edEtcModel.text.toString(),
//                                binding.edEtcType.text.toString(),
//                                reqAmt!!,
//                                proPrice!!,
//                                term!!.toString()
//                            )
//                        }else {
//
//                        }
                        binding.btnSubmit.isEnable(
                            proType!!,
                            brandCodes[pos].code!!,
                            binding.tvNameModel.tag.toString(),
                            binding.tvNameType.tag.toString(),
                            reqAmt!!,
                            proPrice!!,
                            term!!.toString()
                        )
                    }
                    listChoiceDialog.isCancelable = true
                    listChoiceDialog.show(supportFragmentManager, listChoiceDialog.tag)
                }
                "type" ->{
                    listChoiceDialog = ListChoiceDialog.newInstance(
                        R.drawable.ic_badge_error,
                        getString(R.string.lease_product_type),
                        viewModel.typeData!!,
                        selectedTypeItem
                    )

                    listChoiceDialog.setOnItemListener = { pos: Int ->
                        selectedTypeItem = pos
                        binding.tvNameType.setTextColor(
                            ContextCompat.getColor(
                                this,
                                R.color.color_263238
                            )
                        )
                        binding.tvNameType.text = typeCodes[pos].title
                        binding.tvNameType.tag = typeCodes[pos].title
                        viewModel.applyLeaseRequest.name_of_type = typeCodes[pos].code
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
                        val term = if (viewModel.applyLeaseRequest.repayment_term != null){
                            viewModel.applyLeaseRequest.repayment_term
                        }else{
                            ""
                        }
                        binding.btnSubmit.isEnable(
                            proType!!,
                            binding.tvNameBrand.tag.toString(),
                            binding.tvNameModel.tag.toString(),
                            typeCodes[pos].code!!,
                            reqAmt!!,
                            proPrice!!,
                            term!!.toString()
                        )
                    }
                    listChoiceDialog.isCancelable = true
                    listChoiceDialog.show(supportFragmentManager, listChoiceDialog.tag)
                }
                "model" -> {
                    listChoiceDialog = ListChoiceDialog.newInstance(
                        R.drawable.ic_badge_error,
                        getString(R.string.lease_product_type),
                        viewModel.modelData!!,
                        selectedModelItem
                    )

                    listChoiceDialog.setOnItemListener = { pos: Int ->
                        selectedModelItem = pos
                        binding.tvNameModel.setTextColor(
                            ContextCompat.getColor(
                                this,
                                R.color.color_263238
                            )
                        )
                        binding.tvNameModel.text = modelCodes[pos].title
                        binding.tvNameModel.tag = modelCodes[pos].title
                        viewModel.applyLeaseRequest.name_of_model = modelCodes[pos].code
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
                        val term = if (viewModel.applyLeaseRequest.repayment_term != null){
                            viewModel.applyLeaseRequest.repayment_term
                        }else{
                            ""
                        }
                        binding.btnSubmit.isEnable(
                            proType!!,
                            binding.tvNameBrand.tag.toString(),
                            modelCodes[pos].code!!,
                            binding.tvNameType.tag.toString(),
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
                                binding.tvNameModel.tag.toString(),
                                binding.tvNameType.tag.toString(),
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
                    startActivityForResult(intent, REQ_CODE)
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
                        binding.tvNameModel.tag.toString(),
                        binding.tvNameType.tag.toString(),
                        reqAmt!!,
                        s.toString(),
                        term!!.toString()
                    )
                }

            }

            override fun afterTextChanged(s: Editable) {
                viewModel.applyLeaseRequest.product_price = if (s.toString().isNotEmpty()){
                    "LAK ".plus(s.toString())
                }else{
                    ""
                }
                if (binding.edReqAmt.text.toString().isNotEmpty()) {
                    binding.tvAdvancePayment.text =
                        (binding.edReqAmt.text.toString().toInt() - s.toString().toInt()).toString()
                            .plus(" Kip")
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
                        binding.tvNameModel.tag.toString(),
                        binding.tvNameType.tag.toString(),
                        s.toString(),
                        proPrice!!,
                        term!!.toString()
                    )
                }

            }

            override fun afterTextChanged(s: Editable) {
                viewModel.applyLeaseRequest.request_amount = if (s.toString().isNotEmpty()){
                    "LAK ".plus(s.toString())
                }else{
                    ""
                }
                if (binding.edProPrice.text.toString().isNotEmpty()) {
                    binding.tvAdvancePayment.text =
                        (s.toString().toInt() - binding.edProPrice.text.toString().toInt()).toString()
                            .plus(" Kip")
                }
            }
        })

//        binding.edNameBrand.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                val proType = if (viewModel.applyLeaseRequest.product_type != null){
//                    viewModel.applyLeaseRequest.product_type
//                }else{
//                    ""
//                }
//                val reqAmt = if (viewModel.applyLeaseRequest.request_amount != null){
//                    viewModel.applyLeaseRequest.request_amount
//                }else{
//                    ""
//                }
//                val term = if (viewModel.applyLeaseRequest.repayment_term != null){
//                    viewModel.applyLeaseRequest.repayment_term
//                }else{
//                    ""
//                }
//                val proPrice = if (viewModel.applyLeaseRequest.product_price != null){
//                    viewModel.applyLeaseRequest.product_price
//                }else{
//                    ""
//                }
//                binding.btnSubmit.isEnable(
//                    proType!!,
//                    s.toString(),
//                    binding.edNameModel.text.toString(),
//                    binding.edNameType.text.toString(),
//                    reqAmt!!,
//                    proPrice!!,
//                    term!!.toString()
//                )
//
//            }
//
//            override fun afterTextChanged(s: Editable) {
//                viewModel.applyLeaseRequest.name_of_brand = s.toString()
//            }
//        })
//
//        binding.edNameModel.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                val proType = if (viewModel.applyLeaseRequest.product_type != null){
//                    viewModel.applyLeaseRequest.product_type
//                }else{
//                    ""
//                }
//                val reqAmt = if (viewModel.applyLeaseRequest.request_amount != null){
//                    viewModel.applyLeaseRequest.request_amount
//                }else{
//                    ""
//                }
//                val term = if (viewModel.applyLeaseRequest.repayment_term != null){
//                    viewModel.applyLeaseRequest.repayment_term
//                }else{
//                    ""
//                }
//                val proPrice = if (viewModel.applyLeaseRequest.product_price != null){
//                    viewModel.applyLeaseRequest.product_price
//                }else{
//                    ""
//                }
//                binding.btnSubmit.isEnable(
//                    proType!!,
//                    binding.edNameBrand.text.toString(),
//                    s.toString(),
//                    binding.edNameType.text.toString(),
//                    reqAmt!!,
//                    proPrice!!,
//                    term!!.toString()
//                )
//
//            }
//
//            override fun afterTextChanged(s: Editable) {
//                viewModel.applyLeaseRequest.name_of_model = s.toString()
//            }
//        })
//
//        binding.edNameType.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                val proType = if (viewModel.applyLeaseRequest.product_type != null){
//                    viewModel.applyLeaseRequest.product_type
//                }else{
//                    ""
//                }
//                val reqAmt = if (viewModel.applyLeaseRequest.request_amount != null){
//                    viewModel.applyLeaseRequest.request_amount
//                }else{
//                    ""
//                }
//                val term = if (viewModel.applyLeaseRequest.repayment_term != null){
//                    viewModel.applyLeaseRequest.repayment_term
//                }else{
//                    ""
//                }
//                val proPrice = if (viewModel.applyLeaseRequest.product_price != null){
//                    viewModel.applyLeaseRequest.product_price
//                }else{
//                    ""
//                }
//                binding.btnSubmit.isEnable(
//                    proType!!,
//                    binding.edNameBrand.text.toString(),
//                    binding.edNameModel.text.toString(),
//                    s.toString(),
//                    reqAmt!!,
//                    proPrice!!,
//                    term!!.toString()
//                )
//            }
//
//            override fun afterTextChanged(s: Editable) {
//                viewModel.applyLeaseRequest.name_of_type = s.toString()
//            }
//        })

        binding.cbEtc.setOnCheckedChangeListener { _, p1 ->
            viewModel.applyLeaseRequest.etc_status = p1
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
            binding.edEtcType.isEnabled = true
            binding.edEtcModel.isEnabled = true
            binding.edEtcBrand.isEnabled = true
            viewModel.applyLeaseRequest.etc_name_of_type = binding.edEtcType.text.toString()
            viewModel.applyLeaseRequest.etc_name_of_brand = binding.edEtcBrand.text.toString()
            viewModel.applyLeaseRequest.etc_name_of_model = binding.edEtcModel.text.toString()

            binding.tvNameType.isEnabled = false
            binding.tvNameModel.isEnabled = false
            binding.tvNameBrand.isEnabled = false
            viewModel.applyLeaseRequest.name_of_type = ""
            viewModel.applyLeaseRequest.name_of_brand = ""
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
            binding.tvNameType.isEnabled = true
            binding.tvNameModel.isEnabled = true
            binding.tvNameBrand.isEnabled = true
            viewModel.applyLeaseRequest.name_of_type = binding.tvNameType.tag.toString()
            viewModel.applyLeaseRequest.name_of_brand = binding.tvNameBrand.tag.toString()
            viewModel.applyLeaseRequest.name_of_model = binding.tvNameModel.tag.toString()

            binding.edEtcType.isEnabled = false
            binding.edEtcModel.isEnabled = false
            binding.edEtcBrand.isEnabled = false
            viewModel.applyLeaseRequest.etc_name_of_type = ""
            viewModel.applyLeaseRequest.etc_name_of_brand = ""
            viewModel.applyLeaseRequest.etc_name_of_model = ""

            binding.btnSubmit.isEnable(
                proType!!,
                viewModel.applyLeaseRequest.name_of_brand!!,
                viewModel.applyLeaseRequest.name_of_model!!,
                viewModel.applyLeaseRequest.name_of_type!!,
                reqAmt!!,
                proPrice!!,
                term!!.toString()
            )
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_apply_lease
    }

    private var isUpdateProfile: Boolean = false
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_CANCELED) return
        if (requestCode == REQ_CODE) {
            if (data != null) {
                isUpdateProfile = data.getBooleanExtra("IS_UPDATE_PROFILE", false)
                if (isUpdateProfile) {
                    showLoading()
                    viewModel.getUserProfile()
                }
            }
        }
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
}