package com.bnkc.sourcemodule.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import com.bnkc.sourcemodule.R
import com.bnkc.sourcemodule.base.BaseDialogFragment
import com.bnkc.sourcemodule.databinding.DialogTwoButtonBinding

class TwoButtonDialog : BaseDialogFragment<DialogTwoButtonBinding>(){
    private var confirmClickListener: (() -> Unit)? = null

    override fun getLayoutId(): Int {
        return R.layout.dialog_two_button
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