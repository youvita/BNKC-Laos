package com.mobile.bnkcl.ui.user.edit

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.base.BaseActivity
import com.bnkc.sourcemodule.dialog.ListChoiceDialog
import com.mobile.bnkcl.R
import com.mobile.bnkcl.com.view.BnkEditText
import com.mobile.bnkcl.data.response.code.CodesData
import com.mobile.bnkcl.data.response.user.EditAccountInfoData
import com.mobile.bnkcl.data.response.user.MyLeaseData
import com.mobile.bnkcl.data.response.user.ProfileData
import com.mobile.bnkcl.databinding.ActivityEditAccountInfoBinding
import com.mobile.bnkcl.ui.dialog.AlertEditInfoDialog
import com.mobile.bnkcl.ui.pinview.PinCodeActivity
import com.mobile.bnkcl.utilities.Utils
import com.mobile.bnkcl.utilities.UtilsSize
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class EditAccountInfoActivity : BaseActivity<ActivityEditAccountInfoBinding>(),
    View.OnClickListener {

    @Inject
    lateinit var listChoiceDialog: ListChoiceDialog

    private var isUpdate: Boolean? = false
    private var firstIndex: Int? = 0
    private var selectedIndex: Int? = 0
    private var profileData: ProfileData? = ProfileData()
    private var jobTypeTitleList: ArrayList<String>? = ArrayList()
    private var jobTypeCodeList: ArrayList<String>? = ArrayList()
    private val viewModel: EditAccountInfoViewModel by viewModels()
    private var jobTypeList: ArrayList<CodesData>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initActionBar()
        initDisposable()
        initDisablePersonalInfo()
        initLiveData()

    }

    private fun addLeaseInfo(contractNo: List<MyLeaseData>) {

        for (element in contractNo) {
            val layout = binding.llLeaseProductNumber
            layout.setBackgroundResource(R.drawable.round_solid_ffffff_8)
            val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            params.setMargins(0, UtilsSize.getValueInDP(this, 10), 0, 0)
            val res = resources.getIdentifier("round_solid_f3f6f7_8", "drawable", this.packageName)
            val editText = BnkEditText(this)
            editText.layoutParams = params
            editText.textSize = 15F
            editText.isEnabled = false
            editText.setBackgroundResource(res)
            editText.hint = element.contractNo
            editText.typeface = ResourcesCompat.getFont(this, R.font.rubik_medium)
            editText.height = UtilsSize.getValueInDP(this, 50)
            editText.setHintTextColor(ContextCompat.getColor(this, R.color.color_c4d0d6))
            editText.setPadding(UtilsSize.getValueInDP(this, 15), 0, 0, 0)
            layout.addView(editText)
        }

    }

    private fun initLiveData() {

        viewModel.editAccountInfoLiveData.observe(this) {
            val inflater = layoutInflater
            val view: View = inflater.inflate(
                R.layout.custom_toast_layout,
                findViewById(R.id.relativeLayout1)
            )

            val toast = Toast(this@EditAccountInfoActivity)
            toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 200)
            toast.duration = Toast.LENGTH_LONG
            toast.view = view
            toast.show()

            isUpdate = false

            binding.btnSave.setActive(false)
            binding.btnSave.setOnClickListener(null)
        }
    }

    private fun initView() {
        profileData = ProfileData()
        if (intent != null) {
            profileData = intent.getSerializableExtra("ACCOUNT_INFO") as ProfileData?
            binding.profile = profileData

            jobTypeList = intent.getSerializableExtra("JOB_TYPE") as ArrayList<CodesData>?

            for (i in 0 until jobTypeList!!.size) {
                jobTypeTitleList!!.add(jobTypeList!![i].title!!)
                jobTypeCodeList!!.add(jobTypeList!![i].code!!)
            }

            for (i in 0 until jobTypeCodeList!!.size) {
                if (profileData!!.jobType.equals(jobTypeCodeList!![i])) {
                    binding.lytAddressInfo.tvJobType.text = jobTypeTitleList!![i]
                    firstIndex = i
                    selectedIndex = i
                }
            }

            binding.lytAddressInfo.edtBankName.setText(profileData!!.bankName.toString())
            binding.lytAddressInfo.edtAccountNumber.setText(profileData!!.accountNumber.toString())

            addLeaseInfo(profileData!!.myLease!!)
        }

        Utils.setHideKeyboard(this, binding.parentLayout)

        binding.btnCancel.setOnClickListener(this)
        binding.lytAddressInfo.llJobType.setOnClickListener(this)

        binding.btnSave.text = resources.getString(R.string.edit_save)
        binding.btnCancel.text = resources.getString(R.string.edit_cancel)
        binding.lytAddressInfo.edtBankName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.btnSave.isEnable(
                    binding.lytAddressInfo.edtBankName.text.toString(),
                    binding.lytAddressInfo.edtAccountNumber.text.toString()
                )
                if (binding.lytAddressInfo.edtBankName.text.toString() == profileData!!.bankName) {
                    binding.btnSave.setActive(false)
                }

                if (binding.btnSave.isActive()) {
                    binding.btnSave.setOnClickListener(this@EditAccountInfoActivity)
                } else {
                    binding.btnSave.setOnClickListener(null)
                }

                isUpdate = count != 0
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        binding.lytAddressInfo.edtAccountNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                binding.btnSave.isEnable(
                    binding.lytAddressInfo.edtBankName.text.toString(),
                    binding.lytAddressInfo.edtAccountNumber.text.toString()
                )
                if (binding.lytAddressInfo.edtAccountNumber.text.toString() == profileData!!.accountNumber) {
                    binding.btnSave.setActive(false)
                }

                if (binding.btnSave.isActive()) {
                    binding.btnSave.setOnClickListener(this@EditAccountInfoActivity)
                } else {
                    binding.btnSave.setOnClickListener(null)
                }

                isUpdate = count != 0
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
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

    private fun initDisablePersonalInfo() {
        val res = resources.getIdentifier("round_solid_f3f6f7_8", "drawable", this.packageName)
        binding.edtName.setBackgroundResource(res)
        binding.llDob.setBackgroundResource(res)
        binding.llIdNumber.setBackgroundResource(res)
        binding.llGender.setBackgroundResource(res)
        binding.lytAddressInfo.llCapital.setBackgroundResource(res)
        binding.lytAddressInfo.llDistrict.setBackgroundResource(res)
        binding.lytAddressInfo.llVillage.setBackgroundResource(res)
        binding.lytAddressInfo.edtDetailedAddress.setBackgroundResource(res)
        binding.lytAddressInfo.edtEtc.setBackgroundResource(res)

        binding.edtName.isEnabled = false
        binding.edtDob.isEnabled = false
        binding.llIdNumber.isEnabled = false
        binding.llGender.isEnabled = false
        binding.lytAddressInfo.llCapital.isEnabled = false
        binding.lytAddressInfo.llDistrict.isEnabled = false
        binding.lytAddressInfo.llVillage.isEnabled = false
        binding.lytAddressInfo.edtDetailedAddress.isEnabled = false
        binding.lytAddressInfo.edtEtc.isEnabled = false

        binding.lytAddressInfo.tvCapital.setCompoundDrawablesWithIntrinsicBounds(
            0,
            0,
            R.drawable.ic_more_gray_ico,
            0
        )
        binding.lytAddressInfo.tvDistrict.setCompoundDrawablesWithIntrinsicBounds(
            0,
            0,
            R.drawable.ic_more_gray_ico,
            0
        )
        binding.lytAddressInfo.tvVillage.setCompoundDrawablesWithIntrinsicBounds(
            0,
            0,
            R.drawable.ic_more_gray_ico,
            0
        )

        binding.lytAddressInfo.tvCapital.setTextColor(
            ContextCompat.getColor(
                this,
                R.color.color_c4d0d6
            )
        )
        binding.lytAddressInfo.tvDistrict.setTextColor(
            ContextCompat.getColor(
                this,
                R.color.color_c4d0d6
            )
        )
        binding.lytAddressInfo.tvVillage.setTextColor(
            ContextCompat.getColor(
                this,
                R.color.color_c4d0d6
            )
        )

        if (intent != null && profileData != null) {
            binding.lytAddressInfo.tvCapital.text = profileData!!.address?.state?.alias1
            binding.lytAddressInfo.tvDistrict.text = profileData!!.address?.district?.alias1
            binding.lytAddressInfo.tvVillage.text = profileData!!.address?.village?.alias1
            binding.lytAddressInfo.edtDetailedAddress.hint = profileData!!.address?.state?.alias1
            binding.lytAddressInfo.edtDetailedAddress.hint = profileData!!.etcDetailedAddress
            binding.lytAddressInfo.edtEtc.hint = profileData!!.edtStatus.toString()
        }
    }

    private fun initActionBar() {
        val actionBar = binding.actionBar
        actionBar.setBackgroundActionBar(resources.getColor(R.color.white))
        actionBar.setToolbarTitleWithActionBack(
            R.drawable.ic_nav_close_dark_btn,
            getString(R.string.edit_acc_info)
        )
        actionBar.setOnMenuLeftClick {
            if (isUpdate as Boolean) {
                alertNotice()
            } else {
                onBackPressed()
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_edit_account_info
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_cancel -> {
                if (isUpdate as Boolean) {
                    alertNotice()
                } else {
                    onBackPressed()
                }
            }
            R.id.btn_save -> {
                val data = EditAccountInfoData()
                for (i in 0 until jobTypeCodeList!!.size) {
                    data.jobType = jobTypeCodeList!![selectedIndex!!]
                }
                data.bankName = binding.lytAddressInfo.edtBankName.text.toString()
                data.accountNumber = binding.lytAddressInfo.edtAccountNumber.text.toString()

                if (!sharedPrefer.getPrefer(Constants.USER_ID).isNullOrEmpty()) {
                    viewModel.editAccountInfo(data)
                }

            }
            R.id.ll_job_type -> {
                listChoiceDialog = ListChoiceDialog.newInstance(
                    R.drawable.ic_toggle_table_view_on_ico,
                    getString(R.string.addition_job_type),
                    jobTypeTitleList!!,
                    selectedIndex!!
                )
                listChoiceDialog.item = 5

                listChoiceDialog.setOnItemListener = {
                    binding.lytAddressInfo.tvJobType.text = jobTypeTitleList!![it]

                    for (i in 0 until jobTypeTitleList!!.size) {
                        if (jobTypeTitleList!![i] == jobTypeTitleList!![it]) {
                            selectedIndex = i

                            isUpdate = i != firstIndex
                        }
                    }

                    if (binding.lytAddressInfo.tvJobType.text.toString() == jobTypeTitleList!![firstIndex!!]) {
                        binding.btnSave.setActive(false)
                        binding.btnSave.setOnClickListener(null)
                    } else {
                        binding.btnSave.setActive(true)
                        binding.btnSave.setOnClickListener(this)
                    }
                }
                listChoiceDialog.isCancelable = true
                listChoiceDialog.show(supportFragmentManager, listChoiceDialog.tag)
            }
            R.id.cv_change_profile -> {



            }
            R.id.image_profile -> {

            }
        }
    }

    private fun alertNotice() {
        val alertEditInfoDialog = AlertEditInfoDialog()
        alertEditInfoDialog.onConfirmClickedListener { onBackPressed() }
        alertEditInfoDialog.show(supportFragmentManager, alertEditInfoDialog.tag)
    }

}