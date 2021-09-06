package com.mobile.bnkcl.ui.dialog

import android.widget.RadioGroup
import com.bnkc.sourcemodule.base.BaseDialogFragment
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.DialogGenderBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GenderDialog :BaseDialogFragment<DialogGenderBinding>(), RadioGroup.OnCheckedChangeListener {

    override fun getLayoutId(): Int {
        return R.layout.dialog_gender
    }

    lateinit var gender : String
    fun setCheckGender(){

    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when(checkedId){
            R.id.rb_male -> {}
            R.id.rb_female ->{}
        }
    }


}