package com.mobile.bnkcl.ui.signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import com.bnkc.sourcemodule.base.BaseActivity
import com.bnkc.sourcemodule.base.BaseDialogFragment
import com.bnkc.sourcemodule.dialog.DatePickerDialog
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivitySignUpBinding
import com.mobile.bnkcl.databinding.DialogLogOutBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignUpActivity : BaseActivity<ActivitySignUpBinding>() {

    @Inject
 lateinit   var datePickerDialog: DatePickerDialog

    private val viewModel : SignUpViewModel by viewModels()
    override fun getLayoutId(): Int = R.layout.activity_sign_up

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.llDob.setOnClickListener(){
            setDate()
        }


    }

    private fun setDate() {
        datePickerDialog.show(supportFragmentManager, datePickerDialog.tag)
        datePickerDialog.onDateSelected {
            Log.d("zimah", "Date: $it")
        }

    }

}