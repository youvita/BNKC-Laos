package com.mobile.bnkcl.ui.dialog

import android.os.Bundle
import android.view.View
import android.widget.RadioGroup
import com.bnkc.sourcemodule.base.BaseDialogFragment
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.DialogGenderBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class GenderDialog(string: String) :BaseDialogFragment<DialogGenderBinding>(),View.OnClickListener {

    override fun getLayoutId(): Int {
        return R.layout.dialog_gender
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCheckGenderButton()
    }

    var genderCode : String = string
    var genderCheck : String? = null
    private fun setCheckGenderButton(){
        if (genderCheck == "M") {
            binding.rbMale.isChecked = true
        } else {
            binding.rbFemale.isChecked = true
        }

        binding.rbMale.setOnClickListener(this)
        binding.rbFemale.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rb_male -> {
                genderCode = "M"
                dialog?.dismiss()
            }
            R.id.rb_female -> {
                genderCode = "F"
                dialog?.dismiss()
            }
        }    }


}