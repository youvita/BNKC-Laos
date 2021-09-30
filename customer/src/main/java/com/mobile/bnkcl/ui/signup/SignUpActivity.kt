package com.mobile.bnkcl.ui.signup

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.bnkc.library.data.type.RunTimeDataStore
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.base.BaseActivity
import com.bnkc.sourcemodule.dialog.DatePickerDialog
import com.bnkc.sourcemodule.dialog.ListChoiceDialog
import com.bnkc.sourcemodule.dialog.SystemDialog
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.request.area.AddressData
import com.mobile.bnkcl.data.request.auth.IdNumReq
import com.mobile.bnkcl.data.response.area.AreaItems
import com.mobile.bnkcl.data.response.auth.AddressReqObj
import com.mobile.bnkcl.data.response.auth.AreaObj
import com.mobile.bnkcl.data.response.code.CodesData
import com.mobile.bnkcl.databinding.ActivitySignUpBinding
import com.mobile.bnkcl.ui.pinview.PinCodeActivity
import com.mobile.bnkcl.utilities.Utils
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.Disposable
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


@AndroidEntryPoint
class SignUpActivity : BaseActivity<ActivitySignUpBinding>() , View.OnClickListener{
    @Inject
    lateinit var datePickerDialog: DatePickerDialog
    private var objCapital: ArrayList<AreaItems>? = ArrayList()
    private var objDistrict : ArrayList<AreaItems>? = ArrayList()
    private var objVillage : ArrayList<AreaItems>? = ArrayList()
    private var codeObj : ArrayList<CodesData>? = ArrayList()
    private var genderObj : ArrayList<CodesData>? = ArrayList()

    @Inject
    lateinit var systemDialog: SystemDialog

    @Inject
    lateinit var listChoiceDialog: ListChoiceDialog
    private val addressInfoViewModel: AddressInfoViewModel by viewModels()
    private var selectedCapital = -1
    private var selectDistrict = -1
    private var selectVillage = -1
    private var selectGender = -1
    private var selectJobType = -1
    private var temp1 = -1
    private var temp2 = -1
    private var myCalendar = Calendar.getInstance()
    var identificationNumber = ""
    var username = ""
    private var signUpDisposable: Disposable? = null

    val viewModel : SignUpViewModel by viewModels()

    var isChecked : Boolean = false
    var isEnable : Boolean = false

    fun etcCheckBoxClick(){
        isChecked = !isChecked
        Log.d(">>>>>>", "Hello $isChecked")
    }

    override fun getLayoutId(): Int = R.layout.activity_sign_up
    override fun onCreate(savedInstanceState: Bundle?) {
        setStatusBarColor(ContextCompat.getColor(this, R.color.color_f5f7fc))
        super.onCreate(savedInstanceState)

        if (intent.hasExtra(Constants.USER_ID)){
            username = intent.getStringExtra(Constants.USER_ID).toString()
            viewModel.signUpRequest.phone_number = username
        }
        if (intent.hasExtra(Constants.SESSION_ID)){
            viewModel.signUpRequest.session_id = intent.getStringExtra(Constants.SESSION_ID).toString()
        }

        /*observe data*/
        addressInfoViewModel.getCapitalData()
        observeCapitalArea()
        observeDistrictData()
        observeVillage()
        observeCode()
        observeGender()

        /**
         * init hidden keyboard
         * */

        Utils.setHideKeyboard(this, binding.rlSignupInfo)

        /**
         *
         * Listener
         * */
        binding.ivBack.setOnClickListener(this)
        binding.tvDob.addTextChangedListener(mDateWatcher)
        binding.edtName.addTextChangedListener(nameWatcher)
        binding.edtIdNum.addTextChangedListener(textIdentificationNumberWatcher)
        binding.edtRecommend.addTextChangedListener(recommWatcher)
        binding.lltAdditional.edtDetailedAddress.addTextChangedListener(detailWatcher)
        binding.lltAdditional.edtEtc.addTextChangedListener(ectWatcher)
        binding.lltAdditional.edtBankName.addTextChangedListener(bankNameWatcher)
        binding.lltAdditional.edtAccountNumber.addTextChangedListener(accNumberWatcher)

        binding.btnCheck.setOnClickListener(btnCheckId)
        binding.vbResult.setOnClickListener {
            if (binding.vbResult.isActive()){
                val intent = Intent(this, PinCodeActivity::class.java)
                intent.putExtra("req_signup_obj", viewModel.signUpRequest)
                intent.putExtra("pin_action", "sign_up")
                startActivity(intent)
            }
        }


        /**
         * on date of birth click
         * */
        binding.llDob.setOnClickListener {
            datePickerDialog.show(supportFragmentManager, datePickerDialog.tag)
            datePickerDialog.onDateSelected {
                Log.d("Date: ", "onCreate: $it")
                binding.tvDob.setText(it)
            }
        }

        /**
         * on capital click
         * */

        binding.lltAdditional.tvCapital.setOnClickListener {
            var resetCapitalVal = ""
            var valChanged = false
            if (objCapital != null && objCapital!!.size > 0){
                listChoiceDialog = ListChoiceDialog.newInstance(
                    R.drawable.ic_badge_general,
                    getString(R.string.addition_capital),
                    addressInfoViewModel.setUpAreaName(objCapital!!),
                    selectedCapital
                )

                listChoiceDialog.setOnItemListener ={ a: Int ->
                    selectedCapital = a
                    binding.lltAdditional.tvCapital.text = objCapital!![a].name
                    binding.lltAdditional.tvCapital.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.color_263238
                        )
                    )
                    addressInfoViewModel.addressData = AddressData(
                        "DISTRICT",
                        objCapital!![a].id.toString()
                    )
                    addressInfoViewModel.getDistrict()
                    val capital = objCapital!![a]
                    val capitalData = AreaObj()
                    capitalData.id = capital.id
                    capitalData.name = capital.name
                    capitalData.erp_code = capital.erp_code
                    capitalData.iso_code = capital.iso_code
                    capitalData.alias1 = capital.alias1
                    capitalData.alias2 = capital.alias1

                    viewModel.addressReqObj.state = capitalData
                    viewModel.signUpRequest.address = viewModel.addressReqObj

                    val jobType = if (viewModel.signUpRequest.job_type != null){
                        viewModel.signUpRequest.job_type
                    }else {
                        ""
                    }
                    var gender = if (viewModel.signUpRequest.gender != null){
                        viewModel.signUpRequest.gender
                    }else {
                        ""
                    }

                    if (binding.lltAdditional.cbEtc.isChecked){
                        val address = viewModel.signUpRequest.etc_detailed_address!!
                        binding.vbResult.isEnable(
                            binding.edtName.text.toString(),
                            binding.tvDob.text.toString(),
                            binding.edtIdNum.text.toString(),
                            gender!!,
                            address,
                            binding.lltAdditional.edtDetailedAddress.text.toString(),
                            binding.lltAdditional.edtEtc.text.toString(),
                            binding.lltAdditional.edtBankName.text.toString(),
                            binding.lltAdditional.edtAccountNumber.text.toString(),
                            jobType!!,
                            binding.edtRecommend.text.toString(),
                        )
                    }else {
                        val capital = if (viewModel.addressReqObj.state != null){
                            viewModel.signUpRequest.job_type
                        }else {
                            ""
                        }
                        val district = if (viewModel.addressReqObj.district != null){
                            viewModel.signUpRequest.gender
                        }else {
                            ""
                        }
                        val village = if (viewModel.addressReqObj.village != null){
                            viewModel.signUpRequest.gender
                        }else {
                            ""
                        }
                        val detail = if (viewModel.addressReqObj.more_info != null){
                            viewModel.signUpRequest.gender
                        }else {
                            ""
                        }
                        binding.vbResult.isEnable(
                            binding.edtName.text.toString(),
                            binding.tvDob.text.toString(),
                            binding.edtIdNum.text.toString(),
                            gender!!,
                            capital!!,
                            district!!,
                            village!!,
                            detail!!,
                            binding.lltAdditional.edtDetailedAddress.text.toString(),
                            binding.lltAdditional.edtEtc.text.toString(),
                            binding.lltAdditional.edtBankName.text.toString(),
                            binding.lltAdditional.edtAccountNumber.text.toString(),
                            jobType!!,
                            binding.edtRecommend.text.toString(),
                        )
                    }

//                    binding.vbResult.isEnable(
//                        binding.edtName.text.toString(),
//                        binding.tvDob.text.toString(),
//                        binding.edtIdNum.text.toString(),
//                        "M",
//                        "capital",
//                        "district",
//                        "village",
//                        binding.lltAdditional.edtDetailedAddress.text.toString(),
//                        binding.lltAdditional.edtEtc.text.toString(),
//                        binding.lltAdditional.edtBankName.text.toString(),
//                        binding.lltAdditional.edtAccountNumber.text.toString(),
//                        jobType!!,
//                        binding.edtRecommend.text.toString(),
//                    )

                    removeToDefault(valChanged, 2)
                }

                listChoiceDialog.isCancelable = true
                listChoiceDialog.show(supportFragmentManager, listChoiceDialog.tag)
            }
        }

        /**
         * on district click
         * */
        binding.lltAdditional.tvDistrict.setOnClickListener{
            var resetCapitalVal = ""
            var valChanged = false
            if (objDistrict != null && objDistrict!!.size>0){
                objDistrict
                listChoiceDialog = ListChoiceDialog.newInstance(
                    R.drawable.ic_badge_general,
                    "District",
                    addressInfoViewModel.setUpAreaName(objDistrict!!),
                    selectDistrict
                )

                listChoiceDialog.setOnItemListener = { b: Int ->

                    selectDistrict = b
                    binding.lltAdditional.tvDistrict.text = objDistrict!![b].name
                    binding.lltAdditional.tvDistrict.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.color_263238
                        )
                    )

                    addressInfoViewModel.villageData = AddressData(
                        "VILLAGE",
                        objDistrict!![b].id.toString()
                    )
                    addressInfoViewModel.getVillage()

                    val district = objDistrict!![b]
                    val districtData = AreaObj()
                    districtData.id = district.id
                    districtData.name = district.name
                    districtData.erp_code = district.erp_code
                    districtData.iso_code = district.iso_code
                    districtData.alias1 = district.alias1
                    districtData.alias2 = district.alias1
                    viewModel.addressReqObj.district = districtData
                    viewModel.signUpRequest.address = viewModel.addressReqObj
//                    binding.lltAdditional.tvVillage.isEnabled = true
                    valChanged = !objDistrict!![b].name.equals(resetCapitalVal)
                    resetCapitalVal = objDistrict!![b].name!!

                    val jobType = if (viewModel.signUpRequest.job_type != null){
                        viewModel.signUpRequest.job_type
                    }else {
                        ""
                    }
                    var gender = if (viewModel.signUpRequest.gender != null){
                        viewModel.signUpRequest.gender
                    }else {
                        ""
                    }

                    var address = ""
                    if (binding.lltAdditional.cbEtc.isChecked){
                        address = viewModel.signUpRequest.etc_detailed_address!!
                        binding.vbResult.isEnable(
                            binding.edtName.text.toString(),
                            binding.tvDob.text.toString(),
                            binding.edtIdNum.text.toString(),
                            gender!!,
                            address,
                            binding.lltAdditional.edtDetailedAddress.text.toString(),
                            binding.lltAdditional.edtEtc.text.toString(),
                            binding.lltAdditional.edtBankName.text.toString(),
                            binding.lltAdditional.edtAccountNumber.text.toString(),
                            jobType!!,
                            binding.edtRecommend.text.toString(),
                        )
                    }else {
                        val capital = if (viewModel.addressReqObj.state != null){
                            viewModel.signUpRequest.job_type
                        }else {
                            ""
                        }
                        var district = if (viewModel.addressReqObj.district != null){
                            viewModel.signUpRequest.gender
                        }else {
                            ""
                        }
                        var village = if (viewModel.addressReqObj.village != null){
                            viewModel.signUpRequest.gender
                        }else {
                            ""
                        }
                        var detail = if (viewModel.addressReqObj.more_info != null){
                            viewModel.signUpRequest.gender
                        }else {
                            ""
                        }
                        binding.vbResult.isEnable(
                            binding.edtName.text.toString(),
                            binding.tvDob.text.toString(),
                            binding.edtIdNum.text.toString(),
                            gender!!,
                            capital!!,
                            district!!,
                            village!!,
                            detail!!,
                            binding.lltAdditional.edtDetailedAddress.text.toString(),
                            binding.lltAdditional.edtEtc.text.toString(),
                            binding.lltAdditional.edtBankName.text.toString(),
                            binding.lltAdditional.edtAccountNumber.text.toString(),
                            jobType!!,
                            binding.edtRecommend.text.toString(),
                        )
                    }

//                    binding.vbResult.isEnable(
//                        binding.edtName.text.toString(),
//                        binding.tvDob.text.toString(),
//                        binding.edtIdNum.text.toString(),
//                        gender!!,
//                        binding.lltAdditional.edtDetailedAddress.text.toString(),
//                        binding.lltAdditional.edtEtc.text.toString(),
//                        binding.lltAdditional.edtBankName.text.toString(),
//                        binding.lltAdditional.edtAccountNumber.text.toString(),
//                        jobType!!,
//                        binding.edtRecommend.text.toString(),
//                    )
                    removeToDefault(valChanged, 1)
                }

                listChoiceDialog.isCancelable = true
                listChoiceDialog.show(supportFragmentManager, listChoiceDialog.tag)
            }
        }
        /**
         * on village click
         * */
        binding.lltAdditional.tvVillage.setOnClickListener{
            if (objVillage != null && objVillage!!.size>0){
                listChoiceDialog = ListChoiceDialog.newInstance(
                    R.drawable.ic_badge_general,
                    "Village",
                    addressInfoViewModel.setUpAreaName(objVillage!!),
                    selectVillage
                )

                listChoiceDialog.setOnItemListener = { b: Int ->
                    selectVillage = b
                    binding.lltAdditional.tvVillage.text = objVillage!![b].name
                    binding.lltAdditional.tvVillage.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.color_263238
                        )
                    )
                    val village = objVillage!![b]
                    val villageData = AreaObj()
                    villageData.id = village.id
                    villageData.name = village.name
                    villageData.erp_code = village.erp_code
                    villageData.iso_code = village.iso_code
                    villageData.alias1 = village.alias1
                    villageData.alias2 = village.alias1
                    viewModel.addressReqObj.village = villageData
                    viewModel.signUpRequest.address = viewModel.addressReqObj
                    val jobType = if (viewModel.signUpRequest.job_type != null){
                        viewModel.signUpRequest.job_type
                    }else {
                        ""
                    }

                    var gender = if (viewModel.signUpRequest.gender != null){
                        viewModel.signUpRequest.gender
                    }else {
                        ""
                    }

                    if (binding.lltAdditional.cbEtc.isChecked){
                        val address = viewModel.signUpRequest.etc_detailed_address!!
                        binding.vbResult.isEnable(
                            binding.edtName.text.toString(),
                            binding.tvDob.text.toString(),
                            binding.edtIdNum.text.toString(),
                            gender!!,
                            address,
                            binding.lltAdditional.edtDetailedAddress.text.toString(),
                            binding.lltAdditional.edtEtc.text.toString(),
                            binding.lltAdditional.edtBankName.text.toString(),
                            binding.lltAdditional.edtAccountNumber.text.toString(),
                            jobType!!,
                            binding.edtRecommend.text.toString(),
                        )
                    }else {
                        val capital = if (viewModel.addressReqObj.state != null){
                            viewModel.signUpRequest.job_type
                        }else {
                            ""
                        }
                        val district = if (viewModel.addressReqObj.district != null){
                            viewModel.signUpRequest.gender
                        }else {
                            ""
                        }
                        val village = if (viewModel.addressReqObj.village != null){
                            viewModel.signUpRequest.gender
                        }else {
                            ""
                        }
                        val detail = if (viewModel.addressReqObj.more_info != null){
                            viewModel.signUpRequest.gender
                        }else {
                            ""
                        }
                        binding.vbResult.isEnable(
                            binding.edtName.text.toString(),
                            binding.tvDob.text.toString(),
                            binding.edtIdNum.text.toString(),
                            gender!!,
                            capital!!,
                            district!!,
                            village!!,
                            detail!!,
                            binding.lltAdditional.edtDetailedAddress.text.toString(),
                            binding.lltAdditional.edtEtc.text.toString(),
                            binding.lltAdditional.edtBankName.text.toString(),
                            binding.lltAdditional.edtAccountNumber.text.toString(),
                            jobType!!,
                            binding.edtRecommend.text.toString(),
                        )
                    }

//                    binding.vbResult.isEnable(
//                        binding.edtName.text.toString(),
//                        binding.tvDob.text.toString(),
//                        binding.edtIdNum.text.toString(),
//                        "M",
//                        "capital",
//                        "district",
//                        "village",
//                        binding.lltAdditional.edtDetailedAddress.text.toString(),
//                        binding.lltAdditional.edtEtc.text.toString(),
//                        binding.lltAdditional.edtBankName.text.toString(),
//                        binding.lltAdditional.edtAccountNumber.text.toString(),
//                        jobType!!,
//                        binding.edtRecommend.text.toString(),
//                    )

                }

                listChoiceDialog.isCancelable = true
                listChoiceDialog.show(supportFragmentManager, listChoiceDialog.tag)
            }
        }

        /**
         * on gender click
         * */

        binding.llGender.setOnClickListener{
            if (genderObj != null && genderObj!!.size>0){
                listChoiceDialog = ListChoiceDialog.newInstance(
                    R.drawable.ic_badge_general,
                    "Gender",
                    addressInfoViewModel.setUpGenderCode(genderObj!!),
                    selectGender
                )
               listChoiceDialog.setOnItemListener = { d: Int ->
                   selectGender = d
                   binding.tvGender.text = genderObj!![d].title
                   binding.tvGender.setTextColor(
                       ContextCompat.getColor(
                           this,
                           R.color.color_263238
                       )
                   )
                   viewModel.signUpRequest.gender = genderObj!![d].code

                   val jobType = if (viewModel.signUpRequest.job_type != null){
                       viewModel.signUpRequest.job_type
                   }else {
                       ""
                   }

                   var gender = if (viewModel.signUpRequest.gender != null){
                       viewModel.signUpRequest.gender
                   }else {
                       ""
                   }

                   if (binding.lltAdditional.cbEtc.isChecked){
                       val address = viewModel.signUpRequest.etc_detailed_address!!
                       binding.vbResult.isEnable(
                           binding.edtName.text.toString(),
                           binding.tvDob.text.toString(),
                           binding.edtIdNum.text.toString(),
                           gender!!,
                           address,
                           binding.lltAdditional.edtDetailedAddress.text.toString(),
                           binding.lltAdditional.edtEtc.text.toString(),
                           binding.lltAdditional.edtBankName.text.toString(),
                           binding.lltAdditional.edtAccountNumber.text.toString(),
                           jobType!!,
                           binding.edtRecommend.text.toString(),
                       )
                   }else {
                       val capital = if (viewModel.addressReqObj.state != null){
                           viewModel.signUpRequest.job_type
                       }else {
                           ""
                       }
                       val district = if (viewModel.addressReqObj.district != null){
                           viewModel.signUpRequest.gender
                       }else {
                           ""
                       }
                       val village = if (viewModel.addressReqObj.village != null){
                           viewModel.signUpRequest.gender
                       }else {
                           ""
                       }
                       val detail = if (viewModel.addressReqObj.more_info != null){
                           viewModel.signUpRequest.gender
                       }else {
                           ""
                       }
                       binding.vbResult.isEnable(
                           binding.edtName.text.toString(),
                           binding.tvDob.text.toString(),
                           binding.edtIdNum.text.toString(),
                           gender!!,
                           capital!!,
                           district!!,
                           village!!,
                           detail!!,
                           binding.lltAdditional.edtDetailedAddress.text.toString(),
                           binding.lltAdditional.edtEtc.text.toString(),
                           binding.lltAdditional.edtBankName.text.toString(),
                           binding.lltAdditional.edtAccountNumber.text.toString(),
                           jobType!!,
                           binding.edtRecommend.text.toString(),
                       )
                   }

//                   binding.vbResult.isEnable(
//                       binding.edtName.text.toString(),
//                       binding.tvDob.text.toString(),
//                       binding.edtIdNum.text.toString(),
//                       "M",
//                       "capital",
//                       "district",
//                       "village",
//                       binding.lltAdditional.edtDetailedAddress.text.toString(),
//                       binding.lltAdditional.edtEtc.text.toString(),
//                       binding.lltAdditional.edtBankName.text.toString(),
//                       binding.lltAdditional.edtAccountNumber.text.toString(),
//                       jobType!!,
//                       binding.edtRecommend.text.toString(),
//                   )
               }

               listChoiceDialog.isCancelable = true
               listChoiceDialog.show(supportFragmentManager, listChoiceDialog.tag)
           }
        }


        /**
         * on Job type click
         * */

        binding.lltAdditional.llJobType.setOnClickListener{
           if (codeObj != null && codeObj!!.size>0){
               listChoiceDialog = ListChoiceDialog.newInstance(
                   R.drawable.ic_badge_general,
                   "Job type",
                   addressInfoViewModel.setUpCode(codeObj!!),
                   selectJobType
               )
               val jobType = if (viewModel.signUpRequest.job_type != null){
                   viewModel.signUpRequest.job_type
               }else {
                   ""
               }
               listChoiceDialog.setOnItemListener = { c: Int ->
                   selectJobType = c
                   binding.lltAdditional.tvJobType.text = codeObj!![c].title
                   binding.lltAdditional.tvJobType.setTextColor(
                       ContextCompat.getColor(
                           this,
                           R.color.color_263238
                       )
                   )
                   viewModel.signUpRequest.job_type = codeObj!![selectJobType].code
                   var gender = if (viewModel.signUpRequest.gender != null){
                        viewModel.signUpRequest.gender
                    }else {
                        ""
                    }

                    if (binding.lltAdditional.cbEtc.isChecked){
                        val address = viewModel.signUpRequest.etc_detailed_address!!
                        binding.vbResult.isEnable(
                            binding.edtName.text.toString(),
                            binding.tvDob.text.toString(),
                            binding.edtIdNum.text.toString(),
                            gender!!,
                            address,
                            binding.lltAdditional.edtDetailedAddress.text.toString(),
                            binding.lltAdditional.edtEtc.text.toString(),
                            binding.lltAdditional.edtBankName.text.toString(),
                            binding.lltAdditional.edtAccountNumber.text.toString(),
                            jobType!!,
                            binding.edtRecommend.text.toString(),
                        )
                    }else {
                        val capital = if (viewModel.addressReqObj.state != null){
                            viewModel.signUpRequest.job_type
                        }else {
                            ""
                        }
                        val district = if (viewModel.addressReqObj.district != null){
                            viewModel.signUpRequest.gender
                        }else {
                            ""
                        }
                        val village = if (viewModel.addressReqObj.village != null){
                            viewModel.signUpRequest.gender
                        }else {
                            ""
                        }
                        val detail = if (viewModel.addressReqObj.more_info != null){
                            viewModel.signUpRequest.gender
                        }else {
                            ""
                        }
                        binding.vbResult.isEnable(
                            binding.edtName.text.toString(),
                            binding.tvDob.text.toString(),
                            binding.edtIdNum.text.toString(),
                            gender!!,
                            capital!!,
                            district!!,
                            village!!,
                            detail!!,
                            binding.lltAdditional.edtDetailedAddress.text.toString(),
                            binding.lltAdditional.edtEtc.text.toString(),
                            binding.lltAdditional.edtBankName.text.toString(),
                            binding.lltAdditional.edtAccountNumber.text.toString(),
                            jobType!!,
                            binding.edtRecommend.text.toString(),
                        )
                    }
//                   binding.vbResult.isEnable(
//                       binding.edtName.text.toString(),
//                       binding.tvDob.text.toString(),
//                       binding.edtIdNum.text.toString(),
//                       "M",
//                       "capital",
//                       "district",
//                       "village",
//                       binding.lltAdditional.edtDetailedAddress.text.toString(),
//                       binding.lltAdditional.edtEtc.text.toString(),
//                       binding.lltAdditional.edtBankName.text.toString(),
//                       binding.lltAdditional.edtAccountNumber.text.toString(),
//                       jobType!!,
//                       binding.edtRecommend.text.toString(),
//                   )
               }

               listChoiceDialog.isCancelable = true
               listChoiceDialog.show(supportFragmentManager, listChoiceDialog.tag)
           }
        }

        /**
         * on ETC check
         * */

        binding.lltAdditional.cbEtc.setOnClickListener {
            etcClick()
        }
    }

    private fun removeToDefault(valChanged : Boolean, n: Int) {
        when (n) {
            1 -> {
                //set default text before select item
                viewModel.signUpRequest.address?.village = null
                //reset selected index in list
                selectVillage = -1
                //Previous list
                objVillage?.clear()
                binding.lltAdditional.tvVillage.isEnabled = true
            }
            2 -> {
                //set default text before select item
                viewModel.signUpRequest.address?.district = null
                viewModel.signUpRequest.address?.village = null
                //reset selected index in list
                objDistrict!!.clear()
                objVillage!!.clear()
                binding.lltAdditional.tvDistrict.isEnabled = true
                binding.lltAdditional.tvVillage.isEnabled = false
//                communeId = 0
                //reset selected index in list
                selectDistrict = -1
                selectVillage = -1
            }
        }
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

    private fun observeCapitalArea(){
        try {
            addressInfoViewModel.getCapitalData()
            addressInfoViewModel.capital.observe(this){
                objCapital!!.clear()
                objCapital!!.addAll(it)
            }
        }catch (e: Exception){
            e.printStackTrace()
        }

    }

        private fun observeDistrictData() {
            try {
                addressInfoViewModel.district.observe(this){
                    objDistrict!!.clear()
                    objDistrict!!.addAll(it)
                }
            }catch (e: Exception){
                e.printStackTrace()
            }
    }

        private fun observeVillage(){
            try {
                addressInfoViewModel.village.observe(this){
                    objVillage!!.clear()
                    objVillage!!.addAll(it)
                }

            }catch (e: Exception){
                e.printStackTrace()
            }

        }



    private fun observeCode(){
        addressInfoViewModel.getCodes("JOB_TYPE")
        addressInfoViewModel.codesLiveData.observe(this){
            codeObj!!.clear()
            codeObj!!.addAll(it)
        }
    }
    private fun observeGender(){
        addressInfoViewModel.getGender("GENDER")
        addressInfoViewModel.genderLiveData.observe(this){
            genderObj!!.clear()
            genderObj!!.addAll(it)
        }
    }


     private val mDateWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) {
            val text = binding.tvDob.text.toString()
            val textLength: Int = binding.tvDob.text!!.length
            if (text.endsWith("-") || text.endsWith(" ")) return
            if (textLength == 3) {
                binding.tvDob.setText(
                    StringBuilder(text).insert(text.length - 1, "-").toString()
                )
                binding.tvDob.setSelection(
                    binding.tvDob.text.toString().length
                )
            } else if (textLength == 6) {
                binding.tvDob.setText(
                    StringBuilder(text).insert(text.length - 1, "-").toString()
                )
                binding.tvDob.setSelection(
                    binding.tvDob.text.toString().length
                )
            }
            if (textLength == 10) {
                setStringDateToCalendar(s.toString())
            }

            setDateRequestData(s.toString())

        }
    }
    private fun setStringDateToCalendar(stDate: String) {
        try {
            if (stDate.length == 10) {
                val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.US)
                val date = sdf.parse(stDate)
                myCalendar.time = date!!
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }

    private fun setDateRequestData(stDate: String) {
        try {
            if (stDate.split("-").size >= 3) {
                viewModel.signUpRequest.date_of_birth = stDate.split("-")[2].plus("-").plus(
                    stDate.split(
                        "-"
                    )[1]
                ).plus("-").plus(stDate.split("-")[0])
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }


    private val nameWatcher = object : TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            viewModel.signUpRequest.name = s.toString()
            val jobType = if (viewModel.signUpRequest.job_type != null){
                viewModel.signUpRequest.job_type
            }else {
                ""
            }
            var gender = if (viewModel.signUpRequest.gender != null){
                viewModel.signUpRequest.gender
            }else {
                ""
            }

            if (binding.lltAdditional.cbEtc.isChecked){
                val address = viewModel.signUpRequest.etc_detailed_address!!
                binding.vbResult.isEnable(
                    binding.edtName.text.toString(),
                    binding.tvDob.text.toString(),
                    binding.edtIdNum.text.toString(),
                    gender!!,
                    address,
                    binding.lltAdditional.edtDetailedAddress.text.toString(),
                    binding.lltAdditional.edtEtc.text.toString(),
                    binding.lltAdditional.edtBankName.text.toString(),
                    binding.lltAdditional.edtAccountNumber.text.toString(),
                    jobType!!,
                    binding.edtRecommend.text.toString(),
                )
            }else {
                val capital = if (viewModel.addressReqObj.state != null){
                    viewModel.signUpRequest.job_type
                }else {
                    ""
                }
                val district = if (viewModel.addressReqObj.district != null){
                    viewModel.signUpRequest.gender
                }else {
                    ""
                }
                val village = if (viewModel.addressReqObj.village != null){
                    viewModel.signUpRequest.gender
                }else {
                    ""
                }
                val detail = if (viewModel.addressReqObj.more_info != null){
                    viewModel.signUpRequest.gender
                }else {
                    ""
                }
                binding.vbResult.isEnable(
                    binding.edtName.text.toString(),
                    binding.tvDob.text.toString(),
                    binding.edtIdNum.text.toString(),
                    gender!!,
                    capital!!,
                    district!!,
                    village!!,
                    detail!!,
                    binding.lltAdditional.edtDetailedAddress.text.toString(),
                    binding.lltAdditional.edtEtc.text.toString(),
                    binding.lltAdditional.edtBankName.text.toString(),
                    binding.lltAdditional.edtAccountNumber.text.toString(),
                    jobType!!,
                    binding.edtRecommend.text.toString(),
                )
            }
//            binding.vbResult.isEnable(
//                s.toString(),
//                binding.tvDob.text.toString(),
//                binding.edtIdNum.text.toString(),
//                "M",
//                "capital",
//                "district",
//                "village",
//                binding.lltAdditional.edtDetailedAddress.text.toString(),
//                binding.lltAdditional.edtEtc.text.toString(),
//                binding.lltAdditional.edtBankName.text.toString(),
//                binding.lltAdditional.edtAccountNumber.text.toString(),
//                jobType!!,
//                binding.edtRecommend.text.toString(),
//            )
        }

    }

    private val textIdentificationNumberWatcher = object : TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            identificationNumber = binding.edtIdNum.text.toString()
            viewModel.signUpRequest.identification_number = s.toString()
            val jobType = if (viewModel.signUpRequest.job_type != null){
                viewModel.signUpRequest.job_type
            }else {
                ""
            }
            var gender = if (viewModel.signUpRequest.gender != null){
                viewModel.signUpRequest.gender
            }else {
                ""
            }

            if (binding.lltAdditional.cbEtc.isChecked){
                val address = viewModel.signUpRequest.etc_detailed_address!!
                binding.vbResult.isEnable(
                    binding.edtName.text.toString(),
                    binding.tvDob.text.toString(),
                    binding.edtIdNum.text.toString(),
                    gender!!,
                    address,
                    binding.lltAdditional.edtDetailedAddress.text.toString(),
                    binding.lltAdditional.edtEtc.text.toString(),
                    binding.lltAdditional.edtBankName.text.toString(),
                    binding.lltAdditional.edtAccountNumber.text.toString(),
                    jobType!!,
                    binding.edtRecommend.text.toString(),
                )
            }else {
                val capital = if (viewModel.addressReqObj.state != null){
                    viewModel.signUpRequest.job_type
                }else {
                    ""
                }
                val district = if (viewModel.addressReqObj.district != null){
                    viewModel.signUpRequest.gender
                }else {
                    ""
                }
                val village = if (viewModel.addressReqObj.village != null){
                    viewModel.signUpRequest.gender
                }else {
                    ""
                }
                val detail = if (viewModel.addressReqObj.more_info != null){
                    viewModel.signUpRequest.gender
                }else {
                    ""
                }
                binding.vbResult.isEnable(
                    binding.edtName.text.toString(),
                    binding.tvDob.text.toString(),
                    binding.edtIdNum.text.toString(),
                    gender!!,
                    capital!!,
                    district!!,
                    village!!,
                    detail!!,
                    binding.lltAdditional.edtDetailedAddress.text.toString(),
                    binding.lltAdditional.edtEtc.text.toString(),
                    binding.lltAdditional.edtBankName.text.toString(),
                    binding.lltAdditional.edtAccountNumber.text.toString(),
                    jobType!!,
                    binding.edtRecommend.text.toString(),
                )
            }
//            binding.vbResult.isEnable(
//                binding.edtName.text.toString(),
//                binding.tvDob.text.toString(),
//                s.toString(),
//                "M",
//                "capital",
//                "district",
//                "village",
//                binding.lltAdditional.edtDetailedAddress.text.toString(),
//                binding.lltAdditional.edtEtc.text.toString(),
//                binding.lltAdditional.edtBankName.text.toString(),
//                binding.lltAdditional.edtAccountNumber.text.toString(),
//                jobType!!,
//                binding.edtRecommend.text.toString(),
//            )
        }

    }

    private val detailWatcher = object : TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            viewModel.addressReqObj.more_info = s.toString()
            viewModel.signUpRequest.address = viewModel.addressReqObj
            val jobType = if (viewModel.signUpRequest.job_type != null){
                viewModel.signUpRequest.job_type
            }else {
                ""
            }
            var gender = if (viewModel.signUpRequest.gender != null){
                viewModel.signUpRequest.gender
            }else {
                ""
            }

            if (binding.lltAdditional.cbEtc.isChecked){
                val address = viewModel.signUpRequest.etc_detailed_address!!
                binding.vbResult.isEnable(
                    binding.edtName.text.toString(),
                    binding.tvDob.text.toString(),
                    binding.edtIdNum.text.toString(),
                    gender!!,
                    address,
                    binding.lltAdditional.edtDetailedAddress.text.toString(),
                    binding.lltAdditional.edtEtc.text.toString(),
                    binding.lltAdditional.edtBankName.text.toString(),
                    binding.lltAdditional.edtAccountNumber.text.toString(),
                    jobType!!,
                    binding.edtRecommend.text.toString(),
                )
            }else {
                val capital = if (viewModel.addressReqObj.state != null){
                    viewModel.signUpRequest.job_type
                }else {
                    ""
                }
                val district = if (viewModel.addressReqObj.district != null){
                    viewModel.signUpRequest.gender
                }else {
                    ""
                }
                val village = if (viewModel.addressReqObj.village != null){
                    viewModel.signUpRequest.gender
                }else {
                    ""
                }
                val detail = if (viewModel.addressReqObj.more_info != null){
                    viewModel.signUpRequest.gender
                }else {
                    ""
                }
                binding.vbResult.isEnable(
                    binding.edtName.text.toString(),
                    binding.tvDob.text.toString(),
                    binding.edtIdNum.text.toString(),
                    gender!!,
                    capital!!,
                    district!!,
                    village!!,
                    detail!!,
                    binding.lltAdditional.edtDetailedAddress.text.toString(),
                    binding.lltAdditional.edtEtc.text.toString(),
                    binding.lltAdditional.edtBankName.text.toString(),
                    binding.lltAdditional.edtAccountNumber.text.toString(),
                    jobType!!,
                    binding.edtRecommend.text.toString(),
                )
            }
//            binding.vbResult.isEnable(
//                s.toString(),
//                binding.tvDob.text.toString(),
//                binding.edtIdNum.text.toString(),
//                "M",
//                "capital",
//                "district",
//                "village",
//                s.toString(),
//                binding.lltAdditional.edtEtc.text.toString(),
//                binding.lltAdditional.edtBankName.text.toString(),
//                binding.lltAdditional.edtAccountNumber.text.toString(),
//                jobType!!,
//                binding.edtRecommend.text.toString(),
//            )
        }

    }

    private val ectWatcher = object : TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            viewModel.signUpRequest.etc_detailed_address = s.toString()
            val jobType = if (viewModel.signUpRequest.job_type != null){
                viewModel.signUpRequest.job_type
            }else {
                ""
            }
            var gender = if (viewModel.signUpRequest.gender != null){
                viewModel.signUpRequest.gender
            }else {
                ""
            }

            if (binding.lltAdditional.cbEtc.isChecked){
                val address = viewModel.signUpRequest.etc_detailed_address!!
                binding.vbResult.isEnable(
                    binding.edtName.text.toString(),
                    binding.tvDob.text.toString(),
                    binding.edtIdNum.text.toString(),
                    gender!!,
                    address,
                    binding.lltAdditional.edtDetailedAddress.text.toString(),
                    binding.lltAdditional.edtEtc.text.toString(),
                    binding.lltAdditional.edtBankName.text.toString(),
                    binding.lltAdditional.edtAccountNumber.text.toString(),
                    jobType!!,
                    binding.edtRecommend.text.toString(),
                )
            }else {
                val capital = if (viewModel.addressReqObj.state != null){
                    viewModel.signUpRequest.job_type
                }else {
                    ""
                }
                val district = if (viewModel.addressReqObj.district != null){
                    viewModel.signUpRequest.gender
                }else {
                    ""
                }
                val village = if (viewModel.addressReqObj.village != null){
                    viewModel.signUpRequest.gender
                }else {
                    ""
                }
                val detail = if (viewModel.addressReqObj.more_info != null){
                    viewModel.signUpRequest.gender
                }else {
                    ""
                }
                binding.vbResult.isEnable(
                    binding.edtName.text.toString(),
                    binding.tvDob.text.toString(),
                    binding.edtIdNum.text.toString(),
                    gender!!,
                    capital!!,
                    district!!,
                    village!!,
                    detail!!,
                    binding.lltAdditional.edtDetailedAddress.text.toString(),
                    binding.lltAdditional.edtEtc.text.toString(),
                    binding.lltAdditional.edtBankName.text.toString(),
                    binding.lltAdditional.edtAccountNumber.text.toString(),
                    jobType!!,
                    binding.edtRecommend.text.toString(),
                )
            }
//            binding.vbResult.isEnable(
//                s.toString(),
//                binding.tvDob.text.toString(),
//                binding.edtIdNum.text.toString(),
//                "M",
//                "capital",
//                "district",
//                "village",
//                binding.lltAdditional.edtDetailedAddress.text.toString(),
//                s.toString(),
//                binding.lltAdditional.edtBankName.text.toString(),
//                binding.lltAdditional.edtAccountNumber.text.toString(),
//                jobType!!,
//                binding.edtRecommend.text.toString(),
//            )
        }

    }

    private val bankNameWatcher = object : TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            viewModel.signUpRequest.bank_name = s.toString()
            val jobType = if (viewModel.signUpRequest.job_type != null){
                viewModel.signUpRequest.job_type
            }else {
                ""
            }
            var gender = if (viewModel.signUpRequest.gender != null){
                viewModel.signUpRequest.gender
            }else {
                ""
            }

            if (binding.lltAdditional.cbEtc.isChecked){
                val address = viewModel.signUpRequest.etc_detailed_address!!
                binding.vbResult.isEnable(
                    binding.edtName.text.toString(),
                    binding.tvDob.text.toString(),
                    binding.edtIdNum.text.toString(),
                    gender!!,
                    address,
                    binding.lltAdditional.edtDetailedAddress.text.toString(),
                    binding.lltAdditional.edtEtc.text.toString(),
                    binding.lltAdditional.edtBankName.text.toString(),
                    binding.lltAdditional.edtAccountNumber.text.toString(),
                    jobType!!,
                    binding.edtRecommend.text.toString(),
                )
            }else {
                val capital = if (viewModel.addressReqObj.state != null){
                    viewModel.signUpRequest.job_type
                }else {
                    ""
                }
                val district = if (viewModel.addressReqObj.district != null){
                    viewModel.signUpRequest.gender
                }else {
                    ""
                }
                val village = if (viewModel.addressReqObj.village != null){
                    viewModel.signUpRequest.gender
                }else {
                    ""
                }
                val detail = if (viewModel.addressReqObj.more_info != null){
                    viewModel.signUpRequest.gender
                }else {
                    ""
                }
                binding.vbResult.isEnable(
                    binding.edtName.text.toString(),
                    binding.tvDob.text.toString(),
                    binding.edtIdNum.text.toString(),
                    gender!!,
                    capital!!,
                    district!!,
                    village!!,
                    detail!!,
                    binding.lltAdditional.edtDetailedAddress.text.toString(),
                    binding.lltAdditional.edtEtc.text.toString(),
                    binding.lltAdditional.edtBankName.text.toString(),
                    binding.lltAdditional.edtAccountNumber.text.toString(),
                    jobType!!,
                    binding.edtRecommend.text.toString(),
                )
            }
//            binding.vbResult.isEnable(
//                s.toString(),
//                binding.tvDob.text.toString(),
//                binding.edtIdNum.text.toString(),
//                "M",
//                "capital",
//                "district",
//                "village",
//                binding.lltAdditional.edtDetailedAddress.text.toString(),
//                binding.lltAdditional.edtEtc.text.toString(),
//                s.toString(),
//                binding.lltAdditional.edtAccountNumber.text.toString(),
//                jobType!!,
//                binding.edtRecommend.text.toString(),
//            )
        }

    }

    private val accNumberWatcher = object : TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            viewModel.signUpRequest.account_number = s.toString()
            val jobType = if (viewModel.signUpRequest.job_type != null){
                viewModel.signUpRequest.job_type
            }else {
                ""
            }
            var gender = if (viewModel.signUpRequest.gender != null){
                viewModel.signUpRequest.gender
            }else {
                ""
            }

            if (binding.lltAdditional.cbEtc.isChecked){
                val address = viewModel.signUpRequest.etc_detailed_address!!
                binding.vbResult.isEnable(
                    binding.edtName.text.toString(),
                    binding.tvDob.text.toString(),
                    binding.edtIdNum.text.toString(),
                    gender!!,
                    address,
                    binding.lltAdditional.edtDetailedAddress.text.toString(),
                    binding.lltAdditional.edtEtc.text.toString(),
                    binding.lltAdditional.edtBankName.text.toString(),
                    binding.lltAdditional.edtAccountNumber.text.toString(),
                    jobType!!,
                    binding.edtRecommend.text.toString(),
                )
            }else {
                val capital = if (viewModel.addressReqObj.state != null){
                    viewModel.signUpRequest.job_type
                }else {
                    ""
                }
                val district = if (viewModel.addressReqObj.district != null){
                    viewModel.signUpRequest.gender
                }else {
                    ""
                }
                val village = if (viewModel.addressReqObj.village != null){
                    viewModel.signUpRequest.gender
                }else {
                    ""
                }
                val detail = if (viewModel.addressReqObj.more_info != null){
                    viewModel.signUpRequest.gender
                }else {
                    ""
                }
                binding.vbResult.isEnable(
                    binding.edtName.text.toString(),
                    binding.tvDob.text.toString(),
                    binding.edtIdNum.text.toString(),
                    gender!!,
                    capital!!,
                    district!!,
                    village!!,
                    detail!!,
                    binding.lltAdditional.edtDetailedAddress.text.toString(),
                    binding.lltAdditional.edtEtc.text.toString(),
                    binding.lltAdditional.edtBankName.text.toString(),
                    binding.lltAdditional.edtAccountNumber.text.toString(),
                    jobType!!,
                    binding.edtRecommend.text.toString(),
                )
            }
//            binding.vbResult.isEnable(
//                s.toString(),
//                binding.tvDob.text.toString(),
//                binding.edtIdNum.text.toString(),
//                "M",
//                "capital",
//                "district",
//                "village",
//                binding.lltAdditional.edtDetailedAddress.text.toString(),
//                binding.lltAdditional.edtEtc.text.toString(),
//                binding.lltAdditional.edtBankName.text.toString(),
//                s.toString(),
//                jobType!!,
//                binding.edtRecommend.text.toString(),
//            )
        }

    }

    private val recommWatcher = object : TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            viewModel.signUpRequest.recommender = s.toString()
            val jobType = if (viewModel.signUpRequest.job_type != null){
                viewModel.signUpRequest.job_type
            }else {
                ""
            }
            var gender = if (viewModel.signUpRequest.gender != null){
                viewModel.signUpRequest.gender
            }else {
                ""
            }

            if (binding.lltAdditional.cbEtc.isChecked){
                val address = viewModel.signUpRequest.etc_detailed_address!!
                binding.vbResult.isEnable(
                    binding.edtName.text.toString(),
                    binding.tvDob.text.toString(),
                    binding.edtIdNum.text.toString(),
                    gender!!,
                    address,
                    binding.lltAdditional.edtDetailedAddress.text.toString(),
                    binding.lltAdditional.edtEtc.text.toString(),
                    binding.lltAdditional.edtBankName.text.toString(),
                    binding.lltAdditional.edtAccountNumber.text.toString(),
                    jobType!!,
                    binding.edtRecommend.text.toString(),
                )
            }else {
                val capital = if (viewModel.addressReqObj.state != null){
                    viewModel.signUpRequest.job_type
                }else {
                    ""
                }
                val district = if (viewModel.addressReqObj.district != null){
                    viewModel.signUpRequest.gender
                }else {
                    ""
                }
                val village = if (viewModel.addressReqObj.village != null){
                    viewModel.signUpRequest.gender
                }else {
                    ""
                }
                val detail = if (viewModel.addressReqObj.more_info != null){
                    viewModel.signUpRequest.gender
                }else {
                    ""
                }
                binding.vbResult.isEnable(
                    binding.edtName.text.toString(),
                    binding.tvDob.text.toString(),
                    binding.edtIdNum.text.toString(),
                    gender!!,
                    capital!!,
                    district!!,
                    village!!,
                    detail!!,
                    binding.lltAdditional.edtDetailedAddress.text.toString(),
                    binding.lltAdditional.edtEtc.text.toString(),
                    binding.lltAdditional.edtBankName.text.toString(),
                    binding.lltAdditional.edtAccountNumber.text.toString(),
                    jobType!!,
                    binding.edtRecommend.text.toString(),
                )
            }
//            binding.vbResult.isEnable(
//                s.toString(),
//                binding.tvDob.text.toString(),
//                binding.edtIdNum.text.toString(),
//                "M",
//                "capital",
//                "district",
//                "village",
//                binding.lltAdditional.edtDetailedAddress.text.toString(),
//                binding.lltAdditional.edtEtc.text.toString(),
//                binding.lltAdditional.edtBankName.text.toString(),
//                binding.lltAdditional.edtAccountNumber.text.toString(),
//                jobType!!,
//                s.toString(),
//            )
        }

    }

//    private fun validateEdtAddress(){
//        binding.lltAdditional.llVillage.isEnabled = !(binding.lltAdditional.tvDistrict.text.toString() == "" || binding.lltAdditional.tvCapital.text.toString() =="")
//        if (binding.lltAdditional.tvCapital.text.toString() =="") {
//            binding.lltAdditional.llDistrict.isEnabled = false
//            binding.lltAdditional.llVillage.isEnabled = false
//        }else{
//            binding.lltAdditional.llDistrict.isEnabled = true
//        }
//    }

    private val btnCheckId = View.OnClickListener {

        identificationNumber = binding.edtIdNum.text.toString()
        addressInfoViewModel.idNumReq = IdNumReq(username, identificationNumber)
        addressInfoViewModel.verifyId()
        verifyIdentification()

    }


    private fun verifyIdentification(){
        addressInfoViewModel.verifyIdLiveData.observe(this){
            binding.isVerified = it.verified!!
            binding.tvNewCustomer.visibility = View.VISIBLE

            if (it.verified!!){
                binding.llInputIdNum.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.round_stroke_00695c_8
                )
                binding.tvNewCustomer.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.color_00695c
                    )
                )
                binding.tvNewCustomer.text = getText(R.string.sign_new_customer)
                binding.tvNewCustomer.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    ContextCompat.getDrawable(this, R.drawable.ic_correct_ico),
                    null
                )

            }else {
                binding.llInputIdNum.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.round_stroke_d7191f_solid_ffffff_8
                )
                binding.tvNewCustomer.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorPrimary
                    )
                )
                binding.tvNewCustomer.text = getText(R.string.sign_existing_customer)
                binding.tvNewCustomer.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    ContextCompat.getDrawable(this, R.drawable.ic_info_red_ico_1),
                    null
                )

                systemDialog = SystemDialog.newInstance(R.drawable.ic_badge_inform, getString(R.string.dlg_21), getString(R.string.dlg_20), getString(R.string.dlg_06))
                systemDialog.show(supportFragmentManager, systemDialog.tag)
                systemDialog.onConfirmClicked {

                }


            }
        }
    }

    var tempAddressObj : AddressReqObj? = null
    private fun etcClick(){
        tempAddressObj = viewModel.addressReqObj
        if (binding.lltAdditional.cbEtc.isChecked == isChecked){
            binding.lltAdditional.edtEtc.isEnabled = isEnable
            binding.lltAdditional.tvCapital.isEnabled = !isEnable
            binding.lltAdditional.edtDetailedAddress.isEnabled = !isEnable
            binding.lltAdditional.tvCapital.setTextColor(ContextCompat.getColor(this, com.bnkc.sourcemodule.R.color.color_000000))
            binding.lltAdditional.edtDetailedAddress.setTextColor(ContextCompat.getColor(this, com.bnkc.sourcemodule.R.color.color_000000))

            viewModel.addressReqObj = AddressReqObj()
            viewModel.signUpRequest.etc_detailed_address = binding.lltAdditional.edtEtc.text.toString()

        }else{
            binding.lltAdditional.edtEtc.isEnabled = !isEnable
            binding.lltAdditional.tvCapital.isEnabled = isEnable
            binding.lltAdditional.tvDistrict.isEnabled = isEnable
            binding.lltAdditional.tvVillage.isEnabled = isEnable
            binding.lltAdditional.edtDetailedAddress.isEnabled = isEnable

            viewModel.addressReqObj = tempAddressObj!!
            tempAddressObj = viewModel.addressReqObj

            binding.lltAdditional.tvCapital.setTextColor(ContextCompat.getColor(this, com.bnkc.sourcemodule.R.color.color_90a4ae))
            binding.lltAdditional.tvDistrict.setTextColor(ContextCompat.getColor(this, com.bnkc.sourcemodule.R.color.color_90a4ae))
            binding.lltAdditional.tvVillage.setTextColor(ContextCompat.getColor(this, com.bnkc.sourcemodule.R.color.color_90a4ae))
            binding.lltAdditional.edtDetailedAddress.setTextColor(ContextCompat.getColor(this, com.bnkc.sourcemodule.R.color.color_90a4ae))

        }


    }


    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.iv_back -> onBackPressed()
        }
    }
}