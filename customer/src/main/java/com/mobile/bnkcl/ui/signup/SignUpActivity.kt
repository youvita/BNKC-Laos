package com.mobile.bnkcl.ui.signup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.bnkc.sourcemodule.base.BaseActivity
import com.bnkc.sourcemodule.dialog.DatePickerDialog
import com.bnkc.sourcemodule.dialog.ListChoiceDialog
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.request.area.AddressData
import com.mobile.bnkcl.data.response.area.AreaItems
import com.mobile.bnkcl.databinding.ActivitySignUpBinding
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
    private var objects: ArrayList<AreaItems>? = ArrayList()
    private lateinit var req : AddressData
    @Inject
    lateinit var listChoiceDialog: ListChoiceDialog
    private val addressInfoViewModel: AddressInfoViewModel by viewModels()
    private var signUpDisposable: Disposable? = null
    private var selectedItem = 0

    private var myCalendar = Calendar.getInstance()

    override fun getLayoutId(): Int = R.layout.activity_sign_up
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*observe data*/
        observeCapitalArea()
        observeAddressData()

        binding.ivBack.setOnClickListener(this)
        /**
         *
         * */
        binding.tvDob.addTextChangedListener(mDateWatcher)

        binding.llDob.setOnClickListener {
            datePickerDialog.show(supportFragmentManager, datePickerDialog.tag)
            datePickerDialog.onDateSelected {
                Log.d("Date: ", "onCreate: $it")
                binding.tvDob.setText(it)
            }
        }

        addressInfoViewModel.getCapitalData()

        binding.lltAddress.llCapital.setOnClickListener {
            if (objects != null && objects!!.size > 0){
                listChoiceDialog = ListChoiceDialog.newInstance(
                    R.drawable.ic_badge_general,
                    getString(R.string.addition_capital),
                    addressInfoViewModel.setUpAreaName(objects!!),
                    selectedItem
                )

                listChoiceDialog.setOnItemListener ={ a: Int ->
                    selectedItem = a
                    binding.lltAddress.tvCapital.text = objects!![a].alias1

                    addressInfoViewModel.addressData = AddressData(
                        "DISTRICT",
                        objects!![a].id.toString()
                    )
                    addressInfoViewModel.getAddress()

                }
                listChoiceDialog.isCancelable = true
                listChoiceDialog.show(supportFragmentManager, listChoiceDialog.tag)
            }
        }


        binding.lltAddress.llDistrict.setOnClickListener{
            if (objects != null && objects!!.size>0){
                listChoiceDialog = ListChoiceDialog.newInstance(
                    R.drawable.ic_badge_general,
                    "District",
                    addressInfoViewModel.setUpAreaName(objects!!),
                    selectedItem
                )

                listChoiceDialog.setOnItemListener = { b: Int ->
                    selectedItem = b
                    binding.lltAddress.tvDistrict.text = objects!![b].alias1

                    addressInfoViewModel.addressData = AddressData(
                        "VILLAGE",
                        objects!![b].id.toString()
                    )
                    addressInfoViewModel.getAddress()
                }

                listChoiceDialog.isCancelable = true
                listChoiceDialog.show(supportFragmentManager, listChoiceDialog.tag)
            }
        }

        binding.lltAddress.llVillage.setOnClickListener{
            if (objects != null && objects!!.size>0){
                listChoiceDialog = ListChoiceDialog.newInstance(
                    R.drawable.ic_badge_general,
                    "Village",
                    addressInfoViewModel.setUpAreaName(objects!!),
                    selectedItem
                )

                listChoiceDialog.setOnItemListener = { b: Int ->
                    selectedItem = b
                    binding.lltAddress.tvVillage.text = objects!![b].alias1
                }

                listChoiceDialog.isCancelable = true
                listChoiceDialog.show(supportFragmentManager, listChoiceDialog.tag)
            }

        }
    }

    private fun observeCapitalArea(){
        addressInfoViewModel.capital.observe(this){
            objects!!.clear()
            objects!!.addAll(it)
        }
    }

        private fun observeAddressData() {
       addressInfoViewModel.address.observe(this){
           objects!!.clear()
           objects!!.addAll(it)
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
            validateUserInfo()
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
            binding.btnNext.background = ContextCompat.getDrawable(
                this,
                R.drawable.selector_d7191f_8b0304
            )
        } else {
            binding.btnNext.isClickable = false
            binding.btnNext.background = ContextCompat.getDrawable(
                this,
                R.drawable.round_solid_e1e5ec_8
            )
        }
    }


    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.iv_back -> onBackPressed()

        }
    }
}