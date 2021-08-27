/**
 * KOSIGN (Cambodia) Investment Co., Ltd.
 * @author chan youvita
 * @since 2021.08.10
 */
package com.bnkc.sourcemodule.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.bnkc.sourcemodule.R
import com.bnkc.sourcemodule.base.BaseDialogFragment
import com.bnkc.sourcemodule.databinding.DialogConfirmBinding
import com.bnkc.sourcemodule.databinding.DialogSystemBinding

class ConfirmDialog : BaseDialogFragment<DialogConfirmBinding>() {

    private var confirmClickListener: (() -> Unit)? = null

    companion object {
        private const val ICON = "icon"
        private const val TITLE = "title"
        private const val MESSAGE = "message"
        private const val ACT = "act"

        @Synchronized
        fun newInstance(icon: Int, title: String, message: String, act: String): ConfirmDialog {
            return ConfirmDialog().apply {
                arguments = Bundle().apply {
                    putInt(ICON, icon)
                    putString(TITLE, title)
                    putString(MESSAGE, message)
                    putString(ACT, act)
                }
            }
        }
    }

    override fun getLayoutId(): Int = R.layout.dialog_confirm

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.appCompatImageView.background = ContextCompat.getDrawable(view.context, arguments?.getInt(ICON)!!)
        binding.errorTitle = arguments?.getString(TITLE)
        binding.errorMessage = arguments?.getString(MESSAGE)
        binding.errorAct = arguments?.getString(ACT)
        binding.login.setOnClickListener {
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