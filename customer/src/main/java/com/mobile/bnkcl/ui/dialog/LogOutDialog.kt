package com.mobile.bnkcl.ui.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.bnkc.sourcemodule.base.BaseDialogFragment
import com.bnkc.sourcemodule.dialog.ConfirmDialog
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.DialogConnectionBinding
import com.mobile.bnkcl.databinding.DialogLogOutBinding
import com.mobile.bnkcl.databinding.DialogUpdateBinding

class LogOutDialog : BaseDialogFragment<DialogLogOutBinding>() {

    private var confirmClickListener: (() -> Unit)? = null

    override fun getLayoutId(): Int {
        return R.layout.dialog_log_out
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding.appCompatImageView.background = ContextCompat.getDrawable(view.context, arguments?.getInt(
//            ConfirmDialog.ICON
//        )!!)
//        binding.errorTitle = arguments?.getString(ConfirmDialog.TITLE)
//        binding.errorMessage = arguments?.getString(ConfirmDialog.MESSAGE)
//        binding.errorAct = arguments?.getString(ConfirmDialog.ACT)
        binding.btnLeft.setOnClickListener {
            dialog?.dismiss()
        }
        binding.btnRight.setOnClickListener {
            confirmClickListener?.invoke()
            dialog?.dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        confirmClickListener = null
    }

    fun onConfirmClickedListener(confirmListener: (() -> Unit)) =
        apply { this.confirmClickListener = confirmListener }

}