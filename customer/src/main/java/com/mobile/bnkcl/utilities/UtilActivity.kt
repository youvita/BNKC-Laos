package com.mobile.bnkcl.utilities

import com.mobile.bnkcl.ui.lease.apply.ApplyLeaseActivity
import com.mobile.bnkcl.ui.lease.service.LeaseServiceActivity
import com.mobile.bnkcl.ui.otp.OtpActivity

class UtilActivity {

    companion object{
        private var isCreated = false

        var leaseServiceActivity : LeaseServiceActivity? = null
        var otpActivity          : OtpActivity? = null
        var applyLeaseActivity   : ApplyLeaseActivity? = null

        fun isCreated(): Boolean {
            return isCreated
        }

        fun isCreated(isCreated: Boolean) {
            this.isCreated = isCreated
        }
    }

}