package com.mobile.bnkcl.ui.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import com.bnkc.sourcemodule.base.BaseDialogFragment
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.DialogAlertEditInfoBinding

class AlertEditInfoDialog : BaseDialogFragment<DialogAlertEditInfoBinding>() {

    private var confirmClickListener: (() -> Unit)? = null

    override fun getLayoutId(): Int {
        return R.layout.dialog_alert_edit_info
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLeft.setOnClickListener {
            dialog?.dismiss()
        }
        binding.btnRight.setOnClickListener {
            confirmClickListener?.invoke()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        confirmClickListener = null
    }

    fun onConfirmClickedListener(confirmListener: (() -> Unit)) =
        apply { this.confirmClickListener = confirmListener }

}