package com.bnkc.sourcemodule.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.bnkc.sourcemodule.R
import com.bnkc.sourcemodule.base.BaseDialogFragment
import com.bnkc.sourcemodule.databinding.DialogTwoButtonBinding

class TwoButtonDialog : BaseDialogFragment<DialogTwoButtonBinding>(){
    private var confirmClickListener: (() -> Unit)? = null

    override fun getLayoutId(): Int {
        return R.layout.dialog_two_button
    }

    companion object {
        private const val ICON = "icon"
        private const val TITLE = "title"
        private const val MESSAGE = "message"
        private const val LEFT = "left"
        private const val RIGHT = "right"

        @Synchronized
        fun newInstance(icon: Int, title: String, message: String, left: String, right: String): TwoButtonDialog {
            return TwoButtonDialog().apply {
                arguments = Bundle().apply {
                    putInt(ICON, icon)
                    putString(TITLE, title)
                    putString(MESSAGE, message)
                    putString(LEFT, left)
                    putString(RIGHT, right)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imgTitle.setImageDrawable(ContextCompat.getDrawable(view.context, arguments?.getInt(
            ICON
        )!!))
        binding.errorTitle = arguments?.getString(TITLE)
        binding.errorMessage = arguments?.getString(MESSAGE)
        binding.leftLabel = arguments?.getString(LEFT)
        binding.rightLabel = arguments?.getString(RIGHT)

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