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

class SystemDialog: BaseDialogFragment<DialogSystemBinding>() {

    private var confirmClickListener: (() -> Unit)? = null

    companion object {
        private const val TITLE   = "title"
        private const val MESSAGE = "message"

        @Synchronized
        fun newInstance(title: String, message: String): SystemDialog {
            return SystemDialog().apply {
                arguments = Bundle().apply {
                    putString(TITLE, title)
                    putString(MESSAGE, message)
                }
            }
        }
    }

    override fun getLayoutId(): Int = R.layout.dialog_system

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.errorTitle   = arguments?.getString(TITLE)
        binding.errorMessage = arguments?.getString(MESSAGE)
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