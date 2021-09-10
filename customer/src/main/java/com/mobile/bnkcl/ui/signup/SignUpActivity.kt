package com.mobile.bnkcl.ui.signup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.base.BaseActivity
import com.bnkc.sourcemodule.dialog.DatePickerDialog
import com.bnkc.sourcemodule.dialog.ListChoiceDialog
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.request.area.AddressData
import com.mobile.bnkcl.data.request.auth.IdNumReq
import com.mobile.bnkcl.data.request.otp.SendOTPRequest
import com.mobile.bnkcl.data.response.area.AreaItems
import com.mobile.bnkcl.data.response.code.CodesData
import com.mobile.bnkcl.data.response.office.AreaDataResponse
import com.mobile.bnkcl.databinding.ActivitySignUpBinding
import com.mobile.bnkcl.ui.dialog.GenderDialog
import com.mobile.bnkcl.ui.dialog.SortDialog
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.Disposable
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.lang.NullPointerException
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

    private var num = SendOTPRequest()
    private lateinit var req : AddressData
    @Inject
    lateinit var listChoiceDialog: ListChoiceDialog
    private val addressInfoViewModel: AddressInfoViewModel by viewModels()
    private var signUpDisposable: Disposable? = null
    private var selectedItem = 0

    private var myCalendar = Calendar.getInstance()
    var identificationNumber = ""
    var username = ""
    private var genderCode = "M"

    private var clicked : Boolean = false
    private var clicked1 : Boolean = false
    private var clicked2 : Boolean = false
    private var clicked3 : Boolean = false


    override fun getLayoutId(): Int = R.layout.activity_sign_up
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*observe data*/
        observeCapitalArea()
        observeDistrictData()
        observeVillage()
        observeCode()
        observeGender()

        validateEdt()

        binding.ivBack.setOnClickListener(this)
        /**
         *
         * */
        binding.tvDob.addTextChangedListener(mDateWatcher)
        binding.edtIdNum.addTextChangedListener(textIdentificationNumberWatcher)
        binding.btnCheck.setOnClickListener(btnCheckId)

        binding.tvNewCustomer.visibility = View.GONE

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

        binding.lltAddress.llCapital.setOnClickListener {
            addressInfoViewModel.getCapitalData()
            if (objCapital != null && objCapital!!.size > 0){
                listChoiceDialog = ListChoiceDialog.newInstance(
                    R.drawable.ic_badge_general,
                    getString(R.string.addition_capital),
                    addressInfoViewModel.setUpAreaName(objCapital!!),
                    selectedItem
                )

                listChoiceDialog.setOnItemListener ={ a: Int ->
                    selectedItem = a
                    binding.lltAddress.tvCapital.text = objCapital!![a].alias1
                    binding.lltAddress.tvDistrict.text = ""
                    binding.lltAddress.tvVillage.text = ""
                    addressInfoViewModel.addressData = AddressData(
                        "DISTRICT",
                        objCapital!![a].id.toString()
                    )
                    addressInfoViewModel.getDistrict()
                }

                listChoiceDialog.isCancelable = true
                listChoiceDialog.show(supportFragmentManager, listChoiceDialog.tag)
            }
        }

        /**
         * on district click
         * */
        binding.lltAddress.llDistrict.setOnClickListener{
                    if (objDistrict != null && objDistrict!!.size>0){
                        listChoiceDialog = ListChoiceDialog.newInstance(
                                R.drawable.ic_badge_general,
                                "District",
                                addressInfoViewModel.setUpAreaName(objDistrict!!),
                                selectedItem
                        )

                        listChoiceDialog.setOnItemListener = { b: Int ->
                            selectedItem = b
                            binding.lltAddress.tvDistrict.text = objDistrict!![b].alias1
                            binding.lltAddress.tvVillage.text = ""

                            addressInfoViewModel.villageData = AddressData(
                                    "VILLAGE",
                                    objDistrict!![b].id.toString()
                            )
                            addressInfoViewModel.getVillage()
                        }
                        listChoiceDialog.isCancelable = true
                        listChoiceDialog.show(supportFragmentManager, listChoiceDialog.tag)
                    }
        }
        /**
         * on village click
         * */
        binding.lltAddress.llVillage.setOnClickListener{
                if (objVillage != null && objVillage!!.size>0){
                    listChoiceDialog = ListChoiceDialog.newInstance(
                            R.drawable.ic_badge_general,
                            "Village",
                            addressInfoViewModel.setUpAreaName(objVillage!!),
                            selectedItem
                    )

                    listChoiceDialog.setOnItemListener = { b: Int ->
                        selectedItem = b
                        binding.lltAddress.tvVillage.text = objVillage!![b].alias1
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
                        selectedItem
               )
               listChoiceDialog.setOnItemListener = {d : Int ->
                   selectedItem = d
                   binding.tvGender.text = genderObj!![d].title
               }

               listChoiceDialog.isCancelable = true
               listChoiceDialog.show(supportFragmentManager, listChoiceDialog.tag)
           }
        }


        /**
         * on Job type click
         * */

        binding.lltAddress.llJobType.setOnClickListener{
           if (codeObj != null && codeObj!!.size>0){
               listChoiceDialog = ListChoiceDialog.newInstance(
                       R.drawable.ic_badge_general,
                       "Job type",
                       addressInfoViewModel.setUpCode(codeObj!!),
                       selectedItem
               )
               listChoiceDialog.setOnItemListener = {c : Int ->
                   selectedItem = c
                   binding.lltAddress.tvJobType.text = codeObj!![c].title
               }

               listChoiceDialog.isCancelable = true
               listChoiceDialog.show(supportFragmentManager, listChoiceDialog.tag)
           }
        }
    }

    private fun observeCapitalArea(){
        try {
            addressInfoViewModel.getCapitalData()
            addressInfoViewModel.capital.observe(this){
                validateEdt()

                objCapital!!.clear()
                objCapital!!.addAll(it)
            }
        }catch (e:Exception){
            e.printStackTrace()
        }

    }

        private fun observeDistrictData() {
            try {
                addressInfoViewModel.district.observe(this){

                    validateEdt()
                    objDistrict!!.clear()
                    objDistrict!!.addAll(it)
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
    }

        private fun observeVillage(){
            try {
                addressInfoViewModel.village.observe(this){
                    validateEdt()
                    objVillage!!.clear()
                    objVillage!!.addAll(it)
                }

            }catch (e:Exception){
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
//            validateUserInfo()
        }
    }
    private fun setStringDateToCalendar(stDate: String) {
        try {
            if (stDate.length == 10) {
                val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.US)
                val date = sdf.parse(stDate)
                myCalendar.time = date
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }
    private fun validateUserInfo() {
        if (binding.tvDob.text.toString() != ""
            && binding.tvDob.text.toString().length === 10
            && binding.edtName.text.toString() != ""
            && binding.tvGender.text.toString() !=""
        ) {
            binding.btnNext.setOnClickListener(this)
            binding.btnNext.background = ContextCompat.getDrawable(this, R.drawable.selector_d7191f_8b0304)
        } else {
            binding.btnNext.isClickable = false
            binding.btnNext.background = ContextCompat.getDrawable(this, R.drawable.round_solid_e1e5ec_8)
        }
    }


    var textLength = 0
    private val textIdentificationNumberWatcher = object : TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            identificationNumber = binding.edtIdNum.text.toString()
//            binding.edtIdNum.tag = s
//            textLength = binding.edtIdNum.text.toString().length
//
//            binding.tvNewCustomer.visibility = View.GONE

        }

    }
    private fun validateEdt(){

        binding.lltAddress.llVillage.isEnabled = !(binding.lltAddress.tvDistrict.text.toString() == "" || binding.lltAddress.tvCapital.text.toString() =="")


        if (binding.lltAddress.tvCapital.text.toString() =="") {
            binding.lltAddress.llDistrict.isEnabled = false
            binding.lltAddress.llVillage.isEnabled = false
        }else{
            binding.lltAddress.llDistrict.isEnabled = true
        }
    }

    private val btnCheckId = View.OnClickListener {
        addressInfoViewModel.idNumReq = IdNumReq(username, identificationNumber)
       addressInfoViewModel.verifyId()
            verifyIdentification()

            Log.d("Id", "$num")
    }



    private fun verifyIdentification(){
        addressInfoViewModel.verifyIdLiveData.observe(this){
            successListener()
            binding.isVerified = it.verified!!
            binding.tvNewCustomer.visibility = View.VISIBLE

            if (it.verified!!){
                binding.llInputIdNum.background = ContextCompat.getDrawable(this,R.drawable.round_stroke_00695c_8)
                binding.tvNewCustomer.setTextColor(ContextCompat.getColor(this,R.color.color_00695c))
                binding.tvNewCustomer.text = getText(R.string.sign_new_customer)
                binding.tvNewCustomer.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    ContextCompat.getDrawable(this,R.drawable.ic_correct_ico),
                    null
                )

            }else {
                binding.llInputIdNum.background = ContextCompat.getDrawable(this,R.drawable.round_stroke_d7191f_solid_ffffff_8)
                binding.tvNewCustomer.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary))
                binding.tvNewCustomer.text = getText(R.string.sign_existing_customer)
                binding.tvNewCustomer.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    ContextCompat.getDrawable(this,R.drawable.ic_info_red_ico_1),
                    null
                )
            }
        }
    }


    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.iv_back -> onBackPressed()


        }
    }
}