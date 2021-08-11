package com.mobile.bnkcl.ui.dialog

import com.bnkc.sourcemodule.base.BaseDialogFragment
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.DialogApplicationBinding
import com.mobile.bnkcl.databinding.DialogConnectionBinding
import com.mobile.bnkcl.databinding.DialogUpdateBinding

class ApplicationDialog : BaseDialogFragment<DialogApplicationBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.dialog_application
    }

}