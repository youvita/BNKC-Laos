package com.mobile.bnkcl.ui.management.mobile_payment

import com.bnkc.sourcemodule.base.BaseViewModel

class MobilePaymentViewModel(var REPAYMENT_AMOUNT: String, var REPAYMENT_PLAN: String) : BaseViewModel() {

    private val CONTRACT_NO: String? = null
    private var SESSION_ID: String? = ""
    private var WING_ACCOUNT: String? = null
    private var SECURITY_CODE: String? = null

}