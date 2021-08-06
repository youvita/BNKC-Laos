package com.bnkc.sourcemodule.dialog

import com.bnkc.sourcemodule.R
import com.bnkc.sourcemodule.base.BaseDialogFragment
import com.bnkc.sourcemodule.databinding.DialogLoadingBinding

class LoadingDialog: BaseDialogFragment<DialogLoadingBinding>() {

    override fun getLayoutId(): Int = R.layout.dialog_loading

}