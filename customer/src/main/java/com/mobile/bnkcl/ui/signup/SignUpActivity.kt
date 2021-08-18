package com.mobile.bnkcl.ui.signup

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import com.bnkc.sourcemodule.base.BaseActivity
import com.bnkc.sourcemodule.dialog.DatePickerDialog
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivitySignUpBinding
import com.mobile.bnkcl.com.alert.DlgArea
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignUpActivity : BaseActivity<ActivitySignUpBinding>() , View.OnClickListener{
    @Inject
    lateinit var datePickerDialog: DatePickerDialog

    private val addressInfoViewModel: AddressInfoViewModel by viewModels()


    private val viewModel : SignUpViewModel by viewModels()
    override fun getLayoutId(): Int = R.layout.activity_sign_up

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getPreSignUpData()

        binding.lltAddress.addressViewModel?.getCapitalData()


        binding.llDob.setOnClickListener(this)
        binding.lltAddress.llCapital.setOnClickListener(this)
        binding.lltAddress.llDistrict.setOnClickListener(this)
    }

    private fun setDate() {
        datePickerDialog.show(supportFragmentManager, datePickerDialog.tag)
        datePickerDialog.onDateSelected {
            Log.d("zimah", "Date: $it")
        }

    }

    private fun getPreSignUpData(){
        try {
            viewModel.getPreSignUpData()
            viewModel.mgData.observe(this){
                if (it != null) {
                    Log.d("zimah", "getPreSignUpData: ${it.session_id}")
                }
            }

        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun getCapitalData(){
        try {
            addressInfoViewModel.getCapitalData()
            addressInfoViewModel.capital.observe(this){
                if (it != null) {
                    Log.d("zimah", "getPreSignUpData: ${it.category}")
                }
            }

        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.ll_capital -> { getCapitalData() }
            R.id.ll_dob ->{setDate()}
            R.id.ll_village ->{}

        }
    }
}