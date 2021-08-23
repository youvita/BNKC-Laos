package com.mobile.bnkcl.ui.dialog

import android.os.Bundle
import android.view.View
import com.bnkc.sourcemodule.base.BaseDialogFragment
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.DialogSortBinding

class SortDialog(string: String) : BaseDialogFragment<DialogSortBinding>(), View.OnClickListener {

    lateinit var sortCode: String
    private var sortCheck: String = string

    override fun getLayoutId(): Int {
        return R.layout.dialog_sort
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCheckRadioButton()
    }

    private fun setCheckRadioButton() {
        if (sortCheck == "asc") {
            binding.rbNewest.isChecked = true
        } else {
            binding.rbOldest.isChecked = true
        }

        binding.rbNewest.setOnClickListener(this)
        binding.rbOldest.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rb_newest -> {
                sortCode = "asc"
                dialog?.dismiss()
            }
            R.id.rb_oldest -> {
                sortCode = "desc"
                dialog?.dismiss()
            }
        }
    }
}