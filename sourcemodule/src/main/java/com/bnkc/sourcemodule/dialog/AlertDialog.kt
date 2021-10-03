/**
 * KOSIGN (Cambodia) Investment Co., Ltd.
 * @author chan youvita
 * @since 2021.08.10
 */
package com.bnkc.sourcemodule.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import com.bnkc.sourcemodule.R
import com.bnkc.sourcemodule.base.BaseDialogFragment
import com.bnkc.sourcemodule.databinding.DialogSystemBinding

class AlertDialog: BaseDialogFragment<DialogSystemBinding>() {

    private var confirmClickListener: (() -> Unit)? = null

    companion object {
        private const val ICON    = "icon"
        private const val TITLE   = "title"
        private const val MESSAGE = "message"
        private const val BUTTON  = "button"

        @Synchronized
        fun newInstance(icon: Int, title: String, message: String, button: String): AlertDialog {
            return AlertDialog().apply {
                arguments = Bundle().apply {
                    putInt(ICON, icon)
                    putString(TITLE, title)
                    putString(MESSAGE, message)
                    putString(BUTTON, button)
                }
            }
        }
    }

    override fun getLayoutId(): Int = R.layout.dialog_system

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setCancelable(false)

        binding.errorIcon    = arguments?.getInt(ICON)
        binding.errorTitle   = arguments?.getString(TITLE)
        binding.errorMessage = arguments?.getString(MESSAGE)
        binding.errorButton  = arguments?.getString(BUTTON)
        binding.confirm.setOnClickListener {
            confirmClickListener?.invoke()
            dialog?.dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        confirmClickListener = null
    }

    fun onConfirmClicked(confirmListener: (() -> Unit)) =
            apply { this.confirmClickListener = confirmListener }
}