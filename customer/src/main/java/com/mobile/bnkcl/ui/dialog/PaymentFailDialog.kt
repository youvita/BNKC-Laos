package com.mobile.bnkcl.ui.dialog

import com.bnkc.sourcemodule.base.BaseDialogFragment
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.DialogConnectionBinding
import com.mobile.bnkcl.databinding.DialogPaymentFailBinding
import com.mobile.bnkcl.databinding.DialogPaymentSuccessBinding
import com.mobile.bnkcl.databinding.DialogUpdateBinding

class PaymentFailDialog : BaseDialogFragment<DialogPaymentFailBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.dialog_payment_fail
    }

}