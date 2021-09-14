package com.mobile.bnkcl.ui.dialog

import android.os.Bundle
import android.view.View
import com.bnkc.sourcemodule.base.BaseDialogFragment
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.DialogConnectionBinding
import com.mobile.bnkcl.databinding.DialogUnderConstructionBinding
import com.mobile.bnkcl.databinding.DialogUpdateBinding

class UnderConstructionDialog : BaseDialogFragment<DialogUnderConstructionBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.dialog_under_construction
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnConfirm.setOnClickListener {
            dismiss()
        }
    }

}