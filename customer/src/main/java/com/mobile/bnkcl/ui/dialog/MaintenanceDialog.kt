package com.mobile.bnkcl.ui.dialog

import com.bnkc.sourcemodule.base.BaseDialogFragment
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.DialogMaintenanceBinding
import com.mobile.bnkcl.databinding.DialogUpdateBinding

class MaintenanceDialog : BaseDialogFragment<DialogMaintenanceBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.dialog_maintenance
    }

}